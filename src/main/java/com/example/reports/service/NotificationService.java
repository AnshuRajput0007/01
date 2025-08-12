package com.example.reports.service;

import com.example.reports.entity.Report;
import com.example.reports.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public void notifyReportSolved(User recipient, Report report) {
        logger.info("Notification: Report {} solved. Notifying user {} <{}>", report.getId(), recipient.getName(), recipient.getEmail());
    }
}