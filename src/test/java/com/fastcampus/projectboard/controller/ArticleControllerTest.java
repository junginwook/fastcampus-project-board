package com.fastcampus.projectboard.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@DisplayName("View 컨트롤러 - 게시글")
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

	private final MockMvc mvc;

	public ArticleControllerTest(@Autowired MockMvc mvc) {
		this.mvc = mvc;
	}

	@DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상 호출")
	@Test
	public void givenNothing_whenRequestingArticlesView_thenReturnsArticlesView() throws Exception {
		//Given

		//when & then
		mvc.perform(get("/articles"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
				.andExpect(view().name("articles/index"))
				//modelAttribute 확인
				.andExpect(model().attributeExists("articles"));
	}

	@Disabled
	@DisplayName("[view][GET] 게시글 상세 페이지 - 정상 호출")
	@Test
	public void givenNothing_whenRequestingArticleView_thenReturnsArticlesView() throws Exception {
		//Given

		//when & then
		mvc.perform(get("/articles/1"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
				.andExpect(view().name("articles/detail"))
				//modelAttribute 확인
				.andExpect(model().attributeExists("article"))
				.andExpect(model().attributeExists("articleComments"));
	}

	@Disabled
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
}
