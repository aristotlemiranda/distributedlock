package com.sample.aris.distributedlock.builder;

public class CancelTaskBuilder extends BaseTaskConfigurer implements TaskBuilder<ConfiguredTaskPayload, CancelTaskId> {
    private String payload;
    private String endpoint;
    private TaskId taskId;
    static CancelTaskBuilder _INSTANCE;

    public CancelTaskBuilder() {
        _INSTANCE = this;
    }

    public static CancelTaskBuilder getInstance() {
        return _INSTANCE;
    }

    public CancelTaskBuilder schedule() {
       return  _INSTANCE;
    }


    @Override
     public ConfiguredTaskPayload build() {
        System.out.println("Building now....");
        ConfiguredTaskPayload configuredTaskPayload = new ConfiguredTaskPayload();
        configuredTaskPayload.setPayload(payload);
        configuredTaskPayload.setEndpoint(endpoint);
        configuredTaskPayload.setTaskId(taskId);
        return configuredTaskPayload;
    }

    @Override
    public CancelTaskBuilder constructTaskId(TaskId id) {
        taskId = buildTaskId(id);
        return _INSTANCE;
    }
}
