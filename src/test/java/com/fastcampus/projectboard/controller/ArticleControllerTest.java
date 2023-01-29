package com.fastcampus.projectboard.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fastcampus.projectboard.config.SecurityConfig;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.HashtagDto;
import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.service.ArticleService;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("View 컨트롤러 - 게시글")
@Disabled
@Import(SecurityConfig.class)
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

	private final MockMvc mvc;

	@MockBean private ArticleService articleService;

	public ArticleControllerTest(@Autowired MockMvc mvc) {
		this.mvc = mvc;
	}

	@DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상 호출")
	@Test
	public void givenNothing_whenRequestingArticlesView_thenReturnsArticlesView() throws Exception {
		//Given
		given(articleService.searchArticles(eq(null), eq(null), any(Pageable.class))).willReturn(Page.empty());

		//when & then
		mvc.perform(get("/articles"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
				.andExpect(view().name("articles/index"))
				//modelAttribute 확인
				.andExpect(model().attributeExists("articles"));

		then(articleService).should().searchArticles(eq(null), eq(null), any(Pageable.class));
	}
	//test
	@DisplayName("[view][GET] 게시글 상세 페이지 - 정상 호출")
	@Test
	public void givenNothing_whenRequestingArticleView_thenReturnsArticlesView() throws Exception {
		//Given
		Long articleId = 1L;
		given(articleService.getArticle(articleId)).willReturn(createArticleWithCommentsDto());

		//when & then
		mvc.perform(get("/articles/" + articleId))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
				.andExpect(view().name("articles/detail"))
				//modelAttribute 확인
				.andExpect(model().attributeExists("article"))
				.andExpect(model().attributeExists("articleComments"));

		then(articleService).should().getArticle(articleId);
	}

	@DisplayName("[view][GET] 게시글 검색 전용 페이지 - 정상 호출")
	@Test
	public void givenNothing_whenRequestingArticleSearchView_thenReturnsArticleSearchView() throws Exception {
		//Given

		//when & then
		mvc.perform(get("/articles/search"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
				.andExpect(view().name("articles/search"));
	}

	private ArticleDto createArticleDto() {
		return ArticleDto.of(
			createUserAccountDto(),
			"title",
			"content",
			Set.of(HashtagDto.of("java"))
		);
	}

	private ArticleDto createArticleWithCommentsDto() {
		return ArticleDto.of(
				1L,
				createUserAccountDto(),
				"title",
				"content",
				Set.of(HashtagDto.of("java")),
				LocalDateTime.now(),
				"inwook",
				LocalDateTime.now(),
				"inwook"
		);
	}

	private UserAccountDto createUserAccountDto() {
		return UserAccountDto.of(
			"inwook",
			"pw",
			"inwook@naver.com",
			"Inwook",
			"memo",
			LocalDateTime.now(),
			"inwook",
			LocalDateTime.now(),
			"inwook"
		);
	}
}
