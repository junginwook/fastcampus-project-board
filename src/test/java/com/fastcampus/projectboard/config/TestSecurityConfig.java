package com.fastcampus.projectboard.config;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.fastcampus.projectboard.domain.UserAccount;
import com.fastcampus.projectboard.repository.UserAccountRepository;
import java.util.Optional;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

@Import(SecurityConfig.class)
public class TestSecurityConfig {

	//security 레이어에서만 사용되는 빈
	@MockBean private UserAccountRepository userAccountRepository;

	@BeforeTestMethod //스프링이 제공
	public void securitySetUp() {
		given(userAccountRepository.findById(anyString()))
				.willReturn(Optional.of(
						UserAccount.of(
								"inwookTest",
								"pw",
								"inwook@test.com",
								"inwook-test",
								"memo"
						)
				));
	}
}
