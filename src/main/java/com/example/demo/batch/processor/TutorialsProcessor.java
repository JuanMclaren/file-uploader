package com.example.demo.batch.processor;

import org.springframework.batch.item.ItemProcessor;

import com.example.demo.batch.persistence.domain.Tutorial;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class TutorialsProcessor implements ItemProcessor<Tutorial, Tutorial> {
	
	 @Override
	    public Tutorial process(final Tutorial tutorial) throws Exception {
	        Long id = tutorial.getId();
	        String description = tutorial.getDescription().toUpperCase();
	        String title = tutorial.getTitle().toUpperCase();
	        boolean published = tutorial.isPublished();
	        Tutorial transformedObj = new Tutorial(id, description, title,published);
	        log.info("Converting ( {} ) into ( {} )", tutorial, transformedObj);

	        return transformedObj;
	    }

}
