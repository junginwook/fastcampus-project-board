package com.fastcampus.projectboard.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.fastcampus.projectboard.config.SecurityConfig;
import com.fastcampus.projectboard.service.ArticleCommentsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("View 컨트럴로 - 댓글")
@WebMvcTest(ArticleCommentController.class)
class ArticleCommentControllerTest {

	private final MockMvc mvc;

	@MockBean private ArticleCommentsService articleCommentsService;
	
	public ArticleCommentControllerTest(@Autowired MockMvc mvc) {
		this.mvc = mvc;
	}

	@Test
	void name() {

	}
}