package com.polot.gym.service;

import com.polot.gym.entity.TrainingReport;
import com.polot.gym.payload.constants.ReportType;
import com.polot.gym.payload.request.ReportRequest;
import com.polot.gym.payload.response.ReportResponse;
import com.polot.gym.repository.TrainingReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TrainingReportServiceImplTest {

    @Mock
    private TrainingReportRepository trainingReportRepository;

    @InjectMocks
    private TrainingReportServiceImpl trainingReportService;

    @BeforeEach
    void setUp() {
        trainingReportService = new TrainingReportServiceImpl(trainingReportRepository);
    }

    @Test
    void postReport() {
        ReportRequest reportRequest = new ReportRequest("", "", "", true, LocalDate.now().toString(), 1, ReportType.ADD);
        TrainingReport report = new TrainingReport();
        given(trainingReportRepository.save(any())).willReturn(report);
        trainingReportService.postReport(reportRequest);
        verify(trainingReportRepository).save(any());
    }

    @Test
    void getAll() {
        given(trainingReportRepository.findAllByGroupBy()).willReturn(List.of());
        List<ReportResponse> reportResponses = trainingReportService.getAll();
        assertThat(reportResponses).isNotNull();
    }
}