package com.jkonury.www.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author JiHong Jang
 * @since 2014.08.06
 */
public class DummyJobBean extends QuartzJobBean {
    private DummyTask dummyTask;
    private DummyTask2 dummyTask2;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        dummyTask.print();
        dummyTask2.task();
    }

    public void setDummyTask(DummyTask dummyTask) {
        this.dummyTask = dummyTask;
    }

    public void setDummyTask2(DummyTask2 dummyTask2) {
        this.dummyTask2 = dummyTask2;
    }
}
