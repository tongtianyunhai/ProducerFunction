package com.BrainFlux.AutoInput.domain.criteria;

import com.BrainFlux.AutoInput.domain.criteria.base.BaseQueryCriteria;
import lombok.Data;

import java.util.Date;

/**
 * @Author：geliyang
 * @Version：1.0
 * @Date：2022/6/12-22:39
 * @Since:jdk1.8
 * @Description:TODO
 */
@Data
public class ScheduleTaskQueryCriteria extends BaseQueryCriteria {
  private String taskType;
  private String tid;
  private String taskStatus;
  private Date executeTime;
  private Date startTime;
  private Date expireTime;
  private String executeInterval;
  private Date latestExecute;
}
