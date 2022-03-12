package com.app.ecommere.service;

import com.app.ecommere.entity.*;
import com.app.ecommere.exception.ResourceNotFoundException;
import com.app.ecommere.model.OrderDTO;
import com.app.ecommere.payload.response.OrderResponse;
import com.app.ecommere.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public OrderResponse createOrder(Integer userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        List<Cart> carts = cartRepository.findAll();
        Orders newOrders = new Orders();
        newOrders.setStatus(1);
        newOrders.setUser(user);
        orderRepository.save(newOrders);

        List<OrderDTO> body = new ArrayList<>();
        float total = 0;

        for (Cart tmp : carts) {
            OrderDetail orderDetail = OrderDetail.builder()
                    .total(tmp.getProduct().getPrice() * tmp.getQuantity())
                    .price(tmp.getProduct().getPrice())
                    .quantity(tmp.getQuantity())
                    .orders(newOrders)
                    .product(tmp.getProduct())
                    .build();
            body.add(this.mapToDTO(orderDetail));
            orderDetailRepository.save(orderDetail);
        }
        cartRepository.clearCart(user.getId());
        for (OrderDTO temp : body) {
            total += temp.getPrice() * temp.getQuantity();
        }

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setProducts(body);
        orderResponse.setTotal(total);

        return orderResponse;
    }


    public OrderResponse getAllOrderByUserId(Integer userId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrdersUserId(userId);

        List<OrderDTO> orderDTOS = orderDetails.stream().map(this::mapToDTO).collect(Collectors.toList());

        OrderResponse orderResponse = new OrderResponse();
        float total = 0;
        for (OrderDTO temp : orderDTOS) {
            total += temp.getPrice() * temp.getQuantity();
        }
        orderResponse.setProducts(orderDTOS);
        orderResponse.setTotal(total);

        return orderResponse;
    }

    public OrderDTO mapToDTO(OrderDetail order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setProductName(order.getProduct().getName());
        orderDTO.setPrice(order.getPrice());
        orderDTO.setQuantity(order.getQuantity());
        orderDTO.setDate(order.getOrders().getCreateTime());

        return orderDTO;
    }
}
