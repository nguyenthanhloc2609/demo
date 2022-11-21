package com.example.demo.quartz;

import com.example.demo.service.FinanceServiceImpl;
import com.example.demo.service.IFinanceService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateFinanceJob implements Job {
    @Autowired
    IFinanceService financeService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String type = dataMap.getString("type");
        Logger.getLogger(CreateFinanceJob.class.getName()).log(Level.INFO, "execute job type: " + type);
        if (type.equals("day")) {
            //create a document
        } else if (type.equals("year")) {

        }
    }
}
