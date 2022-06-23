package com.BrainFlux.AutoInput.controller;
import com.BrainFlux.AutoInput.common.http.AxiosResult;
import com.BrainFlux.AutoInput.common.page.PageResult;
import com.BrainFlux.AutoInput.controller.base.BaseController;
import com.BrainFlux.AutoInput.domain.EditTask;
import com.BrainFlux.AutoInput.domain.ScheduleTask;
import com.BrainFlux.AutoInput.domain.Vo.DirVo;
import com.BrainFlux.AutoInput.domain.Vo.ScheduleTaskVo;
import com.BrainFlux.AutoInput.domain.Vo.ServerInfoVo;
import com.BrainFlux.AutoInput.domain.criteria.ScheduleTaskQueryCriteria;
import com.BrainFlux.AutoInput.service.*;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/kafkaProducer_api")
@Api(value="kafkaProducerController",description = "kafkaProducerController")
public class KafkaProducerController extends BaseController {

    @Autowired
    private ProducerCSVService producerCSVService;
    @Autowired
    private IScheduleTaskService iScheduleTaskService;
    @Autowired
    private ProducerCSVServiceScheduleTask producerCSVServiceScheduleTask;
//    @Autowired
//    private StartScheduleTask startScheduleTask;
    @Autowired
    private IEditTaskService iEditTaskService;
    // url for import new csv
    @PostMapping("importCSV")
    public AxiosResult<Boolean> importCSVEvent(@RequestBody DirVo dirVo) throws IOException, InterruptedException {
        Map<String, Object> map = new HashMap<>();
        System.out.println(dirVo.getDir());
        producerCSVService.importCSVEvent(dirVo.getDir());
        return toAxiosResult(true);
    }
    @PostMapping("importCSVScheduleTask")
    public AxiosResult<Boolean> importCSVEventScheduleTask(@RequestBody DirVo dirVo) throws IOException, InterruptedException {
        Map<String, Object> map = new HashMap<>();
        System.out.println(dirVo.getDir());
        String uuid="scheduleTaskTest";
        producerCSVServiceScheduleTask.importCSVEvent(dirVo.getDir(),uuid);
        return toAxiosResult(true);
    }
    @GetMapping("/status")
    // url for import new csv
    public AxiosResult<ServerInfoVo> status() throws IOException, InterruptedException {
        ServerInfoVo serverInfoVo=new ServerInfoVo();
        serverInfoVo.setLocation("pittsburgh");
        serverInfoVo.setServer("csvToInfluxDB");
        serverInfoVo.setStatus("running");
        serverInfoVo.setType("producer");
        return AxiosResult.success(serverInfoVo);
    }

    @PostMapping("importCSVTimer")
    public AxiosResult<Boolean> importCSVEventTimer(@RequestBody DirVo dirVo) throws IOException, InterruptedException, ParseException {
        Map<String, Object> map = new HashMap<>();
        System.out.println(dirVo.getDir());
        System.out.println(dirVo.getStartTimer());

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateFormat tmp=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDateTime=localDateTime.format(formatter);
        Date endTimeValue= tmp.parse(formatDateTime);
        if(dirVo.getStartTimer()==endTimeValue){
            System.out.println("start");
        }
//        producerCSVService.importCSVEvent(dirVo.getDir());
        return toAxiosResult(true);
    }
    @PostMapping("addNewScheduleTask")
    public AxiosResult<Boolean> addNewSheduleTask(@RequestBody ScheduleTask scheduleTask)  {
        System.out.println(scheduleTask);
        UUID uuid= UUID.randomUUID();
        scheduleTask.setTid(uuid.toString());
        scheduleTask.setTaskStatus("1");
        boolean res=iScheduleTaskService.addNewScheduleTask(scheduleTask);
        return toAxiosResult(res);
    }
    @PostMapping("checkScheduleTaskByType")
    public AxiosResult<PageResult<ScheduleTaskVo>> checkAllSheduleTaskByType(@RequestBody ScheduleTaskQueryCriteria scheduleTaskQueryCriteria)  {
        PageResult<ScheduleTaskVo>list=iScheduleTaskService.checkAllScheduleTaskByType(scheduleTaskQueryCriteria);
        return AxiosResult.success(list);
    }
    @PostMapping("checkAllScheduleTask")
    public AxiosResult<PageResult<ScheduleTaskVo>> checkAllSheduleTask(@RequestBody ScheduleTaskQueryCriteria scheduleTaskQueryCriteria)  {
        PageResult<ScheduleTaskVo>list=iScheduleTaskService.checkAllScheduleTask(scheduleTaskQueryCriteria);
        return AxiosResult.success(list);
    }
    @PostMapping("checkAllScheduleTaskByTime")
    public AxiosResult<PageResult<ScheduleTaskVo>> checkAllSheduleTaskByTime(@RequestBody ScheduleTaskQueryCriteria scheduleTaskQueryCriteria)  {
        PageResult<ScheduleTaskVo>list=iScheduleTaskService.checkAllScheduleTaskByTime(scheduleTaskQueryCriteria);
        return AxiosResult.success(list);
    }
    @PostMapping("checkAllScheduleTaskByQuery")
    public AxiosResult<PageResult<ScheduleTaskVo>> checkAllSheduleTaskByQuery(@RequestBody ScheduleTaskQueryCriteria scheduleTaskQueryCriteria)  {
        PageResult<ScheduleTaskVo>list=iScheduleTaskService.checkAllScheduleTaskByQuery(scheduleTaskQueryCriteria);
        return AxiosResult.success(list);
    }
    @PostMapping("checkAllActivedScheduleTaskByQuery")
    public AxiosResult<PageResult<ScheduleTaskVo>> checkAllActivedSheduleTaskByQuery(@RequestBody ScheduleTaskQueryCriteria scheduleTaskQueryCriteria)  {
        PageResult<ScheduleTaskVo>list=iScheduleTaskService.checkAllActivedScheduleTaskByQuery(scheduleTaskQueryCriteria);
        return AxiosResult.success(list);
    }
//    @GetMapping("test")
//    public AxiosResult<Boolean> test()  {
//        ScheduledTaskRegistrar scheduledTaskRegistrar=new ScheduledTaskRegistrar();
//        startScheduleTask.configureTasks(scheduledTaskRegistrar);
//        return AxiosResult.success();
//    }
    @PostMapping("editScheduleTaskByID")
    public AxiosResult<Boolean> editScheduleTaskByTID(@RequestBody ScheduleTask scheduleTask)  {
        ScheduleTaskQueryCriteria scheduleTaskQueryCriteria=new ScheduleTaskQueryCriteria();
        scheduleTaskQueryCriteria.setTid(scheduleTask.getTid());
        scheduleTaskQueryCriteria.setTaskStatus("0");
        ScheduleTask tmpScheduleTask;
        tmpScheduleTask=iScheduleTaskService.findById(scheduleTask.getTid());
        iScheduleTaskService.editScheduleTaskStatusByEid(scheduleTaskQueryCriteria);
        tmpScheduleTask.setExecuteTime(scheduleTask.getExecuteTime());
        tmpScheduleTask.setStartTime(scheduleTask.getStartTime());
        tmpScheduleTask.setExpireTime(scheduleTask.getExpireTime());
        tmpScheduleTask.setTaskPath(scheduleTask.getTaskPath());
        tmpScheduleTask.setExecuteInterval(scheduleTask.getExecuteInterval());
        tmpScheduleTask.setTaskStatus("1");
        System.out.println(tmpScheduleTask+ "tmpScheduleTask");
        UUID uuid=UUID.randomUUID();
        tmpScheduleTask.setTid(uuid.toString());
        iScheduleTaskService.addNewScheduleTask(tmpScheduleTask);
        return AxiosResult.success();
    }
    @PostMapping("deleteScheduleTaskByID")
    public AxiosResult<Boolean> deleteScheduleTaskByTID(@RequestBody ScheduleTask scheduleTask)  {
        ScheduleTaskQueryCriteria scheduleTaskQueryCriteria=new ScheduleTaskQueryCriteria();
        scheduleTaskQueryCriteria.setTid(scheduleTask.getTid());
        scheduleTaskQueryCriteria.setTaskStatus("0");
        iScheduleTaskService.editScheduleTaskStatusByEid(scheduleTaskQueryCriteria);
        return AxiosResult.success();
    }

    @PostMapping("deleteTaskTypeByID")
    public AxiosResult<Boolean> deleteTaskTypeByID(@RequestBody EditTask editTask)  {
        iEditTaskService.deleteTaskTypeById(editTask);
        return AxiosResult.success();
    }
    @GetMapping("checkAllTaskType")
    public AxiosResult<List<EditTask>> checkAllTaskType()  {
        List<EditTask> list=iEditTaskService.selectTaskType();
        return AxiosResult.success(list);
    }
    @PostMapping("addTaskType")
    public AxiosResult<Boolean> addTaskType(@RequestBody EditTask editTask) {
        UUID uuid=UUID.randomUUID();
        editTask.setTid(uuid.toString());
        editTask.setStatus(1);
        iEditTaskService.addScheduleTask(editTask);
        return AxiosResult.success();
    }
}
