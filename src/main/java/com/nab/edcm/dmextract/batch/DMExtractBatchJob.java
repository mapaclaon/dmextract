package com.nab.edcm.dmextract.batch;

import com.nab.edcm.dmextract.persistence.models.DMExtract;
import com.nab.edcm.dmextract.persistence.models.TransformedDMExtract;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
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
    InitProcessor initProcessor;

    @Autowired
    DMExtractProcessor extractProcessor;

    @Autowired
    InitWriter initWriter;

    @Autowired
    DMExtractWriter extractWriter;

    @Bean(name = "dmExtractJob")
    public Job dmExtractJob() {

        Job job = jobBuilderFactory.get("job")
                .incrementer(new RunIdIncrementer())
                .listener(this)
                .start(step1(null))
                .next(step2(null))
                .build();

        return job;
    }

    @Bean
    @JobScope
    public Step step1(@Value("#{jobParameters['chunkSize']}") Integer chunkSize) {
        log.info("CHUNK SIZE [{}] ====", chunkSize);
        return stepBuilderFactory.get("step1")
                .<DMExtract, DMExtract> chunk(chunkSize)
                .reader(new InitReader(resource))
                .processor(initProcessor)
                .writer(initWriter)
                .build();
    }

    @Bean
    @JobScope
    public Step step2(@Value("#{jobParameters['chunkSize']}") Integer chunkSize) {
        log.info("CHUNK SIZE [{}] +++++++", chunkSize);
        return stepBuilderFactory.get("step2")
                .<DMExtract, TransformedDMExtract> chunk(chunkSize)
                .reader(new DMExtractReader(resource))
                .processor(extractProcessor)
                .writer(extractWriter)
                .taskExecutor(taskExecutor(null))
                .throttleLimit(15)
                .build();
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("BATCH JOB COMPLETED SUCCESSFULLY");
        }
    }

    @Bean
    @StepScope
    public ThreadPoolTaskExecutor taskExecutor(@Value("#{jobParameters['poolSize']}") Integer poolSize){
        log.info("POOL SIZE [{}] ====", poolSize);
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(poolSize);
        taskExecutor.setWaitForTasksToCompleteOnShutdown(false);
        return taskExecutor;
    }
}
