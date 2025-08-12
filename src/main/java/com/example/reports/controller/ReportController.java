package com.example.reports.controller;

import com.example.reports.dto.ReportDtos.ReportRequest;
import com.example.reports.dto.ReportDtos.ReportResponse;
import com.example.reports.entity.User;
import com.example.reports.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public ResponseEntity<List<ReportResponse>> list(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(reportService.listReports(currentUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportResponse> get(@AuthenticationPrincipal User currentUser, @PathVariable Long id) {
        return ResponseEntity.ok(reportService.getReport(currentUser, id));
    }

    @PostMapping
    public ResponseEntity<ReportResponse> create(@AuthenticationPrincipal User currentUser, @Valid @RequestBody ReportRequest request) {
        return ResponseEntity.ok(reportService.createReport(currentUser, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReportResponse> update(@AuthenticationPrincipal User currentUser, @PathVariable Long id, @Valid @RequestBody ReportRequest request) {
        return ResponseEntity.ok(reportService.updateReport(currentUser, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal User currentUser, @PathVariable Long id) {
        reportService.deleteReport(currentUser, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/solve")
    public ResponseEntity<Void> solve(@AuthenticationPrincipal User currentUser, @PathVariable Long id) {
        reportService.solveReport(currentUser, id);
        return ResponseEntity.noContent().build();
    }
}