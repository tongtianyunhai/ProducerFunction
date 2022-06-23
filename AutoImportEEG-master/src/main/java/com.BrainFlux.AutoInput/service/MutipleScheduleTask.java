package com.BrainFlux.AutoInput.service;

import com.BrainFlux.AutoInput.config.ScheduleTask2Config;
import com.BrainFlux.AutoInput.config.ScheduleTaskConfig;
import com.BrainFlux.AutoInput.domain.ScheduleTask;
import com.BrainFlux.AutoInput.domain.Vo.ScheduleTaskVo;
import com.BrainFlux.AutoInput.domain.criteria.ScheduleTaskQueryCriteria;
import com.BrainFlux.AutoInput.mapper.ScheduleTaskMapper;
import com.google.common.util.concurrent.*;
import org.quartz.*;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.config.FixedRateTask;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Author：geliyang
 * @Version：1.0
 * @Date：2022/6/21-13:15
 * @Since:jdk1.8
 * @Description:TODO
 */
@Configuration
@EnableScheduling
@Component
public class MutipleScheduleTask {
//  @Resource(name="jobDetailTwo")
//    private JobDetail jobDetail;
//  @Resource(name="jobTrigger")
//    private CronTrigger cronTrigger;
//  @Resource(name="scheduler")
//    private Scheduler scheduler;

    @Resource
    private ScheduleTaskMapper scheduleTaskMapper;
    @Autowired
    private ProducerCSVServiceScheduleTask producerCSVServiceScheduleTask;
    @Resource
    private ScheduleTaskConfig scheduleTaskConfig;

    private String dirPath="";
    private String tmpTid="";
    private Date tmpLocalDate;
  @Scheduled(fixedRate = 60*60*1000)
    public void scheduleUpdateCronTrigger() throws SchedulerException{
      ScheduleTaskQueryCriteria scheduleTaskQueryCriteria=new ScheduleTaskQueryCriteria();
      scheduleTaskQueryCriteria.setTaskType("csvToInfluxDB(pitt+pitt)");
      List<ScheduleTaskVo> list= scheduleTaskMapper.selectAllActivedScheduleTaskByQuery(scheduleTaskQueryCriteria);
      String cron="11 11 11 31 2 ?";
      for (ScheduleTaskVo tmpVo: list){
          String tmpHour=tmpVo.getExecuteTime().split(":")[0];
          String tmpMiniute=tmpVo.getExecuteTime().split(":")[1];
          String tmpSecond=tmpVo.getExecuteTime().split(":")[2];
          StringBuffer stringBuffer=new StringBuffer();
          stringBuffer.append(tmpSecond);
          stringBuffer.append(" ");
          stringBuffer.append(tmpMiniute);
          stringBuffer.append(" ");
          stringBuffer.append(tmpHour);
          stringBuffer.append(" ");
          stringBuffer.append("* * ?");
          SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
          try {
              Date tmpStartDate=simpleDateFormat.parse(tmpVo.getStartTime());
              Date tmpexpireDate=simpleDateFormat.parse(tmpVo.getExpireTime());
              Date localDate=simpleDateFormat.parse(String.valueOf(LocalDateTime.now()));
              Date tmpLatestExecute=simpleDateFormat.parse(tmpVo.getLatestExecute());
              long executeOrNot=(localDate.getTime()-tmpLatestExecute.getTime())/(1000*3600*24);
              if(tmpStartDate.before(localDate)&&tmpexpireDate.after(localDate)&&executeOrNot>=Long.parseLong(tmpVo.getExecuteInterval())){
                  cron=stringBuffer.toString();
                  tmpTid=tmpVo.getTid();
                  dirPath=tmpVo.getTaskPath();
                  tmpLocalDate=localDate;
                  JobDetail jobDetail=JobBuilder.newJob(ScheduleTask2Config.class)
                          .withIdentity(tmpVo.getTid(),tmpVo.getTaskType())
                          .usingJobData("taskPath",dirPath).build();
                  Trigger trigger= TriggerBuilder.newTrigger()
                          .withIdentity(tmpVo.getTid(),tmpVo.getTaskType())
                          .withDescription("start a new scheduleTask, tid is: "+tmpTid)
                          .usingJobData("taskId",tmpTid).withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
                  Scheduler scheduler= StdSchedulerFactory.getDefaultScheduler();
                  if(!scheduler.checkExists(jobDetail.getKey())){
                      scheduler.scheduleJob(jobDetail,trigger);
                      System.out.println(((CronTrigger) trigger).getCronExpression()+"cronNow");
                      scheduler.start();
                  }
              }else {
                  cron="11 11 11 31 2 ?";
              }
          } catch (ParseException e) {
              e.printStackTrace();
          }
      }
  }

}
