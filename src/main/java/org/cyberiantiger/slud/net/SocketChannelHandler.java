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

    public SocketChannelHandler(int readBufferSize, int writeBufferSize) throws IOException {
        this.channel = SocketChannel.open();
        this.channel.configureBlocking(false);
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

    public void handleConnect() throws IOException {
        channel.finishConnect();
    }

    public void handleRead() throws IOException {
        readBuffer.compact();
        channel.read(readBuffer);
        readBuffer.flip();
    }

    public void handleWrite() throws IOException {
        writeBuffer.flip();
        channel.write(writeBuffer);
        writeBuffer.compact();
    }

    public void handleAccept() throws IOException {
    }

    public void handleClose() throws IOException {
    }
}
