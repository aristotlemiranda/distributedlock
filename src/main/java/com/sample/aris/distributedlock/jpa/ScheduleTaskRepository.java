package com.sample.aris.distributedlock.jpa;

import com.sample.aris.distributedlock.port.ScheduleTaskPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ScheduleTaskRepository implements ScheduleTaskPort {

    @Autowired
    ScheduleTaskDAO scheduleTaskDAO;

    public ScheduleTask getScheduleTaskById(String id) {

       Optional<ScheduleTask> scheduleTask = scheduleTaskDAO.findById(id);

       if(scheduleTask.isEmpty())
           return null;

       return scheduleTask.get();
    }

    public ScheduleTask saveTask(ScheduleTask scheduleTask) {
        return scheduleTaskDAO.save(scheduleTask);
    }

    public List<ScheduleTask> getScheduledTasks() {
        return scheduleTaskDAO.findAll();
    }
}
