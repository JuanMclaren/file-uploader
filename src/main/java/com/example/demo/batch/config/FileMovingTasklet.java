package com.example.demo.batch.config;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.util.StringUtils; 

@Component
public class FileMovingTasklet implements Tasklet, StepExecutionListener {

	@Value("${file.input}")
	private String fileInput;

	@Value("${file.output}")
	private String fileOutput;
	private Path filepath;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub

	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		if (filepath != null) {
			stepExecution.getJobExecution().getExecutionContext().put("filepath", filepath);
		}
		return ExitStatus.COMPLETED;
	}

 

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		Path dir = Paths.get(fileInput);
		assert Files.isDirectory(dir);
		List<Path> files = Files.list(dir).filter(p -> !Files.isDirectory(p)).collect(Collectors.toList());
		if (!files.isEmpty()) {
			Path file = files.get(0);
			Path dest = Paths.get(fileOutput+ File.separator + getFileName(file));
			Files.move(file, dest, StandardCopyOption.REPLACE_EXISTING);
			 filepath = dest;
		}
		return RepeatStatus.FINISHED;
	}

	private String getFileName(Path file) {
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_hhmmss");
		Date curDate = new Date();
		String strDate = sdf.format(curDate);
		String fileName = file.getFileName().toString();
		String[] parts = fileName.split("\\.");
		
		System.out.println(parts[0]);
		System.out.println(parts[1]);
		
		if(StringUtils.isNotBlank(parts[0])|| StringUtils.isNotBlank(parts[1])) {
			return parts[0].concat(strDate.concat(strDate)).concat(".").concat(parts[1]);
		}
		
		return file.getFileName().toString();
	}

}
