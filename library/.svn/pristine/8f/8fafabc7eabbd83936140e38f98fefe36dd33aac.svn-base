package com.sunny.threadpool;

public interface IDThreadPool {

    // void put(String category, IPriorityTask runnable);

    void put(String category, IPriorityTask runnable, TaskPriority priority);

    void shutdownNow();

    void cancelQueueByCategory(String tag);

    void cancelQueueByTaskID(int taskId);

    int getTaskCount();
}
