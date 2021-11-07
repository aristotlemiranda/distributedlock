package com.sample.aris.distributedlock.port;

import com.sample.aris.distributedlock.jpa.ScheduleTask;

import java.util.List;

public interface ScheduleTaskPort {
    ScheduleTask getScheduleTaskById(String id);
    ScheduleTask saveTask(ScheduleTask scheduleTask);
    List<ScheduleTask> getScheduledTasks();
}
