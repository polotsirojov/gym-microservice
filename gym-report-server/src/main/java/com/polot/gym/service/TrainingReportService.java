package com.polot.gym.service;

import com.polot.gym.payload.request.ReportRequest;
import com.polot.gym.payload.response.ReportResponse;

import java.util.List;

public interface TrainingReportService {
    void postReport(ReportRequest request);

    List<ReportResponse> getAll();
}
