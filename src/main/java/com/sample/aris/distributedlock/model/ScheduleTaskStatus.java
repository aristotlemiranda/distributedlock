package com.sample.aris.distributedlock.model;

public enum ScheduleTaskStatus {
    TRIGGERING,
    SCHEDULED,
    RESCHEDULED,
    TRIGGERED,
    CANCELLED,
    EXIT_WITH_ERROR
}