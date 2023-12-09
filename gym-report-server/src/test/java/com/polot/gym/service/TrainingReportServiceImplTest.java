package com.polot.gym.service;

import com.polot.gym.entity.TrainingReport;
import com.polot.gym.payload.constants.Month;
import com.polot.gym.payload.constants.ReportType;
import com.polot.gym.payload.request.ReportRequest;
import com.polot.gym.payload.response.ReportResponse;
import com.polot.gym.repository.TrainingReportRepository;
import com.polot.gym.repository.projection.CustomTrainingReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TrainingReportServiceImplTest {

    @Mock
    private TrainingReportRepository trainingReportRepository;

    @InjectMocks
    private TrainingReportServiceImpl trainingReportService;

    private String trainerUsername = "trainer";
    private String firstname = "firstname";
    private String lastname = "lastname";
    private Boolean isActive = true;
    private int year = 2023;
    private int day = 10;
    private int trainingDuration = 2;
    private ReportType type = ReportType.ADD;
    private TrainingReport trainingReport;
    private CustomTrainingReport customTrainingReport;

    @BeforeEach
    void setUp() {
        trainingReportService = new TrainingReportServiceImpl(trainingReportRepository);
        trainingReport = TrainingReport.builder()
                .trainerUsername(trainerUsername)
                .trainerFirstname(firstname)
                .trainerLastname(lastname)
                .isActive(isActive)
                .year(year)
                .month(Month.JANUARY)
                .date(day)
                .trainingDuration(trainingDuration)
                .type(type)
                .build();

        customTrainingReport = new CustomTrainingReport() {
            @Override
            public String getTrainerUsername() {
                return trainerUsername;
            }

            @Override
            public String getTrainerFirstname() {
                return firstname;
            }

            @Override
            public String getTrainerLastname() {
                return lastname;
            }

            @Override
            public Boolean getIsActive() {
                return isActive;
            }

            @Override
            public Integer getYear() {
                return year;
            }

            @Override
            public String getMonth() {
                return "JANUARY";
            }

            @Override
            public Integer getDurationSummary() {
                return trainingDuration;
            }
        };
    }

    @Test
    void postReport() {
        ReportRequest request = new ReportRequest(trainerUsername, firstname, lastname, isActive, LocalDate.of(year, java.time.Month.JANUARY, day).toString(), trainingDuration, type);
        given(trainingReportRepository.save(trainingReport)).willReturn(trainingReport);
        trainingReportService.postReport(request);
        verify(trainingReportRepository,times(1)).save(TrainingReport.builder()
                .trainerUsername(trainerUsername)
                .trainerFirstname(request.getTrainerFirstname())
                .trainerLastname(request.getTrainerLastname())
                .isActive(request.getIsActive())
                .year(LocalDate.parse(request.getTrainingDate()).getYear())
                .month(Month.valueOf(LocalDate.parse(request.getTrainingDate()).getMonth().name()))
                .date(LocalDate.parse(request.getTrainingDate()).getDayOfMonth())
                .trainingDuration(request.getTrainingDuration())
                .type(request.getType())
                .build());
    }

    @Test
    void getAll() {
        List<CustomTrainingReport> reports = Arrays.asList(customTrainingReport);
        given(trainingReportRepository.findAllCustomTrainingReportsSummary()).willReturn(reports);
        List<ReportResponse> reportResponses = trainingReportService.getAll();
        assertThat(reportResponses.size()).isEqualTo(1);
    }
}