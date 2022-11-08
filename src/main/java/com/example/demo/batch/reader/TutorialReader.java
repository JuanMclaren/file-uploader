package com.example.demo.batch.reader;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.batch.persistence.domain.Tutorial;

@Configuration
public class TutorialReader {
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
}
