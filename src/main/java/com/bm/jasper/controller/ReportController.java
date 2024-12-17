package com.bm.jasper.controller;

import com.bm.jasper.model.SalesData;
import com.bm.jasper.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/{format}")
    public ResponseEntity<byte[]> generateReport(@PathVariable String format) {
        try {

            List<SalesData> data = List.of(
                new SalesData("Electronics", 25000.0),
                new SalesData("Clothing", 15000.0),
                new SalesData("Books", 8000.0),
                new SalesData("Food", 12000.0),
                new SalesData("Sports", 10000.0)
            );
            
            byte[] reportContent = reportService.exportReport(format, data);
            
            HttpHeaders headers = new HttpHeaders();
            if ("pdf".equalsIgnoreCase(format)) {
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("filename", "sales-report.pdf");
            } else if ("html".equalsIgnoreCase(format)) {
                headers.setContentType(MediaType.TEXT_HTML);
            }
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(reportContent);
                    
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
} 