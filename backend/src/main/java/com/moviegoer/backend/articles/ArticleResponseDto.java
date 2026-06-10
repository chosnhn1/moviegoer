package com.moviegoer.backend.articles;

import java.time.LocalDateTime;

public record ArticleResponseDto(
    Long articleId,
    String content,
    Long authorId,
    String authorName,
    LocalDateTime createdAt
) {

    public static ArticleResponseDto toDto(Article article) {
        return new ArticleResponseDto(
            article.getId(),
            article.getContent(),
            article.getAuthor().getId(),
            article.getAuthor().getUsername(),
            article.getCreatedAt()
        );
    }
}
