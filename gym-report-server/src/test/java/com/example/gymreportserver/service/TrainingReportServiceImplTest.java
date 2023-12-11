package com.example.gymreportserver.service;

import com.example.gymreportserver.entity.TrainingReport;
import com.example.gymreportserver.payload.constants.ReportType;
import com.example.gymreportserver.payload.request.ReportRequest;
import com.example.gymreportserver.repository.TrainingReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TrainingReportServiceImplTest {

    @Mock
    private TrainingReportRepository trainingReportRepository;
    @InjectMocks
    private TrainingReportServiceImpl trainingReportService;
    private String trainerUsername = "trainer";
    private TrainingReport trainingReport;
    private ReportRequest reportRequest;
    private ReportRequest reportRequestFebruary;
    private ReportRequest reportRequestFebruary2024;

    @BeforeEach
    void setUp() {
        trainingReportService = new TrainingReportServiceImpl(trainingReportRepository);
        Map<String, Integer> month = new HashMap<>();
        month.put("DECEMBER", 2);
        Map<Integer, Map<String, Integer>> years = new HashMap<>();
        years.put(2023, month);
        trainingReport = new TrainingReport(trainerUsername, "test", "test", true, years, ReportType.ADD);

        LocalDate localDate = LocalDate.of(2023, Month.DECEMBER, 2);
        LocalDate localDateFebruary = LocalDate.of(2023, Month.FEBRUARY, 2);
        LocalDate localDateFebruary2024 = LocalDate.of(2024, Month.FEBRUARY, 2);
        reportRequest = new ReportRequest(trainerUsername, "test", "test", true, localDate, 2, ReportType.ADD);
        reportRequestFebruary = new ReportRequest(trainerUsername, "test", "test", true, localDateFebruary, 2, ReportType.ADD);
        reportRequestFebruary2024 = new ReportRequest(trainerUsername, "test", "test", true, localDateFebruary2024, 2, ReportType.ADD);
    }

    @Test
    void postReportForEmptyReport() {
        given(trainingReportRepository.findByTrainerUsername(trainerUsername)).willReturn(Optional.empty());
        trainingReportService.postReport(reportRequest, "transactionId");
        verify(trainingReportRepository).findByTrainerUsername(trainerUsername);

        ArgumentCaptor<TrainingReport> argumentCaptor = ArgumentCaptor.forClass(TrainingReport.class);
        verify(trainingReportRepository).save(argumentCaptor.capture());
        TrainingReport savedReport = argumentCaptor.getValue();
        assertThat(savedReport.getTrainerUsername()).isEqualTo(trainerUsername);
        assertThat(savedReport.getTrainerFirstname()).isEqualTo("test");
        assertThat(savedReport.getTrainerLastname()).isEqualTo("test");
        assertThat(savedReport.getStatus()).isTrue();
        assertThat(savedReport.getType()).isEqualTo(ReportType.ADD);
        assertThat(savedReport.getYears()).isEqualTo(Map.of(2023, Map.of("DECEMBER", 2)));
    }

    @Test
    void postReportForExistedYearAndMonth() {
        given(trainingReportRepository.findByTrainerUsername(trainerUsername)).willReturn(Optional.of(trainingReport));
        trainingReportService.postReport(reportRequest, "transactionId");
        verify(trainingReportRepository).findByTrainerUsername(trainerUsername);

        ArgumentCaptor<TrainingReport> argumentCaptor = ArgumentCaptor.forClass(TrainingReport.class);
        verify(trainingReportRepository).save(argumentCaptor.capture());
        TrainingReport savedReport = argumentCaptor.getValue();
        assertThat(savedReport.getTrainerUsername()).isEqualTo(trainerUsername);
        assertThat(savedReport.getTrainerFirstname()).isEqualTo("test");
        assertThat(savedReport.getTrainerLastname()).isEqualTo("test");
        assertThat(savedReport.getStatus()).isTrue();
        assertThat(savedReport.getType()).isEqualTo(ReportType.ADD);
        assertThat(savedReport.getYears()).isEqualTo(Map.of(2023, Map.of("DECEMBER", 4)));
    }

    @Test
    void postReportForExistedYearAndNonExistedMonth() {
        given(trainingReportRepository.findByTrainerUsername(trainerUsername)).willReturn(Optional.of(trainingReport));
        trainingReportService.postReport(reportRequestFebruary, "transactionId");
        verify(trainingReportRepository).findByTrainerUsername(trainerUsername);

        ArgumentCaptor<TrainingReport> argumentCaptor = ArgumentCaptor.forClass(TrainingReport.class);
        verify(trainingReportRepository).save(argumentCaptor.capture());
        TrainingReport savedReport = argumentCaptor.getValue();
        assertThat(savedReport.getTrainerUsername()).isEqualTo(trainerUsername);
        assertThat(savedReport.getTrainerFirstname()).isEqualTo("test");
        assertThat(savedReport.getTrainerLastname()).isEqualTo("test");
        assertThat(savedReport.getStatus()).isTrue();
        assertThat(savedReport.getType()).isEqualTo(ReportType.ADD);
        assertThat(savedReport.getYears()).isEqualTo(Map.of(2023, Map.of("DECEMBER", 2, "FEBRUARY", 2)));
    }

    @Test
    void postReportForNonExistedYear() {
        given(trainingReportRepository.findByTrainerUsername(trainerUsername)).willReturn(Optional.of(trainingReport));
        trainingReportService.postReport(reportRequestFebruary2024, "transactionId");
        verify(trainingReportRepository).findByTrainerUsername(trainerUsername);

        ArgumentCaptor<TrainingReport> argumentCaptor = ArgumentCaptor.forClass(TrainingReport.class);
        verify(trainingReportRepository).save(argumentCaptor.capture());
        TrainingReport savedReport = argumentCaptor.getValue();
        assertThat(savedReport.getTrainerUsername()).isEqualTo(trainerUsername);
        assertThat(savedReport.getTrainerFirstname()).isEqualTo("test");
        assertThat(savedReport.getTrainerLastname()).isEqualTo("test");
        assertThat(savedReport.getStatus()).isTrue();
        assertThat(savedReport.getType()).isEqualTo(ReportType.ADD);
        assertThat(savedReport.getYears()).isEqualTo(Map.of(2023, Map.of("DECEMBER", 2), 2024, Map.of("FEBRUARY", 2)));
    }

    @Test
    void getAll() {
        given(trainingReportRepository.findAll()).willReturn(List.of(trainingReport));
        List<TrainingReport> trainingReports = trainingReportService.getAll();
        assertThat(trainingReports.size()).isEqualTo(1);
    }
}