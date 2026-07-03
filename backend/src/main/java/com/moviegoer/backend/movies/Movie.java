package com.moviegoer.backend.movies;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long tmdb_id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String original_title;
    
    @Column(nullable = false)
    private String original_language;
    
    @Column(nullable = false)
    private Long popularity;
    
    @Column(nullable = false)
    private LocalDate release_date;
    
    @Column(nullable = false)
    private Boolean isAdult;
    
    @Column(nullable = false)
    private String overview;
    
    @Column(nullable = false)
    private String poster_path;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "genre_id", nullable = true)
    private Genre genre;

    // public static class MovieBuilder {
    //     private Long id;
    //     private Long tmdbId;
    //     private String title;
    //     private String original_title;
    //     private String original_language;
    //     private Long popularity;
    //     private String release_date;
    //     private String overview;
    //     private String poster_path;

    //     public MovieBuilder id(Long id)

    // }

}
