package com.BrainFlux.AutoInput.service.impl;

import com.BrainFlux.AutoInput.domain.EditTask;
import com.BrainFlux.AutoInput.mapper.EditTaskMapper;
import com.BrainFlux.AutoInput.mapper.ScheduleTaskMapper;
import com.BrainFlux.AutoInput.service.Base.impl.BaseServiceImpl;
import com.BrainFlux.AutoInput.service.IEditTaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author whl
 * @since 2022-06-20
 */
@Service
public class EditTaskServiceImpl extends BaseServiceImpl<EditTask> implements IEditTaskService {
    @Resource
    private EditTaskMapper editTaskMapper;

    @Override
    public List<EditTask> selectTaskType() {
        return editTaskMapper.selectAllTaskType();
    }

    @Override
    public boolean deleteTaskTypeById(EditTask editTask) {
        return editTaskMapper.deleteTaskTypeByTid(editTask);
    }

    @Override
    public boolean addScheduleTask(EditTask editTask) {
        return editTaskMapper.addScheduleTask(editTask);
    }
}
