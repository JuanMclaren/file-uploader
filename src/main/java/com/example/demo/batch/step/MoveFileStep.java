package com.example.demo.batch.step;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.example.demo.batch.config.FileMovingTasklet;

@Component
public class MoveFileStep {
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	FileMovingTasklet fileMovingTasklet;

	@Bean
	public Step moveFile() {
		return stepBuilderFactory.get("moveFile").tasklet(fileMovingTasklet).build();
	}

}
