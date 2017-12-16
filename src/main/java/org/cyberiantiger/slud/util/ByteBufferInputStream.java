package org.cyberiantiger.slud.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteBufferInputStream extends InputStream {
    private ByteBuffer buffer;

    public ByteBufferInputStream(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public int read() {
        if (buffer.hasRemaining()) {
            return buffer.get();
        } else {
            return -1;
        }
    }

    @Override
    public int read(byte[] b) {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) {
        int read = Math.min(len, buffer.remaining());
        if (read == 0) {
            return -1;
        }
        buffer.get(b, off, read);
        return read;
    }

    @Override
    public long skip(long n) throws IOException {
        throw new IOException("unsupported");
    }

    @Override
    public int available() {
        return buffer.remaining();
    }

    @Override
    public void close() {
    }
}
