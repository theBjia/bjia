package com.sunny.threadpool;

public enum TaskPriority {

    BACK_MIN, BACK_NORM, BACK_MAX, UI_MIN, UI_NORM, UI_MAX;

    public static TaskPriority valueOf(int value) {
        switch (value) {
            case 0:
                return TaskPriority.BACK_MIN;
            case 1:
                return TaskPriority.BACK_NORM;
            case 2:
                return TaskPriority.BACK_MAX;
            case 3:
                return TaskPriority.UI_MIN;
            case 4:
                return TaskPriority.UI_NORM;
            case 5:
                return TaskPriority.UI_MAX;
        }
        return UI_MIN;
    }
}
