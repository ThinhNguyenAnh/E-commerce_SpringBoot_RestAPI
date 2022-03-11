package com.app.ecommere.payload.response;

import com.app.ecommere.model.OrderDTO;
import lombok.Data;

import java.util.List;

@Data
public class OrderResponse {

    private List<OrderDTO> products;
    private float total;
}
