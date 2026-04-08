package ru.alinacozy.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.alinacozy.NauJava.entity.Report;
import ru.alinacozy.NauJava.entity.ReportStatus;
import ru.alinacozy.NauJava.service.ReportService;

import java.util.Map;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Long>> createReport() {
        Long reportId = reportService.createReport();
        return ResponseEntity.ok(Map.of("reportId", reportId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getReport(@PathVariable Long id) {
        Report report = reportService.getReport(id);

        if (report.getStatus() == ReportStatus.CREATED) {
            return ResponseEntity.ok("Отчет еще формируется, попробуйте позже...");
        }

        if (report.getStatus() == ReportStatus.ERROR) {
            return ResponseEntity.status(500).body("Ошибка при формировании отчета");
        }

        return ResponseEntity.ok(report.getContent());
    }
}
