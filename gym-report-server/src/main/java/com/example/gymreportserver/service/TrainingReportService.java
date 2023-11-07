package com.example.gymreportserver.service;

import com.example.gymreportserver.payload.request.ReportRequest;
import com.example.gymreportserver.payload.response.ReportResponse;

import java.util.List;

public interface TrainingReportService {
    void postReport(ReportRequest request, String transactionId);

    List<ReportResponse> getAll();
}
