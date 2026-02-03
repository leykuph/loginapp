package com.loginapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Spring Boot application.
 * 
 * @SpringBootApplication does three things:
 * 1. @Configuration - marks this as a configuration class
 * 2. @EnableAutoConfiguration - tells Spring Boot to auto-configure based on dependencies
 * 3. @ComponentScan - scans for @Component, @Service, @Repository, @Controller in this package
 */
@SpringBootApplication
public class LoginAppApplication {

    public static void main(String[] args) {
        // Starts the embedded Tomcat server and Spring context
        SpringApplication.run(LoginAppApplication.class, args);
    }
}
