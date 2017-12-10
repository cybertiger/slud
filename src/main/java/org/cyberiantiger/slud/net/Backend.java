package org.cyberiantiger.slud.net;

public interface Backend {
    void stop();
    void start();
    void close();
    void addTask(Runnable task);
    void addTask(Runnable task, long when);
    void register(SocketChannelHandler handler);
}
