package com.example.demo.batch.step;

import java.io.IOException;

import javax.sql.DataSource;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
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
import org.springframework.stereotype.Component;
 
import com.example.demo.batch.persistence.domain.Tutorial;
import com.example.demo.batch.processor.TutorialsProcessor;

@Component
public class Step1 {
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Value("${file.input.reader}")
	private String fileInput;

	
	@Autowired
	private DataSource dataSource;
	
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").<Tutorial, Tutorial>chunk(10).reader(multiResourceItemReader())
				.processor(processor()).writer(writer()).faultTolerant()
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
		resourceItemReader.setDelegate(reader());
		return resourceItemReader;
	}

	@Bean
	public FlatFileItemReader<Tutorial> reader() {
		// Create reader instance
		FlatFileItemReader<Tutorial> reader = new FlatFileItemReader<Tutorial>();

		// Set number of lines to skips. Use it if file has header rows.
		reader.setLinesToSkip(1);

		// Configure how each line will be parsed and mapped to different values
		reader.setLineMapper(new DefaultLineMapper<Tutorial>() {
			{
				// 4 columns in each row
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "Id", "Title", "Description", "Published" });
					}
				});
				// Set values in Employee class
				setFieldSetMapper(new BeanWrapperFieldSetMapper<Tutorial>() {
					{
						setTargetType(Tutorial.class);
					}
				});
			}
		});
		return reader;
	}

	@Bean
	public TutorialsProcessor processor() {
		return new TutorialsProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<Tutorial> writer() {
		return new JdbcBatchItemWriterBuilder<Tutorial>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
				.sql("INSERT INTO tutorials (id, Title, Description, Published) VALUES (:id, :Title, :Description, :Published)")
				.dataSource(dataSource).build();
	}
}
