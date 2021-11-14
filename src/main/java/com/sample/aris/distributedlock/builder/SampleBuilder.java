package com.sample.aris.distributedlock.builder;

public class SampleBuilder {

    
    public static void main(String[] args){
        ScheduleTaskId s = new ScheduleTaskId("2", "_OPS");
        TaskId x = buildTaskId(s);
        System.out.println("s = " + x.taskIds());

        String[] taskIds = {"2", "3", "4"};

        CancelTaskId cancelTaskId = new CancelTaskId(taskIds);

        System.out.println(cancelTaskId.taskIds());

         TaskId t = buildTaskId(cancelTaskId);
        System.out.println("taskIds = " + t.taskIds());

    }
    
    public static <T extends TaskId> T buildTaskId(T t) {
        return t;   
    }


    public static ScheduleTaskBuilder createScheduleTask() {
        return new ScheduleTaskBuilder();
    }



}