package ru.alinacozy.NauJava.service;

import ru.alinacozy.NauJava.entity.Report;

import java.util.concurrent.CompletableFuture;

public interface ReportService {
    Report getReport(Long id);
    Long createReport();
    CompletableFuture<Void> generateReportAsync(Long reportId);
}
