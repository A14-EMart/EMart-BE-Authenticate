package com.a14.emart.auth.dto;

import com.a14.emart.auth.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserProfile {
    private Long id;
    private String username;
    private String email;
    private UserRole role;
}
