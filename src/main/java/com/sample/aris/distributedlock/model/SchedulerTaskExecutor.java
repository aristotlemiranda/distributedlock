package com.sample.aris.distributedlock.model;

import com.sample.aris.distributedlock.jpa.ScheduleTask;
import com.sample.aris.distributedlock.port.ScheduleTaskPort;


public class SchedulerTaskExecutor {

    private ScheduleTask scheduleTask;
    private ScheduleTaskPort scheduleTaskPort;


    public SchedulerTaskExecutor(ScheduleTaskPort scheduleTaskPort, ScheduleTask scheduleTask) {
        this.scheduleTaskPort = scheduleTaskPort;
        this.scheduleTask = scheduleTask;

    }

    public ScheduleTask getScheduleTask() {
        return scheduleTask;
    }

    public void setScheduleTask(ScheduleTask scheduleTask) {
        this.scheduleTask = scheduleTask;
    }

    public ScheduleTaskPort getScheduleTaskPort() {
        return scheduleTaskPort;
    }

    public void setScheduleTaskPort(ScheduleTaskPort scheduleTaskPort) {
        this.scheduleTaskPort = scheduleTaskPort;
    }
}
