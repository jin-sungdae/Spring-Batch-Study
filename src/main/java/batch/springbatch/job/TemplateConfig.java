package batch.springbatch.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class TemplateConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public TemplateConfig(JobBuilderFactory jobBuilderFactory,
                          StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }
//
//    @Bean
//    public Job job() {
//        return this.jobBuilderFactory.get("")
//                .incrementer(new RunIdIncrementer())
//                .start(this.step())
//                .build();
//    }

//    @Bean
//    public Step step() {
//        return this.stepBuilderFactory.get("")
//                .chunk()
//                .reader()
//                .processor()
//                .writer()
//                .build();
//
//    }
}
