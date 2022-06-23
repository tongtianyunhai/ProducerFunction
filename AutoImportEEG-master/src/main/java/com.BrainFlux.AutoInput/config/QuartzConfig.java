package com.BrainFlux.AutoInput.config;


import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @Author：geliyang
 * @Version：1.0
 * @Date：2022/6/21-12:55
 * @Since:jdk1.8
 * @Description:TODO
 */
@Configuration
public class QuartzConfig {

    @Bean(name="jobDetailOne")
    public MethodInvokingJobDetailFactoryBean detailFactoryBean(ScheduleTaskConfig task){
        MethodInvokingJobDetailFactoryBean jobDetail=new MethodInvokingJobDetailFactoryBean();
        jobDetail.setConcurrent(false);
        jobDetail.setName("test");
        jobDetail.setGroup("test-groupOne");
        jobDetail.setTargetObject(task);
        jobDetail.setTargetMethod("taskOne");
        return jobDetail;
    }
    @Bean(name="jobDetailTwo")
    JobDetailFactoryBean jobDetailFactoryBean(){
        JobDetailFactoryBean bean=new JobDetailFactoryBean();
        bean.setJobClass(ScheduleTask2Config.class);
        JobDataMap map=new JobDataMap();
        map.put("message","hello 888999");
        bean.setJobDataMap(map);
        return bean;
    };

    @Bean(name ="jobTrigger")
    public CronTriggerFactoryBean cronJobTrigger(){
        CronTriggerFactoryBean trigger=new CronTriggerFactoryBean();
        trigger.setJobDetail(jobDetailFactoryBean().getObject());
        trigger.setCronExpression("0 30 20 * * ?");
        trigger.setName("test");;
        return trigger;
    }
    @Bean(name ="scheduler")
    public SchedulerFactoryBean schedulerFactory(){
        SchedulerFactoryBean bean= new SchedulerFactoryBean();
        bean.setOverwriteExistingJobs(true);
        bean.setStartupDelay(1);
        bean.setTriggers(cronJobTrigger().getObject());
        return bean;
    }

}
