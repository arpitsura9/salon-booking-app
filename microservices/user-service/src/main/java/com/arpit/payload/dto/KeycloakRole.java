package com.arpit.payload.dto;

import lombok.Data;

import java.util.Map;
import java.util.Objects;

@Data
public class KeycloakRole {
    private String id;
    private String name;
    private String description;
    private boolean composite;
    private boolean clientRole;
    private String containerId;
    private Map<String , Object> attributes;
}
