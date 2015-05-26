package com.sunny.threadpool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DDAIThreadPool implements IDThreadPool {
    /**
     * Tyrant
     */
    TyrantExecutor tyrantExecuter;
    /**
     * Coward
     */
    CowardExecutor cowardExecuter;

    CommandFactory tyrantCF;
    CommandFactory cowardCF;

    PriorityBlockingQueue<Runnable> tyrantQ;
    PriorityBlockingQueue<Runnable> cowradQ;

    ConcurrentHashMap<String, IPriorityTask> taskManager = new ConcurrentHashMap<String, IPriorityTask>();

    /**
     * Lock used for all public operations
     */
    private final ReentrantLock lock;

    public static IDThreadPool newThreadPool(int cowardSize, int tyrantSize,
                                             int despoticLimit, boolean sortByLatest) {
        return new DDAIThreadPool(cowardSize, tyrantSize, despoticLimit, 0,
                sortByLatest);
    }

    private DDAIThreadPool(int cowardSize, int tyrantSize, int despoticLimit,
                           long keepAliveTime, boolean sortByLatest) {
        ReentrantLock cowardPauseLock = new ReentrantLock();
        Condition unpaused = cowardPauseLock.newCondition();
        tyrantQ = new PriorityBlockingQueue<Runnable>();
        cowradQ = new PriorityBlockingQueue<Runnable>();
        PriorityThreadFactory threadFactory = new PriorityThreadFactory(
                "thread-pool", 10);
        tyrantExecuter = new TyrantExecutor(tyrantSize, despoticLimit, tyrantQ,
                threadFactory, cowardPauseLock, unpaused);
        cowardExecuter = new CowardExecutor(cowardSize,
                tyrantExecuter.getChain(), cowradQ, threadFactory,
                cowardPauseLock, unpaused);
        tyrantCF = new CommandFactory(true);
        cowardCF = new CommandFactory(false);
        lock = new ReentrantLock();
    }

    ITaskHandler th = new ITaskHandler() {

        @Override
        public void onFinish(String flag) {
            lock.lock();
            try {
                if (taskManager.containsKey(flag)) {
                    taskManager.remove(flag);
                }
            } finally {
                lock.unlock();
            }
        }
    };

    private void execute(String category, IPriorityTask runnable,
                         TaskPriority priority) {
        if (runnable != null) {
            if (priority.ordinal() > TaskPriority.BACK_MAX.ordinal()) {
                tyrantExecuter.execute(tyrantCF.getTask(category, runnable,
                        priority.ordinal(), th));
            } else {
                cowardExecuter.execute(cowardCF.getTask(category, runnable,
                        priority.ordinal(), th));
            }
        }
    }

    @Override
    public void shutdownNow() {
        tyrantExecuter.shutdownNow();
        cowardExecuter.shutdownNow();
    }

    // public void put(String category, TaskPriority runnable) {
    // put(category, runnable, IPriorityTask.PRIORITY_MEDIUM);
    //
    // }

    public void put(String category, IPriorityTask runnable,
                    TaskPriority priority) throws NullPointerException {
        if (runnable == null) {
            throw new NullPointerException();
        }
        String key = runnable.getFlag();
        lock.lock();
        try {
            if (taskManager.containsKey(key)) {
                if (!taskManager.get(key).onRepeatPut(runnable)) {
                    runnable.isolateFlag();
                    taskManager.put(runnable.getFlag(), runnable);
                    execute(category, runnable, priority);
                }
            } else {
                taskManager.put(runnable.getFlag(), runnable);
                execute(category, runnable, priority);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void cancelQueueByCategory(String category) {
        ArrayList<Runnable> keys = new ArrayList<Runnable>();
        ArrayList<Runnable> buffer = new ArrayList<Runnable>();
        lock.lock();
        try {
            tyrantQ.drainTo(keys);
            for (Runnable cmd : keys) {
                if (((PriorityTask) cmd).category.equals(category)) {
                    buffer.add(cmd);
                }
            }
            keys.removeAll(buffer);
            tyrantQ.addAll(keys);
            keys.clear();
            cowradQ.drainTo(keys);
            for (Runnable cmd : keys) {
                if (((PriorityTask) cmd).category.equals(category)) {
                    buffer.add(cmd);
                }
            }
            keys.removeAll(buffer);
            cowradQ.addAll(keys);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void cancelQueueByTaskID(int taskId) {
        lock.lock();
        try {
            Collection<IPriorityTask> ipts = taskManager.values();
            ArrayList<IPriorityTask> needClean = new ArrayList<IPriorityTask>();
            for (IPriorityTask task : ipts) {
                if (task.unregisterListener(taskId)) {
                    needClean.add(task);
                }
            }
            // clean in Queue.
            if (needClean.size() > 0) {
                ArrayList<Runnable> keys = new ArrayList<Runnable>();
                ArrayList<Runnable> buffer = new ArrayList<Runnable>();
                tyrantQ.drainTo(keys);
                PriorityTask pt;
                for (Runnable cmd : keys) {
                    pt = (PriorityTask) cmd;
                    if (needClean.contains(pt.runnable)) {
                        buffer.add(cmd);
                    }
                }
                keys.removeAll(buffer);
                tyrantQ.addAll(keys);
            }
        } finally {
            lock.unlock();
        }
    }

    private final class CowardExecutor extends ThreadPoolExecutor {
        private boolean isPaused;
        private ReentrantLock pauseLock;
        private Condition unpaused;
        private Chain chain;

        public CowardExecutor(int poolSize, Chain _chain,
                              BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
                              ReentrantLock _pauseLock, Condition _unpaused) {
            super(poolSize, poolSize, 0, TimeUnit.MILLISECONDS, workQueue,
                    threadFactory);
            this.pauseLock = _pauseLock;
            this.unpaused = _unpaused;
            this.chain = _chain;
        }

        protected void beforeExecute(Thread t, Runnable r) {
            super.beforeExecute(t, r);
            pauseLock.lock();
            try {
                isPaused = !chain.allowBreath();
                while (isPaused) {
                    unpaused.await();
                    isPaused = !chain.allowBreath();
                }
            } catch (InterruptedException ie) {
                t.interrupt();
            } finally {
                pauseLock.unlock();
            }
        }
    }

    private final class TyrantExecutor extends ThreadPoolExecutor {
        private ReentrantLock pauseLock;
        private Condition unpaused;
        private volatile Chain chain;
        private volatile int despoticLimit;

        public TyrantExecutor(int poolSize, int _despoticLimit,
                              BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
                              ReentrantLock _pauseLock, Condition _unpaused) {
            super(poolSize, poolSize, 0, TimeUnit.MILLISECONDS, workQueue,
                    threadFactory);
            this.despoticLimit = _despoticLimit;
            this.pauseLock = _pauseLock;
            this.unpaused = _unpaused;
            chain = new Chain() {

                @Override
                public boolean allowBreath() {
                    return getActiveCount() < despoticLimit;
                }
            };
        }

        public Chain getChain() {
            return chain;
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            super.afterExecute(r, t);
            pauseLock.lock();
            try {
                if (chain.allowBreath()) {
                    unpaused.signalAll();
                }
            } finally {
                pauseLock.unlock();
            }
        }

    }

    private interface Chain {
        boolean allowBreath();
    }

    @Override
    public int getTaskCount() {
        return tyrantExecuter.getActiveCount() + cowardExecuter.getActiveCount();
    }
}
