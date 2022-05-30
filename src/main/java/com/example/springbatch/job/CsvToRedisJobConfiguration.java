package com.example.springbatch.job;

import com.example.springbatch.domain.User;
import com.example.springbatch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class CsvToRedisJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final UserRepository userRepository;
    private final int chunkSize = 10;

    @Bean
    public Job csvToRedisJob() {
        return jobBuilderFactory.get("csvToRedisJob")
                .incrementer(new RunIdIncrementer())
                .start(csvToRedisJob_Step())
                .build();
    }

    @Bean
    public Step csvToRedisJob_Step() {
        return stepBuilderFactory.get("csvToRedisJob_step")
                .<User, User>chunk(chunkSize)
                .reader(csvFlatFileItemReader())
                .writer(users -> {
                    userRepository.saveAll((users));
                })
                .build();
    }

    @Bean
    public FlatFileItemReader csvFlatFileItemReader() {
        FlatFileItemReader<User> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new ClassPathResource("/samples/users.csv"));
        flatFileItemReader.setLinesToSkip(1);

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer(",");
        delimitedLineTokenizer.setNames("id", "firstname", "lastname", "email", "email2", "profession");

        BeanWrapperFieldSetMapper<User> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(User.class);

        DefaultLineMapper<User> defaultLineMapper = new DefaultLineMapper<>();
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
        defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);

        flatFileItemReader.setLineMapper(defaultLineMapper);
        return flatFileItemReader;
    }
}
