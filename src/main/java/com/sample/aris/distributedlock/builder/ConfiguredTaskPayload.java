package com.sample.aris.distributedlock.builder;

public class ConfiguredTaskPayload {
    private String payload;
    private String endpoint;
    private TaskId taskId;
    private TriggerMode mode;
    private TriggerPolicy.TriggerBy triggerBy;
    private Object setOff;

    public String getPayload() {
        return payload;
    }

    protected void setPayload(String payload) {
        this.payload = payload;
    }

    public String getEndpoint() {
        return endpoint;
    }

    protected void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    protected void setTaskId(TaskId taskId) {
        this.taskId = taskId;
    }

    public TriggerMode getMode() {
        return mode;
    }

    protected void setMode(TriggerMode mode) {
        this.mode = mode;
    }

    public Object getSetOff() {
        return setOff;
    }

    protected void setSetOff(Object setOff) {
        this.setOff = setOff;
    }

    public TriggerPolicy.TriggerBy getTriggerBy() {
        return triggerBy;
    }

    protected void setTriggerBy(TriggerPolicy.TriggerBy triggerBy) {
        this.triggerBy = triggerBy;
    }
}
