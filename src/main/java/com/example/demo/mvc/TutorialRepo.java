package com.example.demo.mvc;

import com.example.demo.batch.persistence.domain.Tutorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorialRepo  extends JpaRepository<Tutorial, Long> {
}
