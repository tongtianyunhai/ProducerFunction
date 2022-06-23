package com.BrainFlux.AutoInput.service;

import com.BrainFlux.AutoInput.domain.EditTask;
import com.BrainFlux.AutoInput.service.Base.BaseService;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author whl
 * @since 2022-06-20
 */
public interface IEditTaskService extends BaseService<EditTask> {
    List<EditTask> selectTaskType();
    boolean deleteTaskTypeById(EditTask editTask);
    boolean addScheduleTask(EditTask editTask);
}
