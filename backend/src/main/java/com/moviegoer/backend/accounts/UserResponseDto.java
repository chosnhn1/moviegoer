package com.moviegoer.backend.accounts;

import java.time.LocalDateTime;

public record UserResponseDto(
    Long id,
    String username,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    // 기존의 Util을 대체하는 Entity-to-DTO 메서드
    public static UserResponseDto toDto(User user) {
        return new UserResponseDto(
            user.getId(),
            user.getUsername(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}
