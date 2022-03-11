package com.app.ecommere.model;

import com.app.ecommere.entity.Role;
import lombok.Data;

@Data
public class UserDTO {

    private Integer id;
    private String fullName;
    private String email;
    private String phone;
}
