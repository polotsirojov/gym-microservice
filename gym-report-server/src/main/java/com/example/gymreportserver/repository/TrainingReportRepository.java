package com.example.gymreportserver.repository;

import com.example.gymreportserver.entity.TrainingReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainingReportRepository extends MongoRepository<TrainingReport, Long> {
    Optional<TrainingReport> findByTrainerUsername(String username);
}
