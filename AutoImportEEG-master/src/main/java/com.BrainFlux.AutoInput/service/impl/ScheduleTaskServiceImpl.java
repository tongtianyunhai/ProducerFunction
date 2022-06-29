package com.BrainFlux.AutoInput.service.impl;
import com.BrainFlux.AutoInput.domain.Vo.ScheduleTaskVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.BrainFlux.AutoInput.common.page.PageResult;
import com.BrainFlux.AutoInput.domain.ScheduleTask;
import com.BrainFlux.AutoInput.domain.criteria.ScheduleTaskQueryCriteria;
import com.BrainFlux.AutoInput.mapper.ScheduleTaskMapper;
import com.BrainFlux.AutoInput.service.Base.impl.BaseServiceImpl;
import com.BrainFlux.AutoInput.service.IScheduleTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author whl
 * @since 2022-05-31
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleTaskServiceImpl extends BaseServiceImpl<ScheduleTask> implements IScheduleTaskService {
    @Resource
    private ScheduleTaskMapper scheduleTaskMapper;

    @Override
    public boolean addNewScheduleTask(ScheduleTask scheduleTask) {
        int res= scheduleTaskMapper.addNewScheduleTask(scheduleTask);
        boolean res2;
        if(res==1){
            res2=true;
        }else {
            res2=false;
        }
        return res2;
    }

    @Override
    public PageResult<ScheduleTaskVo> checkAllScheduleTaskByType(ScheduleTaskQueryCriteria scheduleTaskQueryCriteria) {
        Integer currentPage = scheduleTaskQueryCriteria.getCurrentPage();
        Integer pageSize = scheduleTaskQueryCriteria.getPageSize();

        if (StringUtils.isEmpty(currentPage)) {
            currentPage = 1;
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = 5;
        }

        PageHelper.startPage(currentPage, pageSize);



        List<ScheduleTaskVo> scheduleTaskList = scheduleTaskMapper.selectScheduleTaskByType(scheduleTaskQueryCriteria);

        PageInfo<ScheduleTaskVo> scheduleTaskPageInfo = new PageInfo<>(scheduleTaskList);

        long total = scheduleTaskPageInfo.getTotal();

        //brandList ------>brandVoList



        return new PageResult<ScheduleTaskVo>(total, scheduleTaskList);
    }

    @Override
    public PageResult<ScheduleTaskVo> checkAllScheduleTask(ScheduleTaskQueryCriteria scheduleTaskQueryCriteria) {
        Integer currentPage = scheduleTaskQueryCriteria.getCurrentPage();
        Integer pageSize = scheduleTaskQueryCriteria.getPageSize();

        if (StringUtils.isEmpty(currentPage)) {
            currentPage = 1;
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = 5;
        }

        PageHelper.startPage(currentPage, pageSize);

        List<ScheduleTaskVo> scheduleTaskList = scheduleTaskMapper.selectAllScheduleTask();

        PageInfo<ScheduleTaskVo> scheduleTaskPageInfo = new PageInfo<>(scheduleTaskList);

        long total = scheduleTaskPageInfo.getTotal();

        return new PageResult<ScheduleTaskVo>(total, scheduleTaskList);
    }

    @Override
    public PageResult<ScheduleTaskVo> checkAllScheduleTaskByTime(ScheduleTaskQueryCriteria scheduleTaskQueryCriteria) {
        Integer currentPage = scheduleTaskQueryCriteria.getCurrentPage();
        Integer pageSize = scheduleTaskQueryCriteria.getPageSize();

        if (StringUtils.isEmpty(currentPage)) {
            currentPage = 1;
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = 5;
        }

        PageHelper.startPage(currentPage, pageSize);

        List<ScheduleTaskVo> scheduleTaskList = scheduleTaskMapper.selectAllScheduleTaskByTime(scheduleTaskQueryCriteria);

        PageInfo<ScheduleTaskVo> scheduleTaskPageInfo = new PageInfo<>(scheduleTaskList);

        long total = scheduleTaskPageInfo.getTotal();

        return new PageResult<ScheduleTaskVo>(total, scheduleTaskList);      }

    @Override
    public PageResult<ScheduleTaskVo> checkAllScheduleTaskByQuery(ScheduleTaskQueryCriteria scheduleTaskQueryCriteria) {
        Integer currentPage = scheduleTaskQueryCriteria.getCurrentPage();
        Integer pageSize = scheduleTaskQueryCriteria.getPageSize();

        if (StringUtils.isEmpty(currentPage)) {
            currentPage = 1;
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = 5;
        }

        PageHelper.startPage(currentPage, pageSize);

        List<ScheduleTaskVo> scheduleTaskList = scheduleTaskMapper.selectAllScheduleTaskByQuery(scheduleTaskQueryCriteria);

        PageInfo<ScheduleTaskVo> scheduleTaskPageInfo = new PageInfo<>(scheduleTaskList);

        long total = scheduleTaskPageInfo.getTotal();

        return new PageResult<ScheduleTaskVo>(total, scheduleTaskList);
    }

    @Override
    public PageResult<ScheduleTaskVo> checkAllActivedScheduleTaskByQuery(ScheduleTaskQueryCriteria scheduleTaskQueryCriteria) {
        Integer currentPage = scheduleTaskQueryCriteria.getCurrentPage();
        Integer pageSize = scheduleTaskQueryCriteria.getPageSize();

        if (StringUtils.isEmpty(currentPage)) {
            currentPage = 1;
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = 5;
        }

        PageHelper.startPage(currentPage, pageSize);

        List<ScheduleTaskVo> scheduleTaskList = scheduleTaskMapper.selectAllActivedScheduleTaskByQuery(scheduleTaskQueryCriteria);

        PageInfo<ScheduleTaskVo> scheduleTaskPageInfo = new PageInfo<>(scheduleTaskList);

        long total = scheduleTaskPageInfo.getTotal();

        return new PageResult<ScheduleTaskVo>(total, scheduleTaskList);
    }

    @Override
    public boolean editScheduleTaskStatusByEid(ScheduleTaskQueryCriteria scheduleTaskQueryCriteria) {
        return scheduleTaskMapper.editScheduleStatusByEid(scheduleTaskQueryCriteria);
    }

    @Override
    public boolean updateLatestExecuteByEid(ScheduleTaskQueryCriteria scheduleTaskQueryCriteria) {
        return scheduleTaskMapper.updateLatestExecuteByEid(scheduleTaskQueryCriteria);
    }
}
