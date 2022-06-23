package com.BrainFlux.AutoInput.config;

import com.google.common.util.concurrent.*;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.*;

/**
 * @Author：geliyang
 * @Version：1.0
 * @Date：2022/6/21-14:39
 * @Since:jdk1.8
 * @Description:TODO
 */
@Component
public class ScheduleTask2Config extends QuartzJobBean {
    private static Integer threads=2;
    private static ListeningExecutorService service= MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(threads));
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        System.out.println(context+"context");
        JobDataMap jobDataMap=context.getJobDetail().getJobDataMap();
        JobDataMap triggerDataMap= context.getTrigger().getJobDataMap();
        String taskPath = jobDataMap.getString("taskPath");
        String taskID = triggerDataMap.getString("taskId");

        future= (ListenableFuture<String>) service.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("taskID "+taskID);
                System.out.println("taskPath "+taskPath);
                System.out.println("Current Threads"+ Thread.currentThread());
                return Thread.currentThread().getName();
            }
        });
        Futures.addCallback(future, new FutureCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("task:" +result);
            };

            @Override
            public void onFailure(Throwable t) {
            }
        });
//                      System.out.println("currentThreads "+Thread.currentThread());
    }

    private ListenableFuture<String> future= new ListenableFuture<String>() {
        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isDone() {
            return false;
        }

        @Override
        public String get() throws InterruptedException, ExecutionException {
            return null;
        }

        @Override
        public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return null;
        }

        @Override
        public void addListener(Runnable listener, Executor executor) {

        }
    };
}

