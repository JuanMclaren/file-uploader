package com.example.demo.mvc.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.servlet.ServletContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.example.demo.DemoApplication;

@RunWith(SpringRunner.class)
@WebMvcTest(CSVController.class)
@ComponentScan(basePackageClasses = DemoApplication.class)
public class CSVControllerTest {
	
		@Autowired
	    private MockMvc mvc;
		

	    @Autowired
	    private WebApplicationContext webApplicationContext;
	    
	    @Test
	    public void givenWac_whenServletContext_thenItProvidesGreetController() {
	        final ServletContext servletContext = webApplicationContext.getServletContext();
	        assertNotNull(servletContext);
	        assertTrue(servletContext instanceof MockServletContext);
	        assertNotNull(webApplicationContext.getBean("cSVController"));
	    }

}
