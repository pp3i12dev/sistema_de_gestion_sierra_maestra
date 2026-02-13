package com.sca.controller;

import com.sca.service.BarrilReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports/barriles")
public class BarrilReportController {

    @Autowired
    private BarrilReportService reportService;

    @GetMapping("/total")
    public long getTotalBarriles() {
        return reportService.totalBarriles();
    }

    @GetMapping("/check-threshold")
    public String checkThreshold(@RequestParam int threshold) {
        boolean stockBajo = reportService.isStockBelowThreshold(threshold);
        if (stockBajo) {
            return "⚠️ Stock de barriles por debajo del umbral (" + threshold + ")";
        } else {
            return "✅ Stock suficiente, umbral: " + threshold;
        }
    }
}
