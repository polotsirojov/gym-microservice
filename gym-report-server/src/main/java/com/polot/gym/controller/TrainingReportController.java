package com.polot.gym.controller;

import com.polot.gym.payload.request.ReportRequest;
import com.polot.gym.payload.response.ReportResponse;
import com.polot.gym.service.TrainingReportService;
import jakarta.servlet.http.HttpServletRequest;
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
    public HttpEntity<?> postReport(@Valid @RequestBody ReportRequest request, HttpServletRequest servletRequest){
        trainingReportService.postReport(request);
        return ResponseEntity.ok().build();
    }
}
