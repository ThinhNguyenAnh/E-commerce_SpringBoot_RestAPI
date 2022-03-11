package com.app.ecommere.controller;

import com.app.ecommere.entity.User;
import com.app.ecommere.model.CartDTO;
import com.app.ecommere.repository.UserRepository;
import com.app.ecommere.security.CustomUserDetail;
import com.app.ecommere.service.CartService;
import com.app.ecommere.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<CartDTO>> getCart(@AuthenticationPrincipal CustomUserDetail userDetails){
        List<CartDTO> body = cartService.getAllCart(userDetails.getUserID());

        return ResponseEntity.ok(body);
    }



    @PostMapping("/add/{productId}/{quantity}")
    public ResponseEntity<?> addProductToCart(@AuthenticationPrincipal CustomUserDetail userDetails,
                                              @PathVariable("productId") Integer productId,
                                              @PathVariable("quantity") Integer quantity) {

//        Optional<User>user = userRepository.findById(userDetails.getUserID());

        Integer result = cartService.addProductToCart(productId, quantity, userDetails.getUserID());

        return ResponseEntity.ok(result + " item(s) of this product have add to cart successfully");


    }

    @PutMapping("/update/{productId}/{quantity}")
    public ResponseEntity<?> updateQuantity(@AuthenticationPrincipal CustomUserDetail userDetails, @PathVariable("productId") Integer productId,
                                            @PathVariable("quantity") Integer quantity) {


        cartService.updateCartQuantity(productId, quantity, userDetails.getUserID());


        return  ResponseEntity.ok("Updated successfully");

    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeCart(@AuthenticationPrincipal CustomUserDetail userDetails, @PathVariable("productId") Integer productId) {

        cartService.removeCart(productId, userDetails.getUserID());


        return new ResponseEntity<>("Deleted product int cart successfully", HttpStatus.OK);

    }
}
