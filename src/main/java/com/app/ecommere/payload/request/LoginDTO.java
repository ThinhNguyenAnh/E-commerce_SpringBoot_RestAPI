package com.app.ecommere.payload.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class LoginDTO {
    private String username;
    private String password;
}
