package com.app.ecommere.payload.request;

import com.app.ecommere.entity.Address;
import lombok.Data;

@Data
public class SignupDTO {
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private Address address;
}
