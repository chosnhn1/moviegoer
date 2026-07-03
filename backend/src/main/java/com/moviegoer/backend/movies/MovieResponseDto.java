package com.moviegoer.backend.movies;

public record MovieResponseDto(
    Long id,
    String original_title,
    String original_language,
    String title

) {

}
