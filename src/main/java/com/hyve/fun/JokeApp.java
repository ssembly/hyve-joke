package com.hyve.fun;

import com.hyve.fun.config.ApplicationProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import tech.jhipster.config.DefaultProfileUtil;

@SpringBootApplication
@EnableConfigurationProperties({ LiquibaseProperties.class, ApplicationProperties.class })
@EnableScheduling
public class JokeApp {

    private static final Logger log = LoggerFactory.getLogger(JokeApp.class);

    private final Environment env;

    public JokeApp(Environment env) {
        this.env = env;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(JokeApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
        app.run(args).getEnvironment();
    }
}
