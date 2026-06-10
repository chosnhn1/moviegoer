package com.moviegoer.backend.articles;

public record ArticleRequestDto(
    String content,
    Long authorId
) {
    
}
