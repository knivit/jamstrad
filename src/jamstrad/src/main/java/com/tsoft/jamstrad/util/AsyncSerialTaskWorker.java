package com.tsoft.jamstrad.util;

import java.util.LinkedList;
import java.util.Queue;

public class AsyncSerialTaskWorker<T extends AsyncTask> extends Thread {

    private boolean stop;
    private Queue<T> taskQueue;

    public AsyncSerialTaskWorker(String threadName) {
        super(threadName);
        this.setDaemon(true);
        this.taskQueue = new LinkedList();
    }

    public final synchronized void run() {
        System.out.println("Thread '" + this.getName() + "' started");

        while(!this.isStopped()) {
            T task = null;
            synchronized(this.getTaskQueue()) {
                task = this.getTaskQueue().peek();
            }

            boolean shouldWait = task == null;
            if (task != null) {
                this.processTask(task);
                synchronized(this.getTaskQueue()) {
                    this.getTaskQueue().remove(task);
                    shouldWait = this.getTaskQueue().isEmpty();
                }
            }

            if (shouldWait) {
                try {
                    this.wait();
                } catch (InterruptedException var4) {
                }
            }
        }

        System.out.println("Thread '" + this.getName() + "' stopped");
    }

    public final void addTask(T task) {
        boolean shouldNotify = false;
        synchronized(this.getTaskQueue()) {
            boolean wasEmpty = this.getTaskQueue().isEmpty();
            this.addTaskToQueue(task, this.getTaskQueue());
            shouldNotify = wasEmpty && !this.getTaskQueue().isEmpty();
        }

        if (shouldNotify) {
            synchronized(this) {
                this.notify();
            }
        }

    }

    protected void addTaskToQueue(T task, Queue<T> queue) {
        queue.add(task);
    }

    protected void processTask(T task) {
        task.process();
    }

    public void stopProcessing() {
        this.stop = true;
    }

    public boolean isStopped() {
        return this.stop;
    }

    private Queue<T> getTaskQueue() {
        return this.taskQueue;
    }
}
