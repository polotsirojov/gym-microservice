package com.polot.gym.repository;

import com.polot.gym.repository.projection.CustomTrainingReport;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TrainingReportRepositoryTest {

    @Autowired
    private TrainingReportRepository trainingReportRepository;

    @Test
    void findAllByGroupBy() {
        List<CustomTrainingReport> reportList = trainingReportRepository.findAllByGroupBy();
        Assertions.assertThat(reportList).isNotNull();
    }
}