package com.moviegoer.backend.articles;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ArticleRequestDto(
    @NotBlank String content,
    @NotNull Long authorId
) {
    
}
