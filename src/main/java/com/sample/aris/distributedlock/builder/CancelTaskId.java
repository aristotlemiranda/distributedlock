package com.sample.aris.distributedlock.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CancelTaskId implements TaskId<List<String>> {
    List<String> ids = new ArrayList<>();


    public CancelTaskId(String... succeeding) {

        Arrays.stream(succeeding).forEach(s -> {
            ids.add(s);
        });
    }

    @Override
    public List<String> taskIds() {
        return ids;
    }
}
