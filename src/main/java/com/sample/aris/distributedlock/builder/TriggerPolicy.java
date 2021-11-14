package com.sample.aris.distributedlock.builder;

public class TriggerPolicy {

    private TriggerMode triggerMode;
    private TriggerBy triggerBy;
    private Object setOff;

    public TriggerPolicy(TriggerMode triggerMode, TriggerBy triggerBy, Object setOff) {
        this.triggerMode = triggerMode;
        this.triggerBy = triggerBy;
        this.setOff = setOff;
    }
    public enum TriggerBy {
        DATE_TIME_TRIGGER,
        CRON_TRIGGER
    }


    protected TriggerMode getTriggerMode() {
        return triggerMode;
    }

    protected TriggerBy getTriggerBy() {
        return triggerBy;
    }

    protected Object getSetOff() {
        return setOff;
    }
}
