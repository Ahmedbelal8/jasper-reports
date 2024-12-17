package com.bm.jasper.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    public record ReportFormat(String contentType, String extension) {}
    
    private static final Map<String, ReportFormat> SUPPORTED_FORMATS = Map.of(
        "pdf", new ReportFormat("application/pdf", "pdf"),
        "html", new ReportFormat("text/html", "html")
    );

    public byte[] exportReport(String reportFormat, List<?> dataList) throws FileNotFoundException, JRException {
        var format = SUPPORTED_FORMATS.get(reportFormat.toLowerCase());
        if (format == null) {
            throw new IllegalArgumentException("Unsupported format: " + reportFormat);
        }

        var file = ResourceUtils.getFile("classpath:reports/report.jrxml");
        var jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        var dataSource = new JRBeanCollectionDataSource(dataList);
        

        var parameters = new HashMap<String, Object>();
        parameters.put("createdBy", "Your Company Name");

        var jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        

        return switch (reportFormat.toLowerCase()) {
            case "pdf" -> JasperExportManager.exportReportToPdf(jasperPrint);
            case "html" -> JasperExportManager.exportReportToHtmlFile(String.valueOf(jasperPrint)).getBytes();
            default -> throw new IllegalArgumentException("Unsupported format: " + reportFormat);
        };
    }
} 