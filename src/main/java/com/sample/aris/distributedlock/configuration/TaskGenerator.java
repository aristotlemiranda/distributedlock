package com.sample.aris.distributedlock.configuration;

import com.google.gson.Gson;
import com.sample.aris.distributedlock.builder.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public abstract class TaskGenerator {
    static Gson gson = new Gson();
    public static ScheduleTaskBuilder scheduleTaskBuilder(){
        return new ScheduleTaskBuilder();
    }

    public static CancelTaskBuilder cancelTaskBuilder(){
        return new CancelTaskBuilder();
    }

    public static void main(String[] args) {
        ConfiguredTaskPayload configuredTaskPayload = TaskGenerator
                .scheduleTaskBuilder().constructTaskId(new ScheduleTaskId("1", "_SOD"))
                .endpoint("BATCH_FUND_ALLO")
                .endpoint("again")
                .triggerProvider(new TriggerPolicy(TriggerMode.RESCHEDULE,
                        TriggerPolicy.TriggerBy.DATE_TIME_TRIGGER, Calendar.getInstance().getTime()))
                .taskPayload("THIS IS A SAMPLE ONLY").build();
        String sendForScheduling = gson.toJson(configuredTaskPayload);

        System.out.println("configuredTaskPayload = " + sendForScheduling);
        List<String> cancelIds = new ArrayList<>();
        cancelIds.add("1_PRESOD");
        cancelIds.add("2_SOD");
        ConfiguredTaskPayload configuredTaskPayload1 = TaskGenerator.cancelTaskBuilder().constructTaskId(new CancelTaskId(cancelIds.stream().toArray(String[]::new))).build();
        System.out.println("gson.toJson(configuredTaskPayload1) = " + gson.toJson(configuredTaskPayload1));
    }
}