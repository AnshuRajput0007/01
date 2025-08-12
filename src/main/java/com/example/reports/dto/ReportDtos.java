package com.example.reports.dto;

import com.example.reports.entity.ReportStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

public class ReportDtos {

    public record ReportRequest(
            @NotBlank String problemType,
            @Size(max = 2000) String description,
            String photoUrl,
            @NotNull Double latitude,
            @NotNull Double longitude,
            ReportStatus status
    ) {}

    public record ReportResponse(
            Long id,
            Long userId,
            String problemType,
            String description,
            String photoUrl,
            Double latitude,
            Double longitude,
            ReportStatus status,
            OffsetDateTime createdAt
    ) {}
}