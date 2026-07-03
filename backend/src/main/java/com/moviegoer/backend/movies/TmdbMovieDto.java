package com.moviegoer.backend.movies;

import java.util.List;

public record TmdbMovieDto(
    Long id,
    String title,
    String original_title,
    String original_language,
    List<Long> genre_ids,
    String overview, 
    Boolean adult,
    String poster_path
) {
    public static Movie toEntity(TmdbMovieDto dto) {
        return Movie.builder()
            .tmdb_id(dto.id)
            .title(dto.title)
            .original_title(dto.original_title)
            .original_language(dto.original_language)
            .overview(dto.overview)
            .poster_path(dto.poster_path)
            .isAdult(dto.adult)
            .build();
    }
}
