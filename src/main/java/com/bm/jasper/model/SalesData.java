package com.bm.jasper.model;

import lombok.Data;

@Data
public class SalesData {
    private String category;
    private Double amount;

    public SalesData(String category, Double amount) {
        this.category = category;
        this.amount = amount;
    }
} 