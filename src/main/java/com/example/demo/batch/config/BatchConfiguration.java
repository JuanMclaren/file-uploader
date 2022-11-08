package com.example.demo.batch.config;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableBatchProcessing
@EnableScheduling
@Slf4j
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

 	private AtomicInteger batchRunCounter = new AtomicInteger(0);

 	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	Step step1;
	
	@Autowired
	Step moveFile;

	@Autowired
	private JobCompletionNotificationListener listener;
	
 	@Scheduled(cron="${cron.timer}")
	public void launchJob() throws Exception {
		Date date = new Date();
		log.debug("scheduler starts at " + date);
		JobExecution jobExecution = jobLauncher.run(job(),
				new JobParametersBuilder().addDate("launchDate", date).toJobParameters());
		batchRunCounter.incrementAndGet();
		log.debug("Batch job ends with status as " + jobExecution.getStatus());
		log.debug("scheduler ends ");
	}



	@Bean
	public Job job() {
		return jobBuilderFactory.get("job").listener(listener).
				start(step1).
				next(step1).
				next(moveFile).build();
	}



}
