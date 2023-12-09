package com.polot.gym.repository;

import com.polot.gym.entity.TrainingReport;
import com.polot.gym.payload.constants.Month;
import com.polot.gym.payload.constants.ReportType;
import com.polot.gym.repository.projection.CustomTrainingReport;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
class TrainingReportRepositoryTest {

    @Autowired
    private TrainingReportRepository trainingReportRepository;
    private String trainerUsername = "trainer";

    @BeforeEach
    void setUp() {
        TrainingReport trainingReport = TrainingReport.builder()
                .trainerUsername(trainerUsername)
                .trainerFirstname("firstname")
                .trainerLastname("lastname")
                .isActive(true)
                .year(2023)
                .month(Month.JANUARY)
                .date(10)
                .trainingDuration(2)
                .type(ReportType.ADD)
                .build();
        trainingReportRepository.save(trainingReport);
    }

    @Test
    void shouldReturnOneReport() {
        List<CustomTrainingReport> reportList = trainingReportRepository.findAllCustomTrainingReportsSummary();
        Assertions.assertThat(reportList.size()).isEqualTo(1);
    }
}