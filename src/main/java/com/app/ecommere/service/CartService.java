package com.app.ecommere.service;

import com.app.ecommere.entity.Cart;
import com.app.ecommere.entity.Product;
import com.app.ecommere.entity.User;
import com.app.ecommere.exception.ResourceNotFoundException;
import com.app.ecommere.model.CartDTO;
import com.app.ecommere.repository.CartRepository;
import com.app.ecommere.repository.ProductRepository;
import com.app.ecommere.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public List<CartDTO> getAllCart(Integer id) {

        List<Cart> cart = cartRepository.findAllByUserId(id);

        return cart.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public Integer addProductToCart(Integer productId, Integer quantity, Integer customerId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        User user = userRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", customerId));

        Cart cart = cartRepository.findByProductAndUser(product, user);

        if (cart != null) {
            Integer updatedQuantity = cart.getQuantity() + quantity;
            cart.setQuantity(updatedQuantity);
        } else {
            cart = new Cart();
            cart.setProduct(product);
            cart.setUser(user);
            cart.setQuantity(quantity);
        }

        float amount = cart.getQuantity() * product.getPrice();
        cart.setAmount(amount);
        cartRepository.save(cart);

        return quantity;
    }

    public void updateCartQuantity(Integer productId, Integer quantity, Integer customerID) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        User user = userRepository.findById(customerID)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", customerID));

        Cart cart = cartRepository.findByProductAndUser(product, user);
        if (cart != null) {
            cartRepository.updateQuantity(productId, quantity, customerID);
        }
    }

    public void removeCart(Integer productId, Integer customerId) {
        cartRepository.removeCart(productId, customerId);
    }

    public CartDTO mapToDto(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setProductName(cart.getProduct().getName());
        cartDTO.setQuantity(cart.getQuantity());
        cartDTO.setAmount(cart.getAmount());
        return cartDTO;
    }
}
