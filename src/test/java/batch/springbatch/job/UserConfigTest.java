package batch.springbatch.job;

import batch.springbatch.BatchTestConfig;
import batch.springbatch.core.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBatchTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { UserConfig.class, BatchTestConfig.class})
class UserConfigTest {
//
//    @Autowired
//    private JobLauncherTestUtils jobLauncherTestUtils;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    public void test() throws Exception {
//        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
//
//        //int size = userRepository.findAllByUpdatedDate(LocalDate.now()).size();
//        Assertions.assertThat(jobExecution.getStepExecutions().stream()
//                .filter(x -> x.getStepName().equals("userLevelUpStep"))
//                .mapToInt(StepExecution::getWriteCount)
//                .sum())
//                .isEqualTo(size)
//                .isEqualTo(300);
//        Assertions.assertThat(userRepository.count())
//                .isEqualTo(400);
//    }

}