package com.saghar.marketplace.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AuthDtos {
    public record LoginRequest(
            @NotBlank String usernameOrEmail,
            @NotBlank String password
    ) {}

    public record RegisterRequest(
            @NotNull Role role,
            @NotBlank String firstName,
            @NotBlank String lastName,
            String username, // for manager/supporter
            String email,
            String phone,
            String storeName,
            String city,
            String nationalId,
            String storeCode,
            Long parentManagerId,
            @NotBlank String password
    ) {}

    public enum Role { CUSTOMER, SELLER, MANAGER, SUPPORT }

    public record UserProfile(
            String key,
            Role role,
            String displayName
    ) {}

    public record AuthResponse(
            String accessToken,
            String tokenType,
            long expiresInSeconds,
            UserProfile user
    ) {}
}
