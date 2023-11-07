package com.example.gymreportserver.controller;

import com.example.gymreportserver.entity.TrainingReport;
import com.example.gymreportserver.payload.request.ReportRequest;
import com.example.gymreportserver.payload.response.ReportResponse;
import com.example.gymreportserver.repository.projection.CustomTrainingReport;
import com.example.gymreportserver.service.TrainingReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
public class TrainingReportController {
    private final TrainingReportService trainingReportService;

    @GetMapping
    public HttpEntity<List<ReportResponse>> getAll(){
        return ResponseEntity.ok(trainingReportService.getAll());
    }

    @PostMapping
    public HttpEntity<?> postReport(@Valid @RequestBody ReportRequest request){
        trainingReportService.postReport(request);
        return ResponseEntity.ok().build();
    }
}
