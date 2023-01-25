package com.fastcampus.projectboard.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.Hashtag;
import com.fastcampus.projectboard.domain.UserAccount;
import com.fastcampus.projectboard.domain.type.SearchType;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.ArticleUpdateDto;
import com.fastcampus.projectboard.dto.HashtagDto;
import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.repository.ArticleRepository;
import com.fastcampus.projectboard.repository.HashtagRepository;
import com.fastcampus.projectboard.repository.UserAccountRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

	@InjectMocks //아직 생성자 주입은 할 수 없다.
	private ArticleService sut; //테스트 대상

	@Mock private ArticleRepository articleRepository;
	@Mock private HashtagService hashtagService;
	@Mock private UserAccountRepository userAccountRepository;
	@Mock private HashtagRepository hashtagRepository;

	@DisplayName("검색어 없이 게시글을 검색하면, 게시글 페이지를 반환한다.")
	@Test
	void givenNoSearchParameters_whenSearchingArticles_thenReturnsArticlePage() {
		//Given
		Pageable pageable = Pageable.ofSize(20);
		given(articleRepository.findAll(pageable)).willReturn(Page.empty());

		//When
		Page<ArticleDto> articles = sut.searchArticles(null, null, pageable);

		//Then
		assertThat(articles).isEmpty();
		then(articleRepository).should().findAll(pageable);
	}

	@DisplayName("검색어와 함께 게시글을 검색하면, 게시글 페이지를 반환한다.")
	@Test
	void givenSearchParameters_whenSearchingArticles_thenReturnsArticlePage() {
		//Given
		SearchType searchType = SearchType.TITLE;
		String searchKeyword = "title";
		Pageable pageable = Pageable.ofSize(20);
		given(articleRepository.findByTitleContaining(searchKeyword, pageable)).willReturn(Page.empty());

		//When
		Page<ArticleDto> articles = sut.searchArticles(searchType, searchKeyword, pageable);

		//Then
		assertThat(articles).isEmpty();
		then(articleRepository).should().findByTitleContaining(searchKeyword, pageable);
	}

	@DisplayName("없는 해시태그를 검색하면, 빈 페이지를 반환한다.")
	@Test
	void givenNonexistentHashtag_whenSearchingArticlesViaHashtag_thenReturnsEmptyPage() {

	}

	@DisplayName("게시글 정보를 입력하면, 게시글을 생성한다")
	@Test
	void givenArticleInfo_whenSavingArticle_thenSavesArticle() {

	}

	@DisplayName("게시글의 ID와 수정 정보를 입력하면, 게시글을 수정한다")
	@Test
	void givenArticleIdAndModifiedInfo_whenUpdatingArticle_thenUpdatesArticle() {

	}

	@DisplayName("게시글의 ID와 수정 정보를 입력하면, 게시글을 삭제한다")
	@Test
	void givenArticleIdAndModifiedInfo_whenDeletingArticle_thenDeletesArticle() {

	}

	private UserAccount createUserAccount() {
		return createUserAccount("inwook");
	}

	private UserAccount createUserAccount(String userId) {
		return UserAccount.of(
				userId,
				"password",
				"inwook@email.com",
				"Uno",
				null
		);
	}

	private Article createArticle() {
		return createArticle(1L);
	}

	private Article createArticle(Long id) {
		Article article = Article.of(
				createUserAccount(),
				"title",
				"content"
		);

		article.addHashtags(Set.of(

		));

		ReflectionTestUtils.setField(article,"id", id);

		return article;
	}

	private Hashtag createHashtag(String hashtagName) {
		return createHashtag(1L, hashtagName);
	}
	private Hashtag createHashtag(Long id, String hashtagName) {
		Hashtag hashtag = Hashtag.of(hashtagName);
		ReflectionTestUtils.setField(hashtag, "id", id);

		return hashtag;
	}

	private HashtagDto createHashtagDto() {
		return HashtagDto.of("java");
	}

	private ArticleDto createArticleDto() {
		return createArticleDto("title", "content");
	}

	private ArticleDto createArticleDto(String title, String content) {
		return ArticleDto.of(
			1L,
			createUserAccountDto(),
			title,
			content,
			null,
			LocalDateTime.now(),
			"Uno",
			LocalDateTime.now(),
			"Uno"
		);
	}

	private UserAccountDto createUserAccountDto() {
		return UserAccountDto.of(
				"uno",
				"password",
				"uno@email.com",
				"Uno",
				"memo",
				LocalDateTime.now(),
				"uno",
				LocalDateTime.now(),
				"uno"
		);
	}
}