package com.fastcampus.projectboard.controller;

import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fastcampus.projectboard.config.SecurityConfig;
import com.fastcampus.projectboard.config.TestSecurityConfig;
import com.fastcampus.projectboard.service.ArticleService;
import com.fastcampus.projectboard.service.PaginationService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("View 컨트롤러 - 인증")
@Disabled
@Import(TestSecurityConfig.class)
@WebMvcTest(AuthControllerTest.EmptyController.class)
public class AuthControllerTest {

	private final MockMvc mvc;

	@MockBean private ArticleService articleService;

	@MockBean private PaginationService paginationService;

	public AuthControllerTest(@Autowired MockMvc mvc) {
		this.mvc = mvc;
	}

	@DisplayName("[view][GET] 로그인 페이지 - 정상 호출")
	@Test
	public void givenNothing_whenTryingToLogIn_thenReturnsLoginView() throws Exception {
		//Given

		//when & then
		mvc.perform(get("/login"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
		then(articleService).shouldHaveNoInteractions();
		then(paginationService).shouldHaveNoInteractions();
	}

	/**
	 * 어떤 컨트롤러도 필요하지 않은 테스트임을 나타내기 위해 테스트용 빈 컴포넌트를 사용함.
	 */
	@TestComponent
	static class EmptyController {}
}
