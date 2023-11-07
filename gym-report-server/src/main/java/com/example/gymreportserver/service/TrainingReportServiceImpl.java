package com.example.gymreportserver.service;

import com.example.gymreportserver.entity.TrainingReport;
import com.example.gymreportserver.payload.constants.Month;
import com.example.gymreportserver.payload.request.ReportRequest;
import com.example.gymreportserver.payload.response.MonthResponse;
import com.example.gymreportserver.payload.response.ReportResponse;
import com.example.gymreportserver.payload.response.YearResponse;
import com.example.gymreportserver.repository.TrainingReportRepository;
import com.example.gymreportserver.repository.projection.CustomTrainingReport;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.internal.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingReportServiceImpl implements TrainingReportService {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private final TrainingReportRepository trainingReportRepository;

    @Override
    public void postReport(ReportRequest request, String transactionId) {
        log.info("gym report postReport TransactionId: {}, RequestBody: {}", transactionId, request);
        trainingReportRepository.save(TrainingReport.builder()
                .trainerUsername(request.getTrainerUsername())
                .trainerFirstname(request.getTrainerFirstname())
                .trainerLastname(request.getTrainerLastname())
                .isActive(request.getIsActive())
                .year(request.getTrainingDate().getYear())
                .month(Month.valueOf(request.getTrainingDate().getMonth().name()))
                .date(request.getTrainingDate().getDayOfMonth())
                .trainingDuration(request.getTrainingDuration())
                .type(request.getType())
                .build());
    }

    @Override
    public List<ReportResponse> getAll() {
        List<CustomTrainingReport> reports = trainingReportRepository.findAllByGroupBy();
        List<ReportResponse> reportResponses = new ArrayList<>();
        for (CustomTrainingReport report : reports) {
            Optional<ReportResponse> first = reportResponses.stream()
                    .filter(r -> r.getTrainerUsername().equals(report.getTrainerUsername()) &&
                            r.getTrainerFirstname().equals(report.getTrainerFirstname()) &&
                            r.getTrainerLastname().equals(report.getTrainerLastname()) &&
                            r.getStatus().equals(report.getIsActive())
                    ).findFirst();
            if (first.isPresent()) {
                reportResponses = reportResponses.stream()
                        .map(r -> {
                            if (r.getTrainerUsername().equals(report.getTrainerUsername()) &&
                                    r.getTrainerFirstname().equals(report.getTrainerFirstname()) &&
                                    r.getTrainerLastname().equals(report.getTrainerLastname()) &&
                                    r.getStatus().equals(report.getIsActive())) {
                                if (r.getYears().stream().filter(y -> y.getYear().equals(report.getYear())).findFirst().isEmpty()) {
                                    List<YearResponse> yList = new ArrayList<>(r.getYears());
                                    yList.add(YearResponse.builder()
                                            .year(report.getYear())
                                            .months(List.of(MonthResponse.builder()
                                                    .month(report.getMonth())
                                                    .duration(report.getDurationSummary())
                                                    .build()))
                                            .build());
                                    return ReportResponse.builder()
                                            .trainerUsername(r.getTrainerUsername())
                                            .trainerLastname(r.getTrainerLastname())
                                            .trainerFirstname(r.getTrainerFirstname())
                                            .status(r.getStatus())
                                            .years(yList)
                                            .build();
                                } else {
                                    return ReportResponse.builder()
                                            .trainerUsername(r.getTrainerUsername())
                                            .trainerLastname(r.getTrainerLastname())
                                            .trainerFirstname(r.getTrainerFirstname())
                                            .status(r.getStatus())
                                            .years(r.getYears().stream().map(y -> {
                                                if (y.getYear().equals(report.getYear())) {
                                                    List<MonthResponse> months = new ArrayList<>(y.getMonths());
                                                    months.add(MonthResponse.builder()
                                                            .month(report.getMonth())
                                                            .duration(report.getDurationSummary())
                                                            .build());
                                                    y.setMonths(months);
                                                    return y;
                                                } else {
                                                    return y;
                                                }
                                            }).collect(Collectors.toList()))
                                            .build();
                                }
                            } else {
                                return r;
                            }
                        }).collect(Collectors.toList());
            } else {
                reportResponses.add(ReportResponse.builder()
                        .trainerUsername(report.getTrainerUsername())
                        .trainerLastname(report.getTrainerLastname())
                        .trainerFirstname(report.getTrainerFirstname())
                        .status(report.getIsActive())
                        .years(Arrays.asList(YearResponse.builder()
                                .year(report.getYear())
                                .months(Arrays.asList(MonthResponse.builder()
                                        .month(report.getMonth())
                                        .duration(report.getDurationSummary()).build()))
                                .build()))
                        .build());
            }
        }
        return reportResponses;
    }
}
