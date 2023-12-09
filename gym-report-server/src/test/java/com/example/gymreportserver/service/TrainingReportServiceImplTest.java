package com.example.gymreportserver.service;

import com.example.gymreportserver.entity.TrainingReport;
import com.example.gymreportserver.payload.constants.ReportType;
import com.example.gymreportserver.payload.request.ReportRequest;
import com.example.gymreportserver.repository.TrainingReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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

    @BeforeEach
    void setUp() {
        trainingReportService = new TrainingReportServiceImpl(trainingReportRepository);
        Map<String, Integer> month = new HashMap<>();
        month.put("DECEMBER", 2);
        Map<Integer, Map<String, Integer>> years = new HashMap<>();
        years.put(2023, month);
        trainingReport = new TrainingReport(trainerUsername, "test", "test", true, years, ReportType.ADD);
    }

    @Test
    void postReport() {
        given(trainingReportRepository.findByTrainerUsername(trainerUsername)).willReturn(Optional.of(trainingReport));
        trainingReportService.postReport(new ReportRequest(trainerUsername, "test", "test", true, LocalDate.now(), 2, ReportType.ADD), "transactionId");
        verify(trainingReportRepository).findByTrainerUsername(trainerUsername);
        verify(trainingReportRepository).save(trainingReport);
    }

    @Test
    void getAll() {
        given(trainingReportRepository.findAll()).willReturn(List.of(trainingReport));
        List<TrainingReport> trainingReports = trainingReportService.getAll();
        assertThat(trainingReports.size()).isEqualTo(1);
    }
}