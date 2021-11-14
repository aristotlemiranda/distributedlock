# distributedlock
# DB SETUP

-- db_name.init_lock definition

CREATE TABLE `INT_LOCK` (
  `LOCK_KEY` char(36) NOT NULL,
  `REGION` varchar(100) NOT NULL,
  `CLIENT_ID` char(36) DEFAULT NULL,
  `CREATED_DATE` timestamp NOT NULL,
  PRIMARY KEY (`LOCK_KEY`,`REGION`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4


-- db_name.schedule_task definition

CREATE TABLE `schedule_task` (
  `id` varchar(36) NOT NULL,
  `payload` varchar(100) DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  `triggered_time` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
