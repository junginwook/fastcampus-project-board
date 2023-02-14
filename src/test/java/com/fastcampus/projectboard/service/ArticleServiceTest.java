package com.fastcampus.projectboard.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.Hashtag;
import com.fastcampus.projectboard.domain.UserAccount;
import com.fastcampus.projectboard.domain.constant.SearchType;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.HashtagDto;
import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.repository.ArticleRepository;
import com.fastcampus.projectboard.repository.HashtagRepository;
import com.fastcampus.projectboard.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

	@DisplayName("검색어 없이 게시글을 해시태그 검색하면, 빈 페이지를 반환한다.")
	@Test
	void givenNoSearchParameters_whenSearchingArticlesViaHashtag_thenReturnsEmptyPage() {
		//Given
		Pageable pageable = Pageable.ofSize(20);

		Page<ArticleDto> articles = sut.searchArticlesViaHashtag(null, pageable);

		assertThat(articles).isEqualTo(Page.empty(pageable));
		then(articleRepository).shouldHaveNoInteractions();
	}

	@DisplayName("없는 해시태그를 검색하면, 빈 페이지를 반환한다.")
	@Test
	void givenNonexistentHashtag_whenSearchingArticlesViaHashtag_thenReturnsEmptyPage() {
		//Given
		String hashtagName = "난 없지롱";
		Pageable pageable = Pageable.ofSize(20);
		given(articleRepository.findByHashtagNames(List.of(hashtagName), pageable)).willReturn(new PageImpl<>(List.of(), pageable, 0));

		//When
		Page<ArticleDto> articles = sut.searchArticlesViaHashtag(hashtagName, pageable);

		//Then
		assertThat(articles).isEqualTo(Page.empty(pageable));
		then(articleRepository).should().findByHashtagNames(List.of(hashtagName), pageable);
	}

	@DisplayName("게시글을 해시태그 검색하면, 게시글 페이지를 반환한다.")
	@Test
	void givenHashtag_whenSearchingArticlesViaHashtag_thenReturnsArticlesPage() {
		//Given
		String hashtag = "#java";
		Pageable pageable = Pageable.ofSize(20);
		given(articleRepository.findByHashtag(hashtag, pageable)).willReturn(Page.empty(pageable));

		Page<ArticleDto> articles = sut.searchArticlesViaHashtag(hashtag, pageable);

		assertThat(articles).isEqualTo(Page.empty(pageable));
		then(articleRepository).should().findByHashtag(hashtag, pageable);
	}

	@DisplayName("댓글 달린 게시글이 없으면, 예외를 던진다.")
	@Test
	void givenArticleInfo_whenSavingArticle_thenSavesArticle() {
		//Given
		Long articleId = 0L;
		given(articleRepository.findById(articleId)).willReturn(Optional.empty());

		//When
		Throwable t =
				catchThrowable(() -> sut.getArticleWithComments(articleId));

		//Then
		assertThat(t)
				.isInstanceOf(EntityNotFoundException.class)
				.hasMessage("게시글이 없습니다 - articleId: " + articleId);
		then(articleRepository).should().findById(articleId);
	}

	@DisplayName("게시글을 조회하면, 게시글을 반환한다.")
	@Test
	void givenArticleId_whenSearchingArticle_thenReturnsArticle() {
		//Given
		Long articleId = 1L;
		Article article = createArticle();
		given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

		//When
		ArticleDto dto = sut.getArticle(articleId);

		//Then
		assertThat(dto)
				.hasFieldOrPropertyWithValue("title", article.getTitle())
				.hasFieldOrPropertyWithValue("content", article.getContent())
				.hasFieldOrPropertyWithValue("hashtagDtos", article.getHashtags().stream().map(HashtagDto::from).collect(Collectors.toSet()));
		then(articleRepository).should().findById(articleId);
	}

	@DisplayName("게시글이 없으면 예외를 던진다.")
	@Test
	void givenNonexistentArticleId_whenSearchingArticle_thenThrowsException() {
		//Given
		Long articleId = 0L;
		given(articleRepository.findById(articleId)).willReturn(Optional.empty());

		//When
		Throwable throwable = catchThrowable(() -> sut.getArticle(articleId));

		//Then
		assertThat(throwable)
				.isInstanceOf(EntityNotFoundException.class)
				.hasMessage("게시글이 없습니다. - articleId" + articleId);
		then(articleRepository).should().findById(articleId);
	}

	@DisplayName("게시글 정보를 입력하면, 본문에서 해시태그 정보를 추출하여 해시태그 정보가 포함된 게시글을 생성한다.")
	@Test
	void givenArticleIdAndModifiedInfo_whenUpdatingArticle_thenUpdatesArticle() {
		//Given
		ArticleDto dto = createArticleDto();
		Set<String> expectedHashtagNames = Set.of("java", "spring");
		Set<Hashtag> expectedHashtags = new HashSet<>();
		expectedHashtags.add(createHashtag("java"));

		given(userAccountRepository.getReferenceById(dto.userAccountDto().userId())).willReturn(createUserAccount());
		given(hashtagService.parseHashtagNames(dto.content())).willReturn(expectedHashtagNames);
		given(hashtagService.findHashtagByNames(expectedHashtagNames)).willReturn(expectedHashtags);
		given(articleRepository.save(any(Article.class))).willReturn(createArticle());

		//When
		sut.saveArticle(dto);

		//Then
		then(userAccountRepository).should().getReferenceById(dto.userAccountDto().userId());
		then(hashtagService).should().parseHashtagNames(dto.content());
		then(hashtagService).should().findHashtagByNames(expectedHashtagNames);
		then(articleRepository).should().save(any(Article.class));
	}

	@DisplayName("게시글의 수정 정보를 입력하면, 게시글을 삭제한다")
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