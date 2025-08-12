package com.example.reports.service;

import com.example.reports.dto.ReportDtos.ReportRequest;
import com.example.reports.dto.ReportDtos.ReportResponse;
import com.example.reports.entity.Report;
import com.example.reports.entity.ReportStatus;
import com.example.reports.entity.Role;
import com.example.reports.entity.User;
import com.example.reports.repository.ReportRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final NotificationService notificationService;

    public ReportService(ReportRepository reportRepository, NotificationService notificationService) {
        this.reportRepository = reportRepository;
        this.notificationService = notificationService;
    }

    @Transactional(readOnly = true)
    public List<ReportResponse> listReports(User currentUser) {
        if (currentUser.getRole() == Role.AUTHORITY) {
            return reportRepository.findAll().stream().map(this::toResponse).toList();
        }
        return reportRepository.findByUser(currentUser).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ReportResponse getReport(User currentUser, Long id) {
        Report report = getOwnedOrPermittedReport(currentUser, id);
        return toResponse(report);
    }

    @Transactional
    public ReportResponse createReport(User currentUser, ReportRequest request) {
        Report report = Report.builder()
                .user(currentUser)
                .problemType(request.problemType())
                .description(request.description())
                .photoUrl(request.photoUrl())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .status(request.status() != null ? request.status() : ReportStatus.PENDING)
                .build();
        Report saved = reportRepository.save(report);
        return toResponse(saved);
    }

    @Transactional
    public ReportResponse updateReport(User currentUser, Long id, ReportRequest request) {
        Report report = getOwnedOrPermittedReport(currentUser, id);
        report.setProblemType(request.problemType());
        report.setDescription(request.description());
        report.setPhotoUrl(request.photoUrl());
        report.setLatitude(request.latitude());
        report.setLongitude(request.longitude());
        if (request.status() != null) {
            report.setStatus(request.status());
        }
        return toResponse(report);
    }

    @Transactional
    public void deleteReport(User currentUser, Long id) {
        Report report = getOwnedOrPermittedReport(currentUser, id);
        reportRepository.delete(report);
    }

    @Transactional
    public void solveReport(User currentUser, Long id) {
        if (currentUser.getRole() != Role.AUTHORITY) {
            throw new AccessDeniedException("Only authority can solve reports");
        }
        Report report = reportRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Report not found"));
        User owner = report.getUser();
        reportRepository.delete(report);
        notificationService.notifyReportSolved(owner, report);
    }

    private Report getOwnedOrPermittedReport(User currentUser, Long id) {
        Report report = reportRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Report not found"));
        if (currentUser.getRole() == Role.AUTHORITY) {
            return report;
        }
        if (!report.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Forbidden");
        }
        return report;
    }

    private ReportResponse toResponse(Report report) {
        return new ReportResponse(
                report.getId(),
                report.getUser().getId(),
                report.getProblemType(),
                report.getDescription(),
                report.getPhotoUrl(),
                report.getLatitude(),
                report.getLongitude(),
                report.getStatus(),
                report.getCreatedAt()
        );
    }
}