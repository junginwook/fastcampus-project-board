package com.fastcampus.projectboard.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("Data REST - API test")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class DataRestTest {

	private final MockMvc mvc;

	public DataRestTest(@Autowired MockMvc mvc) {
		this.mvc = mvc;
	}

	@DisplayName("[api] 게시글 리스트 조회")
	@Test
	void givenNothing_whenRequestingArticles_thenReturnsArticlesJsonResponse() throws Exception {

		// When & Then
		mvc.perform(get("/api/articles"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
	}

	@DisplayName("[api] 게시글 단건 조회")
	@Test
	void givenNothing_whenRequestingArticles_thenReturnsArticleJsonResponse() throws Exception {

		// When & Then
		mvc.perform(get("/api/articles/1"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
	}

	@DisplayName("[api] 게시글 -> 댓글 리스트 조회")
	@Test
	void givenNothing_whenRequestingArticleCommentFromArticle_thenReturnsArticleCommentsJsonResponse() throws Exception {

		// When & Then
		mvc.perform(get("/api/articles/1/articleComments"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
	}

	@DisplayName("[api] 댓글 리스트 조회")
	@Test
	void givenNothing_whenRequestingArticleComments_thenReturnsArticleCommentsJsonResponse() throws Exception {

		// When & Then
		mvc.perform(get("/api/articleComments"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
	}

	@DisplayName("[api] 댓글 단건 조회")
	@Test
	void givenNothing_whenRequestingArticleComment_thenReturnsArticleCommentJsonResponse() throws Exception {

		// When & Then
		mvc.perform(get("/api/articleComments/1"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
	}
}
