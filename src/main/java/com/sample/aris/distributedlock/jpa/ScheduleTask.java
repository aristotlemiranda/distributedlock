package com.sample.aris.distributedlock.jpa;

import com.sample.aris.distributedlock.model.ScheduleTaskStatus;

import javax.persistence.*;

@Entity
@Table(name = "schedule_task")
public class ScheduleTask {

    @Id
    private String id;

    private String payload;

    @Enumerated(EnumType.STRING)
    private ScheduleTaskStatus status;

    private String triggeredTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public ScheduleTaskStatus getStatus() {
        return status;
    }

    public void setStatus(ScheduleTaskStatus status) {
        this.status = status;
    }

    public String getTriggeredTime() {
        return triggeredTime;
    }

    public void setTriggeredTime(String triggeredTime) {
        this.triggeredTime = triggeredTime;
    }
}