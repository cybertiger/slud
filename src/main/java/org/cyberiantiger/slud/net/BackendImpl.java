package org.cyberiantiger.slud.net;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static java.nio.channels.SelectionKey.*;

public class BackendImpl extends Thread implements Backend {
    private static final Logger log = LoggerFactory.getLogger(Backend.class);
    private final Object tasksLock = new Object();
    private final PriorityQueue<Task> tasks = new PriorityQueue<>();
    private final Selector sel;
    private final AtomicLong sequenceId = new AtomicLong();

    @Inject
    public BackendImpl(Selector sel) {
        log.info("Creating new BackendImpl {}" + hashCode());
        this.sel = sel;
    }

    public static long milliTime() {
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
    }

    public void addTask(Runnable r) {
        addTask(r, Long.MIN_VALUE);
    }

    public void addTask(Runnable r, long when) {
        synchronized(tasksLock) {
            tasks.add(new Task(r, when, sequenceId.incrementAndGet()));
            // Kick the selector if the task was added externally.
            if (this != Thread.currentThread()) {
                sel.wakeup();
            }
        }
    }

    /**
     * Process tasks on the task queue, and return wait until next invocation.
     * @return Time in millis to pause before next processTasks() calls, 0 to wait forever.
     */
    private long processTasks() {
        long now = milliTime();
        while (true) {
            Task task;
            synchronized (tasksLock) {
                task = tasks.peek();
                if (task == null) {
                    return 0;
                } else {
                    if (task.getWhen() <= now) {
                        tasks.remove();
                    } else {
                        return task.getWhen() - now;
                    }
                }
            }
            task.getTask().run();
        }
    }

    @Override
    public void run() {
        try {
            long wait = 0;
            while (sel.isOpen()) {
                // Update interest ops of all keys.
                for (SelectionKey key : sel.keys()) {
                    if (key.isValid()) {
                        key.interestOps(((SocketChannelHandler) key.attachment()).interestOps());
                    }
                }
                // Do select()
                if (wait >= 0) {
                    sel.select(wait);
                } else {
                    sel.selectNow();
                }
                // Process selected ops.
                for (SelectionKey key : sel.selectedKeys()) {
                    int ops = key.readyOps();
                    SocketChannelHandler handler = (SocketChannelHandler) key.attachment();
                    int interestOps = handler.interestOps();
                    try {
                        if ((ops & interestOps & OP_ACCEPT) != 0) {
                            handler.handleAccept();
                        }
                        if ((ops & interestOps & OP_CONNECT) != 0) {
                            handler.handleConnect();
                        }
                        if ((ops & interestOps & OP_READ) != 0) {
                            handler.handleRead();
                        }
                        if (((ops | interestOps) & OP_WRITE)!= 0) {
                            handler.handleWrite();
                        }
                    } catch (IOException | RuntimeException ex) {
                        log.error("Closing connection", ex);
                        handler.getChannel().close();
                    }
                }
                wait = processTasks();
            }
        } catch (IOException ex) {
            log.error("Closing Backend", ex);
        }
    }

    public void register(SocketChannelHandler channelHandler) {
        try {
            channelHandler.getChannel().register(sel, channelHandler.interestOps(), channelHandler);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public void close() {
        try {
            sel.close();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @AllArgsConstructor
    private static class Task implements Comparable<Task> {
        @Getter
        @NonNull
        private Runnable task;
        @Getter
        private long when;
        @Getter
        private long sequence;


        @Override
        public int compareTo(Task o) {
            int result = Long.compare(getWhen(), o.getWhen());
            if (result != 0) {
                return result;
            }
            return Long.compare(getSequence(), o.getSequence());
        }
    }

}
