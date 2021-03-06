package com.example.springbatch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class HelloWorldJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job helloWorldJob() {
        return jobBuilderFactory.get("helloWorldJob")
                .start(helloWorldJob_Step1(null))
                .next(helloWorldJob_Step2())
                .build();
    }

    @Bean
    @JobScope
    public Step helloWorldJob_Step1(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return stepBuilderFactory.get("helloWorldJob_Step1")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is helloWorldJob_Step1");
                    log.info(">>>>> JobParameter requestDate={} ", requestDate);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step helloWorldJob_Step2() {
        return stepBuilderFactory.get("helloWorldJob_Step2")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is helloWorldJob_Step2");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
