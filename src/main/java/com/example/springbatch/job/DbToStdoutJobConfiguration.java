package com.example.springbatch.job;

import com.example.springbatch.users.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class DbToStdoutJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SqlSessionFactory sqlSessionFactory;

    private final int chunkSize = 10;

    @Bean
    public Job dbToStdoutJob(){
        return jobBuilderFactory.get("dbToStdoutJob")
                .start(dbToStdoutJob_step())
                .build();
    }

    @Bean
    public Step dbToStdoutJob_step() {
        return stepBuilderFactory.get("dbToStdoutJob_step")
                .<User, User>chunk(chunkSize)
                .reader(userPagingItemReader())
                .writer(userPrintItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public MyBatisPagingItemReader<User> userPagingItemReader() {
        return new MyBatisPagingItemReaderBuilder<User>()
                .pageSize(chunkSize)
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("getUser")
                .build();
    }

    @Bean
    public ItemWriter<User> userPrintItemWriter() {
        return list -> {
            for (User u : list) {
                log.info(">>>>> ID: "+ u.getId() +  " | " + u.getLastname() + " " + u.getFirstname());
            }
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        };
    }
}
