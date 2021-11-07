package com.sample.aris.distributedlock.service;

import com.sample.aris.distributedlock.exception.TaskSchedulerException;
import com.sample.aris.distributedlock.jpa.ScheduleTask;
import com.sample.aris.distributedlock.model.ScheduleTaskStatus;
import com.sample.aris.distributedlock.model.SchedulerTaskExecutor;
import com.sample.aris.distributedlock.port.ScheduleTaskPort;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;

/**
 * @author Aristotle Miranda
 * */

@Service
public class SchedulerTaskService {
    static Map<String, TaskScheduled> runnableTaskScheduledMap = new HashMap<>();
    Logger logger = LogManager.getLogger(SchedulerTaskService.class);
    @Autowired
    TaskScheduler taskScheduler;

    @Autowired
    ScheduleTaskPort scheduleTaskPort;

    private final LockRegistry lockRegistry;

    public SchedulerTaskService(LockRegistry lockRegistry) {
        this.lockRegistry = lockRegistry;
    }


    public void schedule(CronTrigger cronTrigger, ScheduleTask scheduleTask)  {

        Optional.ofNullable(runnableTaskScheduledMap.get(scheduleTask.getId()))
                .ifPresent(taskScheduled -> logger.info("Task is already scheduled/rescheduled in memory"));

       if(runnableTaskScheduledMap.get(scheduleTask.getId()) == null) {
           logger.info("Task ID: {} already in-memory to scheduled/rescheduled", scheduleTask.getId());
        }else {
           ScheduleTask task = scheduleTaskPort.getScheduleTaskById(scheduleTask.getId());

           if(task == null) {
               scheduleTaskPort.saveTask(scheduleTask);
           }

           ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(prepareRunnableTask(scheduleTask), cronTrigger);

           assert task != null;
           runnableTaskScheduledMap.put(task.getId(), new TaskScheduled(scheduledFuture, task));
        }
    }

    public void schedule(String triggerExpression, ScheduleTask scheduleTask) throws ParseException {
        Date triggerDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(triggerExpression);

        if(null != runnableTaskScheduledMap.get(scheduleTask.getId()) &&
            !ScheduleTaskStatus.RESCHEDULED.equals(scheduleTask.getStatus())) {
            //TODO remove logging for below line as it will flood the log spool
            logger.info("Task ID: {} already in-memory to scheduled/rescheduled", scheduleTask.getId());
        }else {
            ScheduleTask task = scheduleTaskPort.getScheduleTaskById(scheduleTask.getId());

            if(task == null || ScheduleTaskStatus.RESCHEDULED.equals(scheduleTask.getStatus())) {
                scheduleTaskPort.saveTask(scheduleTask);
            }

            ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(prepareRunnableTask(scheduleTask), triggerDate);
            runnableTaskScheduledMap.put(scheduleTask.getId(), new TaskScheduled(scheduledFuture, scheduleTask));
        }
    }


    public void cancelTask(String id) {
        System.out.println("Cancelling tasks......");
        Optional.ofNullable(runnableTaskScheduledMap.get(id)).ifPresent(taskScheduled -> taskScheduled.getScheduledFuture().cancel(true));
    }


    public void rescheduleTask(String triggerExpression, ScheduleTask task) {

        Optional.ofNullable(runnableTaskScheduledMap.get(task.getId())).ifPresent(taskScheduled -> {
            logger.info("RESCHEDULING TO NEW TIMING..... ID: {}", task.getId());
            //taskScheduled.getScheduledFuture().cancel(true);
            taskScheduled.getScheduledFuture().cancel(true);
            try {
                schedule(triggerExpression, task);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }


    private boolean isTaskTriggerAble(ScheduleTaskStatus scheduleTaskStatus) {
        return ScheduleTaskStatus.SCHEDULED.equals(scheduleTaskStatus) ||
                ScheduleTaskStatus.RESCHEDULED.equals(scheduleTaskStatus);
    }

    @Scheduled(fixedRate = 60000)
    void taskPoller() {
        AtomicReference<List<ScheduleTask>> scheduledTasks = new AtomicReference<>(scheduleTaskPort.getScheduledTasks());

      //  Map<String, ScheduleTask> mapScheduleTask = scheduledTasks.stream().collect(Collectors.toMap(ScheduleTask::getId, Function.identity()));

        scheduledTasks.get().forEach(scheduleTask -> {
            if(Optional.ofNullable(runnableTaskScheduledMap.get(scheduleTask.getId())).isEmpty()
                && isTaskTriggerAble(scheduleTask.getStatus())) {
                try {
                    this.schedule(scheduleTask.getTriggeredTime(), scheduleTask);
                } catch (ParseException e) {
                    logger.error("Unable to parse triggeredTime into date. Trying cron expression...");
                    //TODO set cron expression here
                    this.schedule(new CronTrigger(""), scheduleTask);
                }
            }
        });

        runnableTaskScheduledMap.forEach((k, v) -> logger.info("REPORT TASK ID: {}, DONE: {}, CANCELLED: {}",
                k, v.getScheduledFuture().isDone(), v.getScheduledFuture().isCancelled()));
    }

    @SuppressWarnings("rawtypes")
    static abstract class RunnableWithNotification implements RunnableFuture {

        private final SchedulerTaskExecutor schedulerTaskExecutor;
        private final LockRegistry lockRegistry;

        public RunnableWithNotification(SchedulerTaskExecutor schedulerTaskExecutor, LockRegistry lockRegistry) {
            this.schedulerTaskExecutor = schedulerTaskExecutor;
            this.lockRegistry = lockRegistry;
        }

        @SneakyThrows
        @Override
        public final void run() {

            try {
                String key = schedulerTaskExecutor.getScheduleTask().getId();
                Lock lock = lockRegistry.obtain(key);
                boolean lockAcquired = lock.tryLock(5, TimeUnit.SECONDS);
                if (lockAcquired) {
                    try {
                        System.out.println("lockAcquired = " + true);
                        beforeTriggerTask(schedulerTaskExecutor);
                        triggerTask();
                        afterTriggerTask(schedulerTaskExecutor);
                        Thread.sleep(30000);
                    }
                    finally {
                        lock.unlock();
                    }
                }else {
                    System.out.println("lockAcquired = " + false);
                }
            }catch (TaskSchedulerException taskSchedulerException) {
                onError(taskSchedulerException, schedulerTaskExecutor);
            }
        }

        public abstract void triggerTask();

        public abstract void beforeTriggerTask(SchedulerTaskExecutor schedulerTaskExecutor);

        public abstract void afterTriggerTask(SchedulerTaskExecutor schedulerTaskExecutor);

        public abstract void onError(Throwable t, SchedulerTaskExecutor schedulerTaskExecutor);

    }

    static public class TaskScheduled {
        ScheduledFuture<?> scheduledFuture;
        ScheduleTask task;

        public TaskScheduled(ScheduledFuture<?> scheduledFuture, ScheduleTask task) {
            this.scheduledFuture = scheduledFuture;
            this.task = task;
        }

        public ScheduledFuture<?> getScheduledFuture() {
            return scheduledFuture;
        }
    }

    private RunnableWithNotification prepareRunnableTask(ScheduleTask scheduleTask) {
        return new RunnableWithNotification
                     (new SchedulerTaskExecutor(scheduleTaskPort, scheduleTask), lockRegistry) {
                 @Override
                 public boolean cancel(boolean mayInterruptIfRunning) {
                     if (mayInterruptIfRunning) {
                         System.out.println("Cancelling this scheduled task");
                     }
                     return true;
                 }

                 @Override
                 public boolean isCancelled() {
                     return false;
                 }

                 @Override
                 public boolean isDone() {
                     return false;
                 }

                 @Override
                 public Object get() {
                     return null;
                 }

                 @Override
                 public Object get(long timeout, TimeUnit unit) {
                     return null;
                 }

                 @Override
                 public void triggerTask() {
                     //TODO send message to solace
                     logger.info("TRIGGERING TASK ID:{} PAYLOAD:{}", scheduleTask.getId(), scheduleTask.getPayload());
                 }

                 @Override
                 public void beforeTriggerTask(SchedulerTaskExecutor schedulerTaskExecutor) {
                     logger.info("Triggering task ID: {}", schedulerTaskExecutor.getScheduleTask().getId());
                     if(schedulerTaskExecutor.getScheduleTask().getId().equals("4")) {
                         throw new TaskSchedulerException("Force eviction.....");
                     }
                     schedulerTaskExecutor.getScheduleTask().setStatus(ScheduleTaskStatus.TRIGGERING);
                     schedulerTaskExecutor.getScheduleTaskPort().saveTask(schedulerTaskExecutor.getScheduleTask());
                 }

                 @Override
                 public void afterTriggerTask(SchedulerTaskExecutor schedulerTaskExecutor) {
                     schedulerTaskExecutor.getScheduleTask().setStatus(ScheduleTaskStatus.TRIGGERED);
                     schedulerTaskExecutor.getScheduleTaskPort().saveTask(schedulerTaskExecutor.getScheduleTask());
                 }

                 @Override
                 public void onError(Throwable t, SchedulerTaskExecutor schedulerTaskExecutor) {
                     logger.error("Error while triggering task ID:{}", schedulerTaskExecutor.getScheduleTask().getId());
                     schedulerTaskExecutor.getScheduleTask().setStatus(ScheduleTaskStatus.EXIT_WITH_ERROR);
                     schedulerTaskExecutor.getScheduleTaskPort().saveTask(schedulerTaskExecutor.getScheduleTask());
                 }
             };
    }

}
