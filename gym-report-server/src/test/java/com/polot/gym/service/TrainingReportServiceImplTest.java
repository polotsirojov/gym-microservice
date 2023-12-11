package com.polot.gym.service;

import com.polot.gym.entity.TrainingReport;
import com.polot.gym.payload.constants.Month;
import com.polot.gym.payload.constants.ReportType;
import com.polot.gym.payload.request.ReportRequest;
import com.polot.gym.payload.response.MonthResponse;
import com.polot.gym.payload.response.ReportResponse;
import com.polot.gym.payload.response.YearResponse;
import com.polot.gym.repository.TrainingReportRepository;
import com.polot.gym.repository.projection.CustomTrainingReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

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
    private String firstname = "firstname";
    private String lastname = "lastname";
    private Boolean isActive = true;
    private int year = 2023;
    private int day = 10;
    private int trainingDuration = 2;
    private ReportType type = ReportType.ADD;
    private TrainingReport trainingReport;
    private CustomTrainingReport customTrainingReport;
    private CustomTrainingReport customNewTrainingReport;
    private CustomTrainingReport customTrainingReportFebruary;
    private CustomTrainingReport customTrainingReportFebruary2024;

    @BeforeEach
    void setUp() {
        trainingReportService = new TrainingReportServiceImpl(trainingReportRepository);
        trainingReport = TrainingReport.builder()
                .trainerUsername(trainerUsername)
                .trainerFirstname(firstname)
                .trainerLastname(lastname)
                .isActive(isActive)
                .reportYear(year)
                .reportMonth(Month.JANUARY)
                .reportDate(day)
                .trainingDuration(trainingDuration)
                .reportType(type)
                .build();

        customNewTrainingReport = new CustomTrainingReport() {
            @Override
            public String getTrainerUsername() {
                return "new" + trainerUsername;
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

        customTrainingReportFebruary = new CustomTrainingReport() {
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
                return "FEBRUARY";
            }

            @Override
            public Integer getDurationSummary() {
                return trainingDuration;
            }
        };

        customTrainingReportFebruary2024 = new CustomTrainingReport() {
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
                return 2024;
            }

            @Override
            public String getMonth() {
                return "FEBRUARY";
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
        trainingReportService.postReport(request);
        ArgumentCaptor<TrainingReport> argumentCaptor = ArgumentCaptor.forClass(TrainingReport.class);
        verify(trainingReportRepository).save(argumentCaptor.capture());

        TrainingReport savedReport = argumentCaptor.getValue();
        assertThat(savedReport.getTrainerUsername()).isEqualTo(trainerUsername);
        assertThat(savedReport.getTrainerFirstname()).isEqualTo(firstname);
        assertThat(savedReport.getTrainerLastname()).isEqualTo(lastname);
        assertThat(savedReport.getIsActive()).isTrue();
        assertThat(savedReport.getReportYear()).isEqualTo(year);
        assertThat(savedReport.getReportMonth()).isEqualTo(Month.JANUARY);
        assertThat(savedReport.getReportDate()).isEqualTo(day);
        assertThat(savedReport.getTrainingDuration()).isEqualTo(trainingDuration);
        assertThat(savedReport.getReportType()).isEqualTo(ReportType.ADD);
    }

    @Test
    void getOneReport() {
        List<CustomTrainingReport> reports = Arrays.asList(customTrainingReport);
        given(trainingReportRepository.findAllCustomTrainingReportsSummary()).willReturn(reports);
        List<ReportResponse> reportResponses = trainingReportService.getAll();
        assertThat(reportResponses.size()).isEqualTo(1);
        ReportResponse reportResponse = reportResponses.get(0);
        assertThat(reportResponse.getTrainerUsername()).isEqualTo(trainerUsername);
        assertThat(reportResponse.getTrainerFirstname()).isEqualTo(firstname);
        assertThat(reportResponse.getTrainerLastname()).isEqualTo(lastname);
        assertThat(reportResponse.getStatus()).isTrue();
        assertThat(reportResponse.getYears()).isEqualTo(List.of(YearResponse.builder()
                .year(year)
                .months(List.of(new MonthResponse(Month.JANUARY.toString(), trainingDuration))
                ).build()));
    }

    @Test
    void getAllByExistedYearAndNonExistedMonth() {
        List<CustomTrainingReport> reports = Arrays.asList(customTrainingReport, customTrainingReportFebruary);
        given(trainingReportRepository.findAllCustomTrainingReportsSummary()).willReturn(reports);
        List<ReportResponse> reportResponses = trainingReportService.getAll();
        assertThat(reportResponses.size()).isEqualTo(1);
        ReportResponse reportResponse = reportResponses.get(0);
        assertThat(reportResponse.getTrainerUsername()).isEqualTo(trainerUsername);
        assertThat(reportResponse.getTrainerFirstname()).isEqualTo(firstname);
        assertThat(reportResponse.getTrainerLastname()).isEqualTo(lastname);
        assertThat(reportResponse.getStatus()).isTrue();
        assertThat(reportResponse.getYears()).isEqualTo(List.of(YearResponse.builder()
                .year(year)
                .months(List.of(new MonthResponse(Month.JANUARY.toString(), trainingDuration),
                        new MonthResponse(Month.FEBRUARY.toString(), trainingDuration))
                ).build()));
    }

    @Test
    void getAllByNonExistedYear() {
        List<CustomTrainingReport> reports = Arrays.asList(customTrainingReport, customTrainingReportFebruary, customTrainingReportFebruary2024);
        given(trainingReportRepository.findAllCustomTrainingReportsSummary()).willReturn(reports);
        List<ReportResponse> reportResponses = trainingReportService.getAll();
        assertThat(reportResponses.size()).isEqualTo(1);
        ReportResponse reportResponse = reportResponses.get(0);
        assertThat(reportResponse.getTrainerUsername()).isEqualTo(trainerUsername);
        assertThat(reportResponse.getTrainerFirstname()).isEqualTo(firstname);
        assertThat(reportResponse.getTrainerLastname()).isEqualTo(lastname);
        assertThat(reportResponse.getStatus()).isTrue();
        assertThat(reportResponse.getYears()).isEqualTo(List.of(
                YearResponse.builder()
                        .year(year)
                        .months(List.of(new MonthResponse(Month.JANUARY.toString(), trainingDuration),
                                new MonthResponse(Month.FEBRUARY.toString(), trainingDuration))).build(),
                YearResponse.builder()
                        .year(2024)
                        .months(List.of(new MonthResponse(Month.FEBRUARY.toString(), trainingDuration))).build()));
    }

    @Test
    void getAllByNewTrainer() {
        List<CustomTrainingReport> reports = Arrays.asList(customTrainingReport, customNewTrainingReport);
        given(trainingReportRepository.findAllCustomTrainingReportsSummary()).willReturn(reports);
        List<ReportResponse> reportResponses = trainingReportService.getAll();
        assertThat(reportResponses.size()).isEqualTo(2);
        ReportResponse reportResponse = reportResponses.get(0);
        assertThat(reportResponse.getTrainerUsername()).isEqualTo(trainerUsername);
        assertThat(reportResponse.getTrainerFirstname()).isEqualTo(firstname);
        assertThat(reportResponse.getTrainerLastname()).isEqualTo(lastname);
        assertThat(reportResponse.getStatus()).isTrue();
        assertThat(reportResponse.getYears()).isEqualTo(List.of(YearResponse.builder()
                .year(year)
                .months(List.of(new MonthResponse(Month.JANUARY.toString(), trainingDuration))
                ).build()));

        ReportResponse newReportResponse = reportResponses.get(1);
        assertThat(newReportResponse.getTrainerUsername()).isEqualTo("new" + trainerUsername);
        assertThat(newReportResponse.getTrainerFirstname()).isEqualTo(firstname);
        assertThat(newReportResponse.getTrainerLastname()).isEqualTo(lastname);
        assertThat(newReportResponse.getStatus()).isTrue();
        assertThat(newReportResponse.getYears()).isEqualTo(List.of(YearResponse.builder()
                .year(year)
                .months(List.of(new MonthResponse(Month.JANUARY.toString(), trainingDuration))
                ).build()));
    }
}