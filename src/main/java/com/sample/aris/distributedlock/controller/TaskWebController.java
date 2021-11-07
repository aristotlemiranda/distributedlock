package com.sample.aris.distributedlock.controller;

import com.sample.aris.distributedlock.jpa.ScheduleTask;
import com.sample.aris.distributedlock.port.ScheduleTaskPort;
import com.sample.aris.distributedlock.service.SchedulerTaskService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @author Aristotle Miranda
 * */

@RestController
@RequestMapping("/api/scheduler")
public class TaskWebController {

    @Autowired
    private LockRegistry registry;

    private ScheduleTaskPort scheduleTaskPort;

    @Autowired
    SchedulerTaskService schedulerTaskService;

    @PostMapping("/schedule")
    public void schedule(@RequestParam("triggerTime") String triggerTime, @RequestBody ScheduleTask task) throws Exception {
        schedulerTaskService.schedule(triggerTime, task);
    }

    @PostMapping("/reschedule")
    public void reschedule(@RequestParam("triggerTime") String triggerTime, @RequestBody ScheduleTask task) throws Exception {
        schedulerTaskService.rescheduleTask(triggerTime, task);
    }

    @PostMapping("/cancel")
    public void test(@RequestParam("id") String id)  {
        schedulerTaskService.cancelTask(id);
    }


    @SneakyThrows
    @GetMapping("/update/{id}")
    void update(@PathVariable Integer id) {

        String key = Integer.toString(id);
        Lock lock = registry.obtain(key);
        boolean lockAcquired = lock.tryLock(5, TimeUnit.SECONDS);
        if (lockAcquired) {
            try {
                System.out.println("lockAcquired = " + true);
                Thread.sleep(30000);
            }
            finally {
                lock.unlock();
            }
        }else {
            System.out.println("lockAcquired = " + false);
        }

    }

}
