package com.app.ecommere.payload.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class CreateProductRequest {

    @NotEmpty(message = "Name should be included")
    private String name;
    @NotEmpty(message = "Description should be included")
    private String description;
    @NotEmpty(message = "Detail should be included")
    private String detail;
    private int quantity;
    private float price;
    private Integer categoryId;

}
