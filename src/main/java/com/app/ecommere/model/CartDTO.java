package com.app.ecommere.model;

import lombok.Data;

@Data
public class CartDTO {

    private String productName;
    private int quantity;
    private float amount;
}
