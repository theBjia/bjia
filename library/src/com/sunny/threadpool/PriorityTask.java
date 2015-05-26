package com.sunny.threadpool;

public class PriorityTask extends AbstractCommand implements Runnable {

    public IPriorityTask runnable;
    private ITaskHandler handler;
    public String category;

    public PriorityTask(String category, boolean _commandSortByLatest,
                        IPriorityTask runnable, ITaskHandler handler) {
        super(_commandSortByLatest);
        this.category = category;
        this.handler = handler;
        this.runnable = runnable;
    }

    public PriorityTask(String category, boolean _commandSortByLatest,
                        int priority, IPriorityTask runnable, ITaskHandler handler) {
        super(_commandSortByLatest, priority);
        this.category = category;
        this.handler = handler;
        this.runnable = runnable;
    }

    @Override
    public void run() {
        if (runnable != null) {
            runnable.run();
        }
        if (handler != null) {
            handler.onFinish(runnable.getFlag());
        }
    }
}
