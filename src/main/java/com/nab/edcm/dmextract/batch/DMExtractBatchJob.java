package com.nab.edcm.dmextract.batch;

import com.nab.edcm.dmextract.persistence.models.DMExtract;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@Slf4j
public class DMExtractBatchJob extends JobExecutionListenerSupport {

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Value("${input.file}")
    Resource resource;

    @Autowired
    DMExtractProcessor extractProcessor;

    @Autowired
    DMExtractWriter extractWriter;

    @Bean(name = "dmExtractJob")
    public Job dmExtractJob() {

        Step step = stepBuilderFactory.get("step1")
                .<DMExtract, DMExtract> chunk(2)
                .reader(new DMExtractReader(resource))
                .processor(extractProcessor)
                .writer(extractWriter)
                .taskExecutor(taskExecutor())
                .throttleLimit(15)
                .build();

        Job job = jobBuilderFactory.get("job")
                .incrementer(new RunIdIncrementer())
                .listener(this)
                .start(step)
                .build();

        return job;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("BATCH JOB COMPLETED SUCCESSFULLY");
        }
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(15);
        taskExecutor.setWaitForTasksToCompleteOnShutdown(false);
        return taskExecutor;
    }
}
