package com.arpit.payload.dto;

import lombok.Data;

@Data
public class KeycloakUserDTO {
    private String id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
}
