package com.moviegoer.backend.movies;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class TmdbClientService {

    private final RestClient restClient;

    @Value("${tmdb.key}")
    private String key;

    public TmdbClientService() {
        this.restClient = RestClient.builder()
            .baseUrl("https://api.themoviedb.org/3")
            .defaultHeader("Authorization", "Bearer " + key)
            .build();
    }

    public TmdbMovieDto fetchTmdbPopularMovies() {
        return restClient.get()
            .uri("/movie/polular?language=ko-KR")
            .header("Authorization", "Bearer " + key)
            .retrieve()
            .body(TmdbMovieDto.class);
    }
}
