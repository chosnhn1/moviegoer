package com.moviegoer.backend.articles;

import jakarta.validation.constraints.NotBlank;

public record ArticleRequestDto(
    @NotBlank String title,
    @NotBlank String content
) {
    
}
