package com.example.demo;

import com.example.demo.batch.persistence.domain.Tutorial;
import com.example.demo.mvc.TutorialRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.stream.IntStream;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		 SpringApplication.run(DemoApplication.class, args);

	}

	@Bean
	public CommandLineRunner demo(TutorialRepo repo) {
		return (args) -> {

			IntStream.range(0, 100).forEach(
					nbr -> getAndPersistTutorial(nbr,repo)
			);

		};
	}

	private void getAndPersistTutorial(int nbr, TutorialRepo repo) {
		Tutorial tutorial = Tutorial.builder().
				id(Long.valueOf(nbr)).
				title("title:" + nbr).
				description("Description:" + nbr).
				published( nbr% 5 == 0? true: false).
				build();
		System.out.println("tutorial persisted " + tutorial);
		repo.save(tutorial);
	}
}