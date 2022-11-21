package com.example.demo.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.quartz.CronScheduleBuilder.cronSchedule;

public class ScheduleUtils {

    public static void startJobDaily() {
        Logger.getLogger(ScheduleUtils.class.getName()).log(Level.INFO, "create JobDaily");
        SchedulerFactory schedFact = new StdSchedulerFactory();
        try {
            Scheduler sched = schedFact.getScheduler();

            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("type", "day");
            JobDetail job = JobBuilder.newJob(CreateFinanceJob.class).withIdentity("createDay", "finance")
                    .usingJobData(jobDataMap).build();

            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("cronTrigger1", "finance").startNow()
                    .withSchedule(cronSchedule("0 0 0 * * ?"))
                    .forJob("createDay", "finance")
                    .build();
            sched.scheduleJob(job, trigger);
            sched.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startJobYear() {
        Logger.getLogger(ScheduleUtils.class.getName()).log(Level.INFO, "create Job on first day of year");
        SchedulerFactory schedFact = new StdSchedulerFactory();
        try {
            Scheduler sched = schedFact.getScheduler();

            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("type", "year");
            JobDetail job = JobBuilder.newJob(CreateFinanceJob.class).withIdentity("createDay", "finance")
                    .usingJobData(jobDataMap).build();

            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("createYear", "finance").startNow()
                    .withSchedule(cronSchedule("0 0 0 1 JAN ? 2023/1"))
                    .forJob("createYear", "finance")
                    .build();
            sched.scheduleJob(job, trigger);
            sched.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
