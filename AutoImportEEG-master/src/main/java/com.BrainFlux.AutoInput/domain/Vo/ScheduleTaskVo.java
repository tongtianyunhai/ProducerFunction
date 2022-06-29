package com.BrainFlux.AutoInput.domain.Vo;

import lombok.Data;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Author：geliyang
 * @Version：1.0
 * @Date：2022/6/11-20:44
 * @Since:jdk1.8
 * @Description:TODO
 */
@Data
public class ScheduleTaskVo {
    private static final long serialVersionUID = 1L;

    private String tid;

    private String expireTime;

    private String startTime;

    private String taskType;

    private String taskPath;

    private String executeTime;

    private String taskStatus;

    private String executeInterval;

    private String latestExecute;

}
