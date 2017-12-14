package org.cyberiantiger.slud.net;

import lombok.Getter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static java.nio.channels.SelectionKey.OP_CONNECT;
import static java.nio.channels.SelectionKey.OP_READ;
import static java.nio.channels.SelectionKey.OP_WRITE;

public class SocketChannelHandler {
    @Getter
    private final SocketChannel channel;
    @Getter
    private final ByteBuffer readBuffer;
    @Getter
    private final ByteBuffer writeBuffer;

    public SocketChannelHandler(SocketChannel channel, int readBufferSize, int writeBufferSize) {
        if (channel.isBlocking()) {
            throw new IllegalArgumentException("Expected non blocking channel");
        }
        this.channel = channel;
        readBuffer = ByteBuffer.allocate(readBufferSize);
        readBuffer.limit(0);
        writeBuffer = ByteBuffer.allocate(writeBufferSize);
    }

    public int interestOps() {
        if (channel.isConnected()) {
            int ops = 0;
            if (readBuffer.limit() < readBuffer.array().length) {
                ops |= OP_READ;
            }
            if (writeBuffer.position() > 0) {
                ops |= OP_WRITE;
            }
            return ops;
        } else if (channel.isOpen()) {
            return OP_CONNECT;
        } else {
            return 0;
        }
    }

    public boolean handleConnect() throws IOException {
        return channel.finishConnect();
    }

    public boolean handleRead() throws IOException {
        readBuffer.compact();
        int readBytes = channel.read(readBuffer);
        readBuffer.flip();
        return readBytes >= 0;
    }

    public boolean handleWrite() throws IOException {
        writeBuffer.flip();
        channel.write(writeBuffer);
        writeBuffer.compact();
        return true;
    }

    public boolean handleAccept() throws IOException {
        return true;
    }

    public void handleClose() throws IOException {
        channel.close();
    }
}
