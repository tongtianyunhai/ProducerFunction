package com.BrainFlux.AutoInput.service;


import com.BrainFlux.AutoInput.common.page.PageResult;
import com.BrainFlux.AutoInput.domain.ScheduleTask;
import com.BrainFlux.AutoInput.domain.Vo.ScheduleTaskVo;
import com.BrainFlux.AutoInput.domain.criteria.ScheduleTaskQueryCriteria;
import com.BrainFlux.AutoInput.service.Base.BaseService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author whl
 * @since 2022-05-31
 */
public interface IScheduleTaskService extends BaseService<ScheduleTask> {
    boolean addNewScheduleTask(ScheduleTask scheduleTask);
    PageResult<ScheduleTaskVo> checkAllScheduleTaskByType(ScheduleTaskQueryCriteria scheduleTaskQueryCriteria);
    PageResult<ScheduleTaskVo> checkAllScheduleTask(ScheduleTaskQueryCriteria scheduleTaskQueryCriteria);
    PageResult<ScheduleTaskVo> checkAllScheduleTaskByTime(ScheduleTaskQueryCriteria scheduleTaskQueryCriteria);
    PageResult<ScheduleTaskVo> checkAllScheduleTaskByQuery(ScheduleTaskQueryCriteria scheduleTaskQueryCriteria);
    PageResult<ScheduleTaskVo> checkAllActivedScheduleTaskByQuery(ScheduleTaskQueryCriteria scheduleTaskQueryCriteria);
    boolean editScheduleTaskStatusByEid(ScheduleTaskQueryCriteria scheduleTaskQueryCriteria);
    boolean updateLatestExecuteByEid(ScheduleTaskQueryCriteria scheduleTaskQueryCriteria);

}
