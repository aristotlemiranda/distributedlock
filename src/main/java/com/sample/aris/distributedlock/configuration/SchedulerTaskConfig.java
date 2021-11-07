package com.sample.aris.distributedlock.configuration;

import com.sample.aris.distributedlock.model.ConfiguredScheduledTask;
import com.sample.aris.distributedlock.model.ScheduledTask;
import com.sample.aris.distributedlock.model.TriggerProvider;
import com.sample.aris.distributedlock.model.TriggerStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.jdbc.lock.DefaultLockRepository;
import org.springframework.integration.jdbc.lock.JdbcLockRegistry;
import org.springframework.integration.jdbc.lock.LockRepository;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import javax.sql.DataSource;


@Configuration
@EnableScheduling
public class SchedulerTaskConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(taskScheduler());
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(3);
        scheduler.setThreadNamePrefix("ThreadScheduler-");
        scheduler.initialize();
        return scheduler;
    }

    public static void main(String[] args) {
        ConfiguredScheduledTask scheduledTaskBuilder = ScheduledTaskBuilder.create(new ScheduledTask())
                .createTaskPayload("SAMPLE PAYLOAD")
                .triggerStrategy(TriggerStrategy.SCHEDULE)
                .triggerProcedure(TriggerProvider.CRON_TRIGGER, new CronTrigger("* * * * 1 7"))
                .build();

//        ConfiguredScheduledTask rescheduledTaskBuilder = ScheduledTaskBuilder.create(new ReScheduledTask())
//                .payload("RESCHEDULED PAYLOAD")
//                .triggerStrategy(TriggerStrategy.RESCHEDULE)
//                .build();

        System.out.println("scheduledTaskBuilder = " + scheduledTaskBuilder);
      //  System.out.println("scheduledTaskBuilder = " + rescheduledTaskBuilder);
    }


    @Bean
    DefaultLockRepository defaultLockRepository(DataSource dataSource) {
        DefaultLockRepository repository = new DefaultLockRepository(dataSource);
        repository.setTimeToLive(60 * 1000);
        return repository;
    }

    @Bean
    JdbcLockRegistry jdbcLockRegistry(LockRepository repository) {
        return new JdbcLockRegistry(repository);
    }

}