package com.sample.aris.distributedlock.model;

public abstract class ConfiguredScheduledTask<T> {
    private TriggerStrategy triggerStrategy;
    private Object payload;
    private TriggerProvider triggerProvider;
    private Object triggerProcedure;

    public TriggerStrategy getTriggerStrategy() {
        return this.triggerStrategy;
    }

    public void setTriggerStrategy(TriggerStrategy triggerStrategy) {
        this.triggerStrategy = triggerStrategy;
    }

    public Object getPayload() {
        return this.payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public TriggerProvider getTriggerProvider() {
        return triggerProvider;
    }

    public void setTriggerProvider(TriggerProvider triggerProvider) {
        this.triggerProvider = triggerProvider;
    }

    public Object getTriggerProcedure() {
        return triggerProcedure;
    }

    public void setTriggerProcedure(Object triggerProcedure) {
        this.triggerProcedure = triggerProcedure;
    }
}


