package com.app.ecommere.controller;

import com.app.ecommere.entity.Address;
import com.app.ecommere.entity.Role;
import com.app.ecommere.entity.User;
import com.app.ecommere.payload.request.LoginDTO;
import com.app.ecommere.payload.request.SignupDTO;
import com.app.ecommere.payload.response.JwtAuthResponse;
import com.app.ecommere.repository.AddressRepository;
import com.app.ecommere.repository.RoleRepository;
import com.app.ecommere.repository.UserRepository;
import com.app.ecommere.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AddressRepository addressRepository;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/signin")
    public ResponseEntity<JwtAuthResponse> authenticateUser(@RequestBody LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JwtAuthResponse(token));
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupDTO signupDTO) {
        if (userRepository.existsByUsername(signupDTO.getUsername())) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        // add check for email exists in DB
        if (userRepository.existsByEmail(signupDTO.getEmail())) {
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }
        Role userRole = roleRepository.findByName("USER");

//        Address address = Address.builder()
//                .district(signupDTO.getAddress().getDistrict())
//                .street(signupDTO.getAddress().getStreet())
//                .ward(signupDTO.getAddress().getWard())
//                .build();

        User user = User.builder()
                .username(signupDTO.getUsername())
                .email(signupDTO.getEmail())
                .fullName(signupDTO.getFullName())
                .password(passwordEncoder.encode(signupDTO.getPassword()))
                .phone(signupDTO.getPhone())
                .role(userRole)
                .build();

        Address address = signupDTO.getAddress();
        address.add(user);

        addressRepository.save(address);


//        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);

    }
}
