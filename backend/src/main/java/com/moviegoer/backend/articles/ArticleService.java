package com.moviegoer.backend.articles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.moviegoer.backend.accounts.User;
import com.moviegoer.backend.accounts.UserNotFoundException;
import com.moviegoer.backend.accounts.UserRepository;

@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    public ArticleResponseDto createArticle(String content, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(
                String.format("해당 아이디 (%d) 를 가진 사용자를 찾을 수 없습니다.", userId)
            ));

        Article article = Article.builder()
            .content(content)
            .author(user)
            .build();
        
        Article savedArticle = articleRepository.save(article);

        return ArticleResponseDto.toDto(savedArticle);
    }

    public ArticleResponseDto getArticleById(Long articleId) {
        Article article = articleRepository
            .findById(articleId)
            .orElseThrow(() -> new ArticleNotFoundException(
                String.format("해당 아이디 (%d) 를 가진 게시글을 찾을 수 없습니다.", articleId)
            ));

        return ArticleResponseDto.toDto(article);
    }


}
