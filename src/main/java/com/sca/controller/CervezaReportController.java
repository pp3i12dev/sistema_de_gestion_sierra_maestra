package com.sca.controller;

import com.sca.dto.CervezaReportDTO;
import com.sca.service.CervezaReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports/cervezas")
public class CervezaReportController {

    @Autowired
    private CervezaReportService reportService;

    @GetMapping("/all")
    public List<CervezaReportDTO> getAllCervezasWithLitros() {
        return reportService.allCervezasWithLitros();
    }

    @GetMapping("/check-threshold")
    public String checkThreshold(@RequestParam long threshold) {
        boolean stockBajo = reportService.isStockBelowThreshold(threshold);
        if (stockBajo) {
            List<CervezaReportDTO> bajas = reportService.cervezasBelowThreshold(threshold);
            String nombres = bajas.stream()
                    .map(CervezaReportDTO::getNombreCerveza)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("Ninguna");
            return "⚠️ Stock de cerveza por debajo del umbral (" + threshold + " litros). Cervezas afectadas: " + nombres;
        } else {
            return "✅ Stock suficiente, umbral: " + threshold + " litros";
        }
    }
}