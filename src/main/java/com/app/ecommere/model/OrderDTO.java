package com.app.ecommere.model;

import lombok.Data;

import java.util.Date;

@Data
public class OrderDTO {
    private String productName;
    private int quantity;
    private float price;
    private Date date;
}
