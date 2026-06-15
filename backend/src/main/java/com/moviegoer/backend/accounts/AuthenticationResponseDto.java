package com.moviegoer.backend.accounts;

import lombok.Builder;

@Builder
public record AuthenticationResponseDto(
    String jwt
) {

}
