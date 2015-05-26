package com.sunny.threadpool;

public class CommandFactory {

    boolean sortByLatest = false;

    public CommandFactory(boolean sortByLatest) {
        this.sortByLatest = sortByLatest;
    }

    public CommandFactory() {
        this.sortByLatest = false;
    }

    public PriorityTask getTask(String category, IPriorityTask runnable,
                                int priority, ITaskHandler handler) {
        PriorityTask task = new PriorityTask(category, sortByLatest, priority,
                runnable, handler);
        return task;
    }

    public PriorityTask getTask(String category, IPriorityTask runnable,
                                ITaskHandler handler) {
        PriorityTask task = new PriorityTask(category, sortByLatest, runnable,
                handler);
        return task;
    }
}
