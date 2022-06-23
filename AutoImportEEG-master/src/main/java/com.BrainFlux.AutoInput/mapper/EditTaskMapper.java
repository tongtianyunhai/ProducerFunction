package com.BrainFlux.AutoInput.mapper;

import com.BrainFlux.AutoInput.domain.EditTask;
import com.BrainFlux.AutoInput.domain.Vo.ScheduleTaskVo;
import com.BrainFlux.AutoInput.domain.criteria.ScheduleTaskQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author whl
 * @since 2022-06-20
 */
@Repository
public interface EditTaskMapper extends BaseMapper<EditTask> {
    List<EditTask> selectAllTaskType();
    boolean deleteTaskTypeByTid(EditTask editTask);
    boolean addScheduleTask(EditTask editTask);
}
