package com.app.ecommere.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthResponse {

    private String accessToken;
    private final String tokenType = "Bearer";

    public JwtAuthResponse(String accessToken){
        this.accessToken = accessToken;
    }
}
