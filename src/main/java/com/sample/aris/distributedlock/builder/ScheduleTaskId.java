package com.sample.aris.distributedlock.builder;

import java.util.Arrays;

public class ScheduleTaskId implements TaskId<String> {

    private String id;

    public ScheduleTaskId(String firstID, String... succeeding) {
        id = firstID;
        Arrays.stream(succeeding).forEach(s -> {
           id = id.concat(s);
        });
    }

    @Override
    public String taskIds() {
        return id;
    }
}
