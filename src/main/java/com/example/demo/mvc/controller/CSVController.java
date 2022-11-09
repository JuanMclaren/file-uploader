package com.example.demo.mvc.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.example.demo.batch.persistence.domain.Tutorial;
import com.example.demo.mvc.TutorialRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.mvc.controller.response.ResponseMessage;
import com.example.demo.mvc.helper.CSVHelper;

@Controller
@RequestMapping("/api/csv")
public class CSVController {

	@Value("${file.input}")
	private String fileInput;


	@Autowired
	TutorialRepo tutorialRepo;


	@GetMapping("/get")
	public ResponseEntity<List<Tutorial>> get() {
		return ResponseEntity.status(HttpStatus.OK).body(tutorialRepo.findAll());
	}

	@PostMapping("/upload")
	public ResponseEntity<ResponseMessage> singleFileUpload(@RequestParam("file") MultipartFile file) {
		String message = "";
		if (file.isEmpty()) {
			message = "Please upload a  file!";
		}
		else {
			try {
				if (CSVHelper.hasCSVFormat(file)) {
					byte[] bytes = file.getBytes();
					Path path = Paths.get(fileInput + file.getOriginalFilename());
					Files.write(path, bytes);

					message = "You successfully uploaded '" + file.getOriginalFilename() + "^^^^" + path.getFileName()
							+ "'";
				} else {
					message = "Please upload a csv file!";
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Invalid Request"));
	}
}
