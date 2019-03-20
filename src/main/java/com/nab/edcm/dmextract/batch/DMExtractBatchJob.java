package com.nab.edcm.dmextract.batch;

import com.nab.edcm.dmextract.persistence.models.DMExtract;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class DMExtractBatchJob extends JobExecutionListenerSupport {

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Value("${input.file}")
    Resource resource;

    @Autowired
    DMExtractProcessor processor;

    @Autowired
    DMExtractWriter writer;

    @Bean(name = "dmExtractJob")
    public Job dmExtractJob() {

        Step step = stepBuilderFactory.get("step-1")
                .<DMExtract, DMExtract> chunk(1)
                .reader(new DMExtractReader(resource))
                .processor(processor)
                .writer(writer)
                .build();

        Job job = jobBuilderFactory.get("accounting-job")
                .incrementer(new RunIdIncrementer())
                .listener(this)
                .start(step)
                .build();

        return job;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            System.out.println("BATCH JOB COMPLETED SUCCESSFULLY");
        }
    }

}
