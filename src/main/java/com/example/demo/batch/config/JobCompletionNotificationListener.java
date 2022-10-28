package com.example.demo.batch.config;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.example.demo.batch.persistence.domain.Tutorial;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

		 @Autowired
	     JdbcTemplate jdbcTemplate;

	public void beforeJob(JobExecution jobExecution) {
		log.info("Called beforeJob().");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("!!! JOB FINISHED! Time to verify the results");

			String query = "SELECT id, title, description, published FROM tutorials";
			RowMapper<Object> rowMapper = (rs, row) -> new Tutorial(rs.getLong(1), rs.getString(2), rs.getString(3),
					rs.getBoolean(4));
			jdbcTemplate.query(query, rowMapper)
					.forEach(tutorial -> log.info("Found < {} > in the database.:  ", tutorial.toString()));
		}

		else if (jobExecution.getStatus() == BatchStatus.FAILED) {
			log.error("JOB FAILED");
		}
	}

}
