package com.example.gymreportserver.service;

import com.example.gymreportserver.entity.TrainingReport;
import com.example.gymreportserver.payload.constants.Month;
import com.example.gymreportserver.payload.request.ReportRequest;
import com.example.gymreportserver.repository.TrainingReportRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainingReportServiceImpl implements TrainingReportService {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private final TrainingReportRepository trainingReportRepository;

    @Override
    public void postReport(ReportRequest request, String transactionId) {
        log.info("gym report postReport TransactionId: {}, RequestBody: {}", transactionId, request);
        int year = request.getTrainingDate().getYear();
        Month month = Month.valueOf(request.getTrainingDate().getMonth().name());

        Optional<TrainingReport> trainingReportOptional = trainingReportRepository.findByTrainerUsername(request.getTrainerUsername());
        Map<Integer, Map<String, Integer>> d = new HashMap<>();
        Map<String, Integer> m = new HashMap<>();

        if (trainingReportOptional.isEmpty()) {
            m.put(month.name(), request.getTrainingDuration());
            d.put(year, m);
            trainingReportRepository.save(TrainingReport.builder()
                    .trainerUsername(request.getTrainerUsername())
                    .trainerFirstname(request.getTrainerFirstname())
                    .trainerLastname(request.getTrainerLastname())
                    .status(request.getIsActive())
                    .years(d)
                    .type(request.getType())
                    .build());
        } else {
            d = trainingReportOptional.get().getYears();
            if (d.containsKey(year)) {
                m = d.get(year);
                if (m.containsKey(month.name())) {
                    Integer duration = m.get(month.name());
                    m.put(month.name(), duration + request.getTrainingDuration());
                } else {
                    m.put(month.name(), request.getTrainingDuration());
                }
            } else {
                m.put(month.name(), request.getTrainingDuration());
                d.put(year, m);
            }
            TrainingReport trainingReport = trainingReportOptional.get();
            trainingReport.setYears(d);
            trainingReportRepository.save(trainingReport);
        }


    }

    @Override
    public List<TrainingReport> getAll() {
        return trainingReportRepository.findAll();
    }
}
