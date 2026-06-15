package com.moviegoer.backend.accounts;

public record AuthenticationRequestDto(
    String username,
    String password
) {

}
