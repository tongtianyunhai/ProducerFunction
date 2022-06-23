package com.BrainFlux.AutoInput.config;

import com.BrainFlux.AutoInput.domain.ScheduleTask;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;

/**
 * @Author：geliyang
 * @Version：1.0
 * @Date：2022/6/21-13:07
 * @Since:jdk1.8
 * @Description:TODO
 */
@Component
@Configuration
@EnableScheduling
public class ScheduleTaskConfig {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(ScheduleTaskConfig.class);
    public void taskOne(){
        LOGGER.info("999666");
    }
}
