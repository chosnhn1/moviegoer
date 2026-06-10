package com.moviegoer.backend.articles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.moviegoer.backend.accounts.MoviegoerUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Controller
@RestController("/api/v1/article")
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/{articleId}")
    @Operation(summary = "게시글 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "게시글 없음"),
    })
    public ResponseEntity<ArticleResponseDto> getArticle(@PathVariable Long articleId) {
        ArticleResponseDto article = articleService.getArticleById(articleId);
        return ResponseEntity.ok(article);
    }

    @PostMapping
    @Operation(summary = "게시글 작성")
    @ApiResponses({
        @ApiResponse(),
        @ApiResponse(),
    })
    public ResponseEntity<ArticleResponseDto> createArticle(
        @AuthenticationPrincipal MoviegoerUserDetails userDetails,
        @RequestBody ArticleRequestDto article
    ) {
        ArticleResponseDto createdArticle = articleService.createArticle(
            article.content(),
            userDetails.getId()
        );
        return ResponseEntity.ok(createdArticle);
    }
}
