package com.sample.aris.distributedlock.configuration;


import com.sample.aris.distributedlock.model.ConfiguredScheduledTask;
import com.sample.aris.distributedlock.model.TriggerProvider;
import com.sample.aris.distributedlock.model.TriggerStrategy;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.Assert;
import java.util.Date;

public class ScheduledTaskBuilder<T> {
    private static ConfiguredScheduledTask configuredScheduledTask;
    private static ScheduledTaskBuilder _INSTANCE = new ScheduledTaskBuilder();

    public ScheduledTaskBuilder triggerStrategy(TriggerStrategy triggerStrategy) {
        configuredScheduledTask.setTriggerStrategy(triggerStrategy);
        return _INSTANCE;
    }

    public ScheduledTaskBuilder createTaskPayload(Object payload) {
        configuredScheduledTask.setPayload(payload);
        return _INSTANCE;
    }

    public static ScheduledTaskBuilder create(ConfiguredScheduledTask configuredScheduledTask) {

      synchronized (_INSTANCE) {
          setConfiguredScheduledTask(configuredScheduledTask);
          _INSTANCE = new ScheduledTaskBuilder();
      }
        return _INSTANCE;
    }

    public ScheduledTaskBuilder triggerProcedure(TriggerProvider triggerProvider, Object object) {
        configuredScheduledTask.setTriggerProvider(triggerProvider);

        if(TriggerProvider.SPECIFIC_DATETIME_TRIGGER.equals(triggerProvider)) {
            Assert.isAssignable(object.getClass(), Date.class, "Trigger procedure mismatch, a Date must be given.");
        }else {
            Assert.isAssignable(object.getClass(), CronTrigger.class, "Trigger procedure mismatch, CronTrigger must be given.");
        }
        configuredScheduledTask.getTriggerProvider().getTriggerProcedure().accept(configuredScheduledTask, object);
        return _INSTANCE;
    }

    public static ConfiguredScheduledTask build() {
        Assert.notNull(configuredScheduledTask.getPayload(), "Payload cannot be null or empty.");
        Assert.notNull(configuredScheduledTask.getTriggerStrategy(), "Trigger Strategy must be set.");
        Assert.notNull(configuredScheduledTask.getTriggerProvider(), "Trigger provider and procedure must be set.");
        Assert.notNull(configuredScheduledTask.getTriggerProcedure(), "Trigger provider and procedure must be set.");
        return configuredScheduledTask;
    }

    public static void setConfiguredScheduledTask(ConfiguredScheduledTask configuredScheduledTask) {
        ScheduledTaskBuilder.configuredScheduledTask = configuredScheduledTask;
    }
}
