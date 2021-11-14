package com.sample.aris.distributedlock.builder;

public abstract class BaseTaskConfigurer  {

    public abstract TaskBuilder constructTaskId(TaskId taskId);

    protected  <S extends TaskId> S buildTaskId(S s) {
        return s;
    }
}
