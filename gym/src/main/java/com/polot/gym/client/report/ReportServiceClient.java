package com.polot.gym.client.report;

import com.polot.gym.payload.request.ReportRequest;
import com.polot.gym.payload.response.TrainingReport;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(value = "GYM-REPORT-SERVICE", fallback = GymReportServiceFallback.class)
public interface ReportServiceClient {

    @GetMapping("/api/v1/report")
    List<TrainingReport> getAll(@RequestHeader("Authorization") String token);

    @PostMapping("/api/v1/report")
    @CircuitBreaker(name = "myCircuitBreaker", fallbackMethod = "fallbackMethod")
    void postReport(@Valid @RequestBody ReportRequest request, @RequestHeader("TransactionId") String transactionId);

    default void fallbackMethod(Throwable t) {
    }
}
