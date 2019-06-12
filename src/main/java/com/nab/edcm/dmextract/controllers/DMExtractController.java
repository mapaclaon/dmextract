package com.nab.edcm.dmextract.controllers;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DMExtractController {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    @Qualifier("dmExtractJob")
    Job dmExtractJob;

    @RequestMapping("/run-batch-job")
    public String handle() throws Exception {

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("source", "Spring Boot")
                .addLong("chunkSize", 3L)
                .addLong("poolSize", 5L)
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(dmExtractJob, jobParameters);

        return "Batch job has been invoked";
    }
}
