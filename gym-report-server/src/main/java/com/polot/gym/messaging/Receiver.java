package com.polot.gym.messaging;

import com.polot.gym.payload.request.ReportRequest;
import com.polot.gym.service.TrainingReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class Receiver {
    private final TrainingReportService trainingReportService;
    private Logger log = LoggerFactory.getLogger(Receiver.class);

    public Receiver(TrainingReportService trainingReportService) {
        this.trainingReportService = trainingReportService;
    }

    @JmsListener(destination = "gym.report", containerFactory = "myFactory")
    public void receiveMessage(@Payload ReportRequest request,
                               @Header String transactionId) {
        log.info("Message received: {}, TransactionId: {}", request, transactionId);
        trainingReportService.postReport(request);
    }
}
