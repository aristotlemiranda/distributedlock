package com.sample.aris.distributedlock.builder;

public interface TaskId<T> {
    default T taskIds() {
        return null;
    }
}
