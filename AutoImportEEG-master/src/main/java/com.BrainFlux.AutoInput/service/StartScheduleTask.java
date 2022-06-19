package com.BrainFlux.AutoInput.service;

import com.BrainFlux.AutoInput.domain.ScheduleTask;
import com.BrainFlux.AutoInput.domain.Vo.DirVo;
import com.BrainFlux.AutoInput.domain.Vo.ScheduleTaskVo;
import com.BrainFlux.AutoInput.domain.criteria.ScheduleTaskQueryCriteria;
import com.BrainFlux.AutoInput.mapper.ScheduleTaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Author：geliyang
 * @Version：1.0
 * @Date：2022/6/14-14:09
 * @Since:jdk1.8
 * @Description:TODO
 */
@Service
public class StartScheduleTask implements SchedulingConfigurer {
    @Resource
    private ScheduleTaskMapper scheduleTaskMapper;
    @Autowired
    private ProducerCSVServiceScheduleTask producerCSVServiceScheduleTask;

    private String dirPath="";
    private String tmpTid="";
    private Date tmpLocalDate;
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(
                () -> {
                    System.out.println("start dynamic scheduleTask"+LocalDateTime.now().toLocalTime());
                    try {
                        producerCSVServiceScheduleTask.importCSVEvent(dirPath,tmpTid);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ScheduleTaskQueryCriteria scheduleTaskQueryCriteria=new ScheduleTaskQueryCriteria();
                    scheduleTaskQueryCriteria.setTid(tmpTid);
                    scheduleTaskQueryCriteria.setLatestExecute(tmpLocalDate);
                    scheduleTaskMapper.updateLatestExecuteByEid(scheduleTaskQueryCriteria);
                },
                triggerContext -> {
                    ScheduleTaskQueryCriteria scheduleTaskQueryCriteria=new ScheduleTaskQueryCriteria();
                    scheduleTaskQueryCriteria.setTaskType("1");
                    List<ScheduleTaskVo>list= scheduleTaskMapper.selectAllActivedScheduleTaskByQuery(scheduleTaskQueryCriteria);
                    String cron="11 11 11 31 2 ?";
                    for (ScheduleTaskVo tmpVo: list){
                        System.out.println("tid"+tmpVo.getTid());
                        System.out.println("startTime"+tmpVo.getStartTime());
                        System.out.println("expireTIme"+tmpVo.getExpireTime());
                        System.out.println("executeTime"+tmpVo.getExecuteTime());
                        System.out.println("task_path"+tmpVo.getTaskPath());
                        System.out.println("task_type"+tmpVo.getTaskType());
                        String tmpHour=tmpVo.getExecuteTime().split(":")[0];
                        String tmpMiniute=tmpVo.getExecuteTime().split(":")[1];
                        String tmpSecond=tmpVo.getExecuteTime().split(":")[2];
                        System.out.println(tmpHour);
                        System.out.println(tmpMiniute);
                        System.out.println(tmpSecond);
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
                            if(tmpStartDate.before(localDate)&&tmpexpireDate.after(localDate)&&executeOrNot<=Long.parseLong(tmpVo.getExecuteInterval())){
                                System.out.println(stringBuffer.toString());
                                cron=stringBuffer.toString();
                                tmpTid=tmpVo.getTid();
                                dirPath=tmpVo.getTaskPath();
                                tmpLocalDate=localDate;
                            }else {
                                cron="11 11 11 31 2 ?";
                            }
                            System.out.println(LocalDateTime.now()+" localDateTime");
                            System.out.println("tmpStartDate"+tmpStartDate+ " localDate"+localDate);
                            System.out.println("timeDiffer"+ (localDate.getTime()-tmpLatestExecute.getTime())/(1000*3600*24));
                            System.out.println(tmpStartDate.before(localDate)&&tmpexpireDate.after(localDate));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(cron);
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );
    }


}
