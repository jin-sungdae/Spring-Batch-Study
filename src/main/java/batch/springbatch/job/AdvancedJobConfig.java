package batch.springbatch.job;

import batch.springbatch.job.validator.LocalDateParameterValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
@Slf4j
public class AdvancedJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job advancedJob(JobExecutionListener jobExecutionListener,
                           Step advancedStep) {
        return jobBuilderFactory.get("advancedJob")
                .incrementer(new RunIdIncrementer())
                .validator(new LocalDateParameterValidator("targetDate"))
                .listener(jobExecutionListener)
                .start(advancedStep)
                .build();
    }

    @JobScope
    @Bean
    public JobExecutionListener jobExecutionListener() {
        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                log.info("[JobExecutionListener#beforeJob] jobExecution is " + jobExecution.getStatus());
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                if (jobExecution.getStatus() == BatchStatus.FAILED) {
                    log.error("[JobExecutionListener#afterJob] JobExecution is FAILED!!! RECOVER ASAP");
                    // NotificationService.notify("");
                } else {
                    log.info("[JobExecutionListener#afterJob] jobExecution is " + jobExecution.getStatus());
                }
            }
        };
    }

    @JobScope
    @Bean
    public Step advancedStep(StepExecutionListener stepExecutionListener,
            Tasklet advancedTasklet) {
        return stepBuilderFactory.get("advancedStep")
                .tasklet(advancedTasklet)
                .build();
    }

    @StepScope
    @Bean
    public StepExecutionListener stepExecutionListener() {
        return new StepExecutionListener() {
            @Override
            public void beforeStep(StepExecution stepExecution) {
                log.info("[StepExecutionListener#beforStep] stepExecution is " + stepExecution.getStatus());
            }

            @Override
            public ExitStatus afterStep(StepExecution stepExecution) {
                log.info("[StepExecutionListener#afterStep] stepExecution is " + stepExecution.getStatus());
                return stepExecution.getExitStatus();
            }
        };
    }


    @StepScope
    @Bean
    public Tasklet advancedTasklet(@Value("#{jobParameters['targetDate']}") String targetDate) {
        return ((contribution, chunkContext) -> {
            log.info("[advancedJobConfig] JobParameter - targetDate = " + targetDate);
            log.info("[advancedJobConfig] excuted advancedTasklet");
            //throw new RuntimeException("ERROR!!!!");
            return RepeatStatus.FINISHED;
        });
    }
}
