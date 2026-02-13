package com.sca.controller;

import com.sca.service.LoteReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports/lotes")
public class LoteReportController {

    @Autowired
    private LoteReportService reportService;

    @GetMapping("/total")
    public long getTotalLotes() {
        return reportService.totalLotes();
    }

    @GetMapping("/check-threshold")
    public String checkThreshold(@RequestParam long threshold) {
        boolean stockBajo = reportService.isStockBelowThreshold(threshold);
        if (stockBajo) {
            return "⚠️ Stock de lotes terminados por debajo del umbral (" + threshold + ")";
        } else {
            return "✅ Stock suficiente de lotes terminados, umbral: " + threshold;
        }
    }
}