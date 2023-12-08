package com.example.gymreportserver.repository;

import com.example.gymreportserver.entity.TrainingReport;
import com.example.gymreportserver.payload.constants.ReportType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@DataMongoTest
class TrainingReportRepositoryTest {

    @Autowired
    private TrainingReportRepository trainingReportRepository;

    @Test
    void findByTrainerUsername() {
        trainingReportRepository.save(new TrainingReport("trainer","test","test",true, Map.of(2023,Map.of("JANUARY",2)), ReportType.ADD));
        Optional<TrainingReport> trainingReportOptional = trainingReportRepository.findByTrainerUsername("trainer");
        assertThat(trainingReportOptional).isPresent();
    }
}