package com.BrainFlux.AutoInput.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author whl
 * @since 2022-05-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ScheduleTask implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId
    private String tid;

    private Date executeTime;

    private String taskPath;

    private Date expireTime;

    private Date startTime;

    private Date latestExecute;

    private String executeInterval;

    private String taskType;

    private String taskStatus;

    private String additionOne;

    private String additionTwo;
}
