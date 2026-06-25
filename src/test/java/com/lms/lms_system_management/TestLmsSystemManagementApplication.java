package com.lms.lms_system_management;

import org.springframework.boot.SpringApplication;

public class TestLmsSystemManagementApplication {

    public static void main(String[] args) {
        SpringApplication.from(SystemManagementApplication::main)
                .with(TestcontainersConfiguration.class).run(args);
    }

}
