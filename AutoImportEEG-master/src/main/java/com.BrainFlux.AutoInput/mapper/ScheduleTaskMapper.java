package com.BrainFlux.AutoInput.mapper;


import com.BrainFlux.AutoInput.domain.ScheduleTask;
import com.BrainFlux.AutoInput.domain.Vo.ScheduleTaskVo;
import com.BrainFlux.AutoInput.domain.criteria.ScheduleTaskQueryCriteria;
import com.BrainFlux.AutoInput.mapper.base.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.*;
/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author whl
 * @since 2022-05-31
 */
@Repository
public interface ScheduleTaskMapper extends MyMapper<ScheduleTask> {
    int addNewScheduleTask(ScheduleTask scheduleTask);
    List<ScheduleTaskVo>selectScheduleTaskByType(ScheduleTaskQueryCriteria scheduleTaskQueryCriteria);
    List<ScheduleTaskVo>selectAllScheduleTask();
    List<ScheduleTaskVo>selectAllScheduleTaskByTime(ScheduleTaskQueryCriteria scheduleTaskQueryCriteria);
    List<ScheduleTaskVo>selectAllScheduleTaskByQuery(ScheduleTaskQueryCriteria scheduleTaskQueryCriteria);
    List<ScheduleTaskVo>selectAllActivedScheduleTaskByQuery(ScheduleTaskQueryCriteria scheduleTaskQueryCriteria);
    boolean editScheduleStatusByEid(ScheduleTaskQueryCriteria scheduleTaskQueryCriteria);
    boolean updateLatestExecuteByEid(ScheduleTaskQueryCriteria scheduleTaskQueryCriteria);
}
