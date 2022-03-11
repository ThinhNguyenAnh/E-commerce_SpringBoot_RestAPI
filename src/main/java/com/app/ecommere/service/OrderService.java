package com.app.ecommere.service;

import com.app.ecommere.entity.*;
import com.app.ecommere.exception.ResourceNotFoundException;
import com.app.ecommere.model.OrderDTO;
import com.app.ecommere.payload.response.OrderResponse;
import com.app.ecommere.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

    public OrderResponse createOrder(Integer productId, Integer userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Cart cart = cartRepository.findByProductAndUser(product, user);


        Orders newOrders = new Orders();
        newOrders.setStatus(1);
        newOrders.setUser(user);
        orderRepository.save(newOrders);

        OrderDetail orderDetail = OrderDetail.builder()
                .total(product.getPrice() * cart.getQuantity())
                .price(product.getPrice())
                .quantity(cart.getQuantity())
                .orders(newOrders)
                .product(product)
                .build();

        orderDetailRepository.save(orderDetail);
        cartRepository.removeCart(product.getId(), user.getId());
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setProducts(Arrays.asList(mapToDTO(orderDetail)));
        orderResponse.setTotal(orderDetail.getTotal());

        return orderResponse;
    }


    public OrderResponse getAllOrderByUserId(Integer userId){
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrdersUserId(userId);

        List<OrderDTO> orderDTOS = orderDetails.stream().map(this::mapToDTO).collect(Collectors.toList());

        OrderResponse orderResponse = new OrderResponse();
        float total = 0 ;
        for( OrderDTO temp :orderDTOS){
             total += temp.getPrice()* temp.getQuantity();
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
