package batch.springbatch.job;

import batch.springbatch.core.domain.Calendar;
import batch.springbatch.core.domain.DayType;
import batch.springbatch.core.domain.Person;
import batch.springbatch.core.domain.ResultText;
import batch.springbatch.core.repository.PersonRepository;
import batch.springbatch.job.validator.DayOfWeek;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class ItemWriterConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;

    public ItemWriterConfig(JobBuilderFactory jobBuilderFactory,
                            StepBuilderFactory stepBuilderFactory,
                            DataSource dataSource,
                            EntityManagerFactory entityManagerFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.dataSource = dataSource;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Bean
    public Job itemWriterJob() throws Exception {
        return this.jobBuilderFactory.get("itemWriterJob")
                .incrementer(new RunIdIncrementer())
                //.start(this.csvItemWriterStep())
                //.start(this.jdbcBatchItemWriterStep())
                //.start(this.jpaItemWriterStep())
                .start(this.jpaWriterStep())
                .build();
    }

    @Bean
    public Step jdbcBatchItemWriterStep2() {
        return stepBuilderFactory.get("jdbcBatchItemWriterStep2")
                .<Person, Person>chunk(10)
                .reader(itemReader())
                .writer(jdbcBatchItemWriter2())
                .build();
    }

    private ItemWriter<Person> jdbcBatchItemWriter2() {
        JdbcBatchItemWriter<Person> itemWriter = new JdbcBatchItemWriterBuilder<Person>()
                .dataSource(dataSource)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("insert into person(name, age, address) values(:name, :age, :address)")
                .build();

        itemWriter.afterPropertiesSet();
        return itemWriter;
    }

    @Bean
    public Step csvItemWriterStep() throws Exception {
        return this.stepBuilderFactory.get("csvItemWriterStep")
                .<Person, Person>chunk(10)
                .reader(itemReader())
                .writer(csvItemWriter())
                .build();

    }

    private ItemWriter<Person> csvItemWriter() throws Exception {
        BeanWrapperFieldExtractor<Person> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[] {"id", "name", "age", "address"});

        DelimitedLineAggregator<Person> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);

        FlatFileItemWriter<Person> itemWriter = new FlatFileItemWriterBuilder<Person>()
                .name("csvFileItemWriter")
                .encoding("UTF-8")
                .resource(new FileSystemResource("output/test-output.csv"))
                .lineAggregator(lineAggregator)
                .headerCallback(writer -> writer.write("id,이름,나이,거주지"))
                .footerCallback(writer -> writer.write("----------------\n"))
                .append(true)
                .build();
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }

    private ItemReader<Person> itemReader() {
        return new CustomItemReader<>(getItems());
    }

    private List<Person> getItems() {
        List<Person> items = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            items.add(new Person(i + 1, "test name" + i, "test age", "test address"));
        }

        return items;
    }

    // **********************************************************************************************//

    @Bean
    public Step jdbcBatchItemWriterStep() {
        return stepBuilderFactory.get("jdbcBatchItemWriterStep")
                .<Calendar, Calendar>chunk(10)
                .reader(itemReader2())
                .writer(jdbcBatchItemWriter())
                .build();
    }

    private ItemWriter<Calendar> jdbcBatchItemWriter() {
        JdbcBatchItemWriter<Calendar> itemWriter = new JdbcBatchItemWriterBuilder<Calendar>()
                .dataSource(dataSource)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("insert into calendar(date, day_type) values(:date, day_type)")
                .build();

        itemWriter.afterPropertiesSet();
        return itemWriter;
    }


    private ItemReader<Calendar> itemReader2() {
        return new CustomItemReader<>(getItems2());
    }

    private List<Calendar> getItems2() {
        List<Calendar> items = new ArrayList<>();
        if (DayOfWeek.getDayOfWeek() == 1 || DayOfWeek.getDayOfWeek() == 7)
            items.add(new Calendar( LocalDate.now(), DayType.HOLIDAY, LocalDateTime.now(), LocalDateTime.now(), "create_id", "update_id"));
        else
            items.add(new Calendar( LocalDate.now(), DayType.STUDYDAY, LocalDateTime.now(), LocalDateTime.now(), "create_id", "update_id"));


        return items;
    }

    // *******************************************jpaItemWriter로 설정!!***************************************************//

    @Bean
    public Step jpaItemWriterStep() throws Exception {
        return stepBuilderFactory.get("jpaItemWriterStep")
                .<Calendar, Calendar>chunk(10)
                .reader(itemReader2())
                .writer(jpaItemWriter())
                .build();
    }

    private ItemWriter<Calendar> jpaItemWriter() throws Exception {
        JpaItemWriter<Calendar> itemWriter = new JpaItemWriterBuilder<Calendar>()
                .entityManagerFactory(entityManagerFactory)
                .build();
        itemWriter.afterPropertiesSet();

        return itemWriter;
    }

    // *******************************************READER***************************************************//

    private ItemWriter<? super ResultText> jpaItemWriter2() throws Exception {
        JpaItemWriter<ResultText> itemWriter = new JpaItemWriterBuilder<ResultText>()
                .entityManagerFactory(entityManagerFactory)
                .build();
        itemWriter.afterPropertiesSet();

        return itemWriter;
    }

    @Bean
    public Step jpaWriterStep() throws Exception {
        return stepBuilderFactory.get("jpaWriterStep")
                .<Person, Person>chunk(10)
                .reader(this.jpaCursorItemReader())
                .processor(itemProcesor2())
                .writer(jpaItemWriter2())
                .build();
    }

//    private ItemReader<Calendar> itemReader2() {
//        return new CustomItemReader<>(getItems2());
//    }
//
//    private List<Calendar> getItems2() {
//        List<Calendar> items = new ArrayList<>();
//        if (DayOfWeek.getDayOfWeek() == 1 || DayOfWeek.getDayOfWeek() == 7)
//            items.add(new Calendar( LocalDate.now(), DayType.HOLIDAY, LocalDateTime.now(), LocalDateTime.now(), "create_id", "update_id"));
//        else
//            items.add(new Calendar( LocalDate.now(), DayType.STUDYDAY, LocalDateTime.now(), LocalDateTime.now(), "create_id", "update_id"));
//
//
//        return items;
//    }



    private JpaCursorItemReader<Person> jpaCursorItemReader() throws Exception {
        JpaCursorItemReader<Person> itemReader = new JpaCursorItemReaderBuilder<Person>()
                .name("jpaCursorItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select p from Person p")
                .build();
        itemReader.afterPropertiesSet();

        return itemReader;
    }

    private ItemProcessor<? super Person, ? extends Person> itemProcesor2() {
        return item -> {
            if (item.getId() % 2 == 0) {
                return item.getId();
            }
            return null;
        };
    }
}
