package com.sample.aris.distributedlock.builder;

public class ScheduleTaskBuilder extends BaseTaskConfigurer implements TaskBuilder<ConfiguredTaskPayload, ScheduleTaskId> {
    private String payload;
    private String endpoint;
    private TaskId taskId;
    private TriggerPolicy triggerPolicy;
    static ScheduleTaskBuilder _INSTANCE;

    public ScheduleTaskBuilder() {
        _INSTANCE = this;
    }

    public ScheduleTaskBuilder taskPayload(String payload) {
        this.payload = payload;
        return  _INSTANCE;
    }

    public ScheduleTaskBuilder endpoint(String endpoint) {
        this.endpoint = endpoint;
        return  _INSTANCE;
    }

    public ScheduleTaskBuilder triggerProvider(TriggerPolicy triggerPolicy) {
        this.triggerPolicy = triggerPolicy;
        return _INSTANCE;
    }
    @Override
    public ConfiguredTaskPayload build() {
        System.out.println("Building now....");
       ConfiguredTaskPayload configuredTaskPayload = new ConfiguredTaskPayload();
       configuredTaskPayload.setPayload(payload);
       configuredTaskPayload.setEndpoint(endpoint);
       configuredTaskPayload.setTaskId(taskId);
       configuredTaskPayload.setMode(triggerPolicy.getTriggerMode());
       configuredTaskPayload.setTriggerBy(triggerPolicy.getTriggerBy());
       configuredTaskPayload.setSetOff(triggerPolicy.getSetOff());
        return configuredTaskPayload;
    }


    @Override
    public ScheduleTaskBuilder constructTaskId(TaskId id) {
        taskId = buildTaskId(id);
        return _INSTANCE;
    }
}
