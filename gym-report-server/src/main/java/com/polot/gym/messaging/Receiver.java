package com.polot.gym.messaging;

import com.polot.gym.payload.request.ReportRequest;
import com.polot.gym.service.TrainingReportService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver{
    private final TrainingReportService trainingReportService;

    public Receiver(TrainingReportService trainingReportService) {
        this.trainingReportService = trainingReportService;
    }

    @JmsListener(destination = "gym.report", containerFactory = "myFactory")
    public void receiveMessage(ReportRequest request) {
        System.out.println("Received <" + request + ">");
        trainingReportService.postReport(request);
    }
}
