package com.moviegoer.backend.movies;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TmdbMovieResponseDto(
    Long id,
    String title,
    @JsonProperty("original_title") String originalTitle,
    @JsonProperty("original_language") String originalLanguage,
    String overview,
    Boolean adult,
    @JsonProperty("poster_path") String posterPath,
    List<TmdbGenreDto> genres
) {
    public static Movie toEntity(TmdbMovieResponseDto dto) {
        return Movie.builder()
            .tmdb_id(dto.id)
            .title(dto.title)
            .original_title(dto.originalTitle)
            .original_language(dto.originalLanguage)
            .overview(dto.overview)
            .poster_path(dto.posterPath)
            .isAdult(dto.adult)
            .build();
    }

    public record TmdbGenreDto(
        Long id,
        String name
    ) {
    }
}
