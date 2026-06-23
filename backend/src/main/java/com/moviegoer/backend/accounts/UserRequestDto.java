package com.moviegoer.backend.accounts;

import jakarta.validation.constraints.NotBlank;

public record UserRequestDto(

    @NotBlank
    String username,

    @NotBlank
    String password
) {
}
