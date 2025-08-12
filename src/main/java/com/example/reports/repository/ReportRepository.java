package com.example.reports.repository;

import com.example.reports.entity.Report;
import com.example.reports.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByUser(User user);
}