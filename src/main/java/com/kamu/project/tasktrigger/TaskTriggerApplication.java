package com.kamu.project.tasktrigger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.kamu.project.tasktrigger")
public class TaskTriggerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskTriggerApplication.class, args);
    }

}
