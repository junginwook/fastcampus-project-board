package com.fastcampus.projectboard.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.type.SearchType;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.ArticleUpdateDto;
import com.fastcampus.projectboard.repository.ArticleRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

	@InjectMocks //아직 생성자 주입은 할 수 없다.
	private ArticleService sut; //테스트 대상

	@Mock
	private ArticleRepository articleRepository;

	/*
		검색
		각 게시글 페이지로 이동
		페이지네이션
		홈 버튼 -> 게시판 페이지로 리다이렉션
		정렬 기능
	 */
	@DisplayName("게시글을 검색하면, 게시글 리스트를 반환한다.")
	@Test
	void givenSearchParameters_whenSearchingArticles_thenReturnsArticles() {

		//When
		Page<ArticleDto> articles = sut.searchArticles(SearchType.TITLE, "search keyword"); //제목, 본문, ID, 닉네임, 해시태그

		//Then
		Assertions.assertThat(articles).isNotNull();
	}

	@DisplayName("게시글을 조회하면, 게시글을 반환한다.")
	@Test
	void givenArticleId_whenSearchingArticle_thenReturnsArticle() {

		//When
		ArticleDto article = sut.searchArticle(1L);
		//Then
		Assertions.assertThat(article).isNotNull();
	}

	@DisplayName("게시글 정보를 입력하면, 게시글을 생성한다")
	@Test
	void givenArticleInfo_whenSavingArticle_thenSavesArticle() {
		//Given
		given(articleRepository.save(any(Article.class))).willReturn(null);

		//When
		sut.saveArticle(ArticleDto.of(LocalDateTime.now(), "inwook", "title", "content", "#hashtag"));

		//Then
		//save메서드가 호출되었는지 검사
		then(articleRepository).should().save(any(Article.class));
	}

	@DisplayName("게시글의 ID와 수정 정보를 입력하면, 게시글을 수정한다")
	@Test
	void givenArticleIdAndModifiedInfo_whenUpdatingArticle_thenUpdatesArticle() {
		//Given
		given(articleRepository.save(any(Article.class))).willReturn(null);

		//When
		sut.updateArticle(1L, ArticleUpdateDto.of("title", "content", "#hashtag"));

		//Then
		//save메서드가 호출되었는지 검사
		then(articleRepository).should().save(any(Article.class));
	}

	@DisplayName("게시글의 ID와 수정 정보를 입력하면, 게시글을 삭제한다")
	@Test
	void givenArticleIdAndModifiedInfo_whenDeletingArticle_thenDeletesArticle() {
		//Given 코드적으로 메서드가 호출된다 명시만
		willDoNothing().given(articleRepository).delete(any(Article.class));

		//When
		sut.deleteArticle(1L);

		//Then
		//save메서드가 호출되었는지 검사
		then(articleRepository).should().delete(any(Article.class));
	}
}