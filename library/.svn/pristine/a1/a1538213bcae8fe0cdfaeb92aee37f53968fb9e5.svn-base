package com.sunny.threadpool;

public interface IPriorityTask {

    void run();

    /**
     * 标记是Task种类。
     *
     * @return
     */
    String getFlag();

    /**
     * @param newTask
     * @return true if return. This event has been completed.
     */
    boolean onRepeatPut(IPriorityTask newTask);

    void isolateFlag();

    /**
     * unregister a listener.
     *
     * @param taskId
     * @return true if return, No one cares about this task.
     */
    boolean unregisterListener(int taskId);

}
