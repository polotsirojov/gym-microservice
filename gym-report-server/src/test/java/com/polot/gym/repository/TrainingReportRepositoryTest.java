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
    private String firstname = "firstname";
    private String lastname = "lastname";
    private TrainingReport trainingReport;

    @BeforeEach
    void setUp() {
        trainingReport = TrainingReport.builder()
                .trainerUsername(trainerUsername)
                .trainerFirstname(firstname)
                .trainerLastname(lastname)
                .isActive(true)
                .reportYear(2023)
                .reportMonth(Month.JANUARY)
                .reportDate(10)
                .trainingDuration(2)
                .reportType(ReportType.ADD)
                .build();
        trainingReportRepository.save(trainingReport);
    }

    @Test
    void shouldSaveReport() {
        Assertions.assertThat(trainingReport.getId()).isNotNull();
        Assertions.assertThat(trainingReport.getTrainerUsername()).isEqualTo(trainerUsername);
        Assertions.assertThat(trainingReport.getTrainerFirstname()).isEqualTo(firstname);
        Assertions.assertThat(trainingReport.getTrainerLastname()).isEqualTo(lastname);
        Assertions.assertThat(trainingReport.getIsActive()).isTrue();
        Assertions.assertThat(trainingReport.getReportYear()).isEqualTo(2023);
        Assertions.assertThat(trainingReport.getReportMonth()).isEqualTo(Month.JANUARY);
        Assertions.assertThat(trainingReport.getReportDate()).isEqualTo(10);
        Assertions.assertThat(trainingReport.getTrainingDuration()).isEqualTo(2);
        Assertions.assertThat(trainingReport.getReportType()).isEqualTo(ReportType.ADD);
    }

    @Test
    void shouldReturnOneReport() {
        List<CustomTrainingReport> reportList = trainingReportRepository.findAllCustomTrainingReportsSummary();
        Assertions.assertThat(reportList.size()).isEqualTo(1);
        CustomTrainingReport trainingReport = reportList.get(0);
        Assertions.assertThat(trainingReport.getTrainerUsername()).isEqualTo(trainerUsername);
        Assertions.assertThat(trainingReport.getTrainerFirstname()).isEqualTo(firstname);
        Assertions.assertThat(trainingReport.getTrainerLastname()).isEqualTo(lastname);
        Assertions.assertThat(trainingReport.getIsActive()).isTrue();
        Assertions.assertThat(trainingReport.getYear()).isEqualTo(2023);
        Assertions.assertThat(trainingReport.getMonth()).isEqualTo(Month.JANUARY.toString());
        Assertions.assertThat(trainingReport.getDurationSummary()).isEqualTo(2);
    }

    @Test
    void shouldReturnOneReportWithGrouping() {
        TrainingReport report = TrainingReport.builder()
                .trainerUsername(trainerUsername)
                .trainerFirstname(firstname)
                .trainerLastname(lastname)
                .isActive(true)
                .reportYear(2023)
                .reportMonth(Month.JANUARY)
                .reportDate(1)
                .trainingDuration(1)
                .reportType(ReportType.ADD)
                .build();
        trainingReportRepository.save(report);
        List<CustomTrainingReport> reportList = trainingReportRepository.findAllCustomTrainingReportsSummary();
        Assertions.assertThat(reportList.size()).isEqualTo(1);
        CustomTrainingReport trainingReport = reportList.get(0);
        Assertions.assertThat(trainingReport.getTrainerUsername()).isEqualTo(trainerUsername);
        Assertions.assertThat(trainingReport.getTrainerFirstname()).isEqualTo(firstname);
        Assertions.assertThat(trainingReport.getTrainerLastname()).isEqualTo(lastname);
        Assertions.assertThat(trainingReport.getIsActive()).isTrue();
        Assertions.assertThat(trainingReport.getYear()).isEqualTo(2023);
        Assertions.assertThat(trainingReport.getMonth()).isEqualTo(Month.JANUARY.toString());
        Assertions.assertThat(trainingReport.getDurationSummary()).isEqualTo(3);
    }

    @Test
    void shouldReturnTwoReport() {
        TrainingReport report = TrainingReport.builder()
                .trainerUsername(trainerUsername)
                .trainerFirstname(firstname)
                .trainerLastname(lastname)
                .isActive(true)
                .reportYear(2024)
                .reportMonth(Month.JANUARY)
                .reportDate(1)
                .trainingDuration(1)
                .reportType(ReportType.ADD)
                .build();
        trainingReportRepository.save(report);
        List<CustomTrainingReport> reportList = trainingReportRepository.findAllCustomTrainingReportsSummary();
        Assertions.assertThat(reportList.size()).isEqualTo(2);
        CustomTrainingReport trainingReport = reportList.get(0);
        Assertions.assertThat(trainingReport.getTrainerUsername()).isEqualTo(trainerUsername);
        Assertions.assertThat(trainingReport.getTrainerFirstname()).isEqualTo(firstname);
        Assertions.assertThat(trainingReport.getTrainerLastname()).isEqualTo(lastname);
        Assertions.assertThat(trainingReport.getIsActive()).isTrue();
        Assertions.assertThat(trainingReport.getYear()).isEqualTo(2023);
        Assertions.assertThat(trainingReport.getMonth()).isEqualTo(Month.JANUARY.toString());
        Assertions.assertThat(trainingReport.getDurationSummary()).isEqualTo(2);

        CustomTrainingReport secondTrainingReport = reportList.get(1);
        Assertions.assertThat(secondTrainingReport.getTrainerUsername()).isEqualTo(trainerUsername);
        Assertions.assertThat(secondTrainingReport.getTrainerFirstname()).isEqualTo(firstname);
        Assertions.assertThat(secondTrainingReport.getTrainerLastname()).isEqualTo(lastname);
        Assertions.assertThat(secondTrainingReport.getIsActive()).isTrue();
        Assertions.assertThat(secondTrainingReport.getYear()).isEqualTo(2024);
        Assertions.assertThat(secondTrainingReport.getMonth()).isEqualTo(Month.JANUARY.toString());
        Assertions.assertThat(secondTrainingReport.getDurationSummary()).isEqualTo(1);
    }
}