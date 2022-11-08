package com.example.demo.batch.step;

import java.io.IOException;

import javax.sql.DataSource;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.example.demo.batch.persistence.domain.Tutorial;
import com.example.demo.batch.processor.TutorialsProcessor;
import com.example.demo.batch.setter.TutorialPreparedStatementSetter;

@Component
public class Step1Config {

	private static final String QUERY_INSERT_TUTORIAL = "INSERT INTO tutorials (id, TITLE, DESCRIPTION, PUBLISHED) VALUES (?,?, ?, ?)";

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Value("${file.input.reader}")
	private String fileInput;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	
	
	@Autowired
	private FlatFileItemReader<Tutorial> reader;

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").<Tutorial, Tutorial>chunk(10).reader(multiResourceItemReader())
				.processor(processor()).writer(csvFileDatabaseItemWriter(dataSource, namedJdbcTemplate)).faultTolerant()
				.skip(org.springframework.batch.item.file.transform.IncorrectTokenCountException.class).skipLimit(2)
				.build();
	}

	@Bean
	public MultiResourceItemReader<Tutorial> multiResourceItemReader() {
		MultiResourceItemReader<Tutorial> resourceItemReader = new MultiResourceItemReader<Tutorial>();
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = null;
		try {
			resources = resolver.getResources(fileInput);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resourceItemReader.setResources(resources);
		resourceItemReader.setDelegate(reader);
		return resourceItemReader;
	}



	@Bean
	public TutorialsProcessor processor() {
		return new TutorialsProcessor();
	}

	@Bean
	ItemWriter<Tutorial> csvFileDatabaseItemWriter(DataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate) {
		JdbcBatchItemWriter<Tutorial> databaseItemWriter = new JdbcBatchItemWriter<>();
		databaseItemWriter.setDataSource(dataSource);
		databaseItemWriter.setJdbcTemplate(jdbcTemplate);

		databaseItemWriter.setSql(QUERY_INSERT_TUTORIAL);

		ItemPreparedStatementSetter<Tutorial> valueSetter = new TutorialPreparedStatementSetter();
		databaseItemWriter.setItemPreparedStatementSetter(valueSetter);

		return databaseItemWriter;
	}
 
}
