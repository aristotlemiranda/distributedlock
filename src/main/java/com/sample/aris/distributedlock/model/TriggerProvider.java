package com.sample.aris.distributedlock.model;

import java.util.function.BiConsumer;

public enum TriggerProvider {

    SPECIFIC_DATETIME_TRIGGER((configuredScheduledTask, object) -> {
        configuredScheduledTask.setTriggerProcedure(object);
    }),
    CRON_TRIGGER((configuredScheduledTask, object) -> {
        configuredScheduledTask.setTriggerProcedure(object);
    });

    private BiConsumer<ConfiguredScheduledTask, Object> triggerProcedure;

    TriggerProvider(BiConsumer<ConfiguredScheduledTask, Object> triggerProcedure)  {
        this.triggerProcedure = triggerProcedure;
    }

    public BiConsumer<ConfiguredScheduledTask, Object> getTriggerProcedure() {
        return triggerProcedure;
    }

}
