package com.moviegoer.backend.articles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moviegoer.backend.accounts.MoviegoerUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/articles")
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    @Operation(summary = "게시글 전체 조회 (페이지)")
    public ResponseEntity<Page<ArticleResponseDto>> getAllArticlesPagenated(
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        var articlePage = articleService.getAllArticlePage(page, size);
        return ResponseEntity.ok(articlePage);
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
        @ApiResponse(responseCode = "200", description = "성공")
    })
    public ResponseEntity<ArticleResponseDto> createArticle(
        @AuthenticationPrincipal MoviegoerUserDetails userDetails,
        @RequestBody ArticleRequestDto article
    ) {
        ArticleResponseDto createdArticle = articleService.createArticle(
            article.title(),
            article.content(),
            userDetails.getId()
        );
        return ResponseEntity.ok(createdArticle);
    }

    @PatchMapping("/{articleId}")
    @Operation(summary = "게시글 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "게시글 없음"),
    })
    public ResponseEntity<ArticleResponseDto> updateArticle(
        @AuthenticationPrincipal MoviegoerUserDetails userDetails,
        @PathVariable Long articleId,
        @RequestBody ArticleRequestDto article
    ) {
        ArticleResponseDto updatedArticle = articleService.updateArticle(
            userDetails.getId(),
            articleId,
            article.title(),
            article.content()
        );
        return ResponseEntity.ok(updatedArticle);
    }

    @DeleteMapping("/{articleId}")
    @Operation(summary = "게시글 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "성공"),
        @ApiResponse(responseCode = "404", description = "게시글 없음"),
    })
    public ResponseEntity<Void> deleteArticle(
        @AuthenticationPrincipal MoviegoerUserDetails userDetails,
        @PathVariable Long articleId
    ) {
        articleService.deleteArticle(userDetails.getId(), articleId);
        return ResponseEntity.noContent().build();
    }
}
