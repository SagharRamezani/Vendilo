package com.saghar.marketplace.api.dto;

import jakarta.validation.constraints.NotNull;

public class ManagerDtos {
    public record BlockUserRequest(@NotNull Long targetUserId) {}
    public record BlockUserResponse(long blockedUserId, long blockedByManagerId, String status) {}
}
