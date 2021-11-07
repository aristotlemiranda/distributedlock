package com.sample.aris.distributedlock.jpa;


import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleTaskDAO extends JpaRepository<ScheduleTask, String> {

}
