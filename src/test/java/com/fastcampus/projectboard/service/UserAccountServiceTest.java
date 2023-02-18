package com.fastcampus.projectboard.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.fastcampus.projectboard.domain.UserAccount;
import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.repository.UserAccountRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserAccountServiceTest {

	@InjectMocks private UserAccountService sut;

	@Mock private UserAccountRepository userAccountRepository;

	@DisplayName("존재하는 회원 ID를 검색하면, 회원 데이터를 Optional로 반환한다.")
	@Test
	void givenExistentUserId_whenSearching_thenReturnsOptionalUserData() {
		//Given
		String username = "inwook";
		given(userAccountRepository.findById(username)).willReturn(Optional.of(createUserAccount(username)));

		//When
		Optional<UserAccountDto> result = sut.searchUser(username);

		//Then
		assertThat(result).isPresent();
		then(userAccountRepository).should().findById(username);
	}


	@DisplayName("존재하지 않는 회원 ID를 검색하면, 비어있는 Optional을 반환한다.")
	@Test
	void givenNonexistentUserId_whenSearching_thenReturnsOptionalUserData() {
		//Given
		String username = "존재 하지 않음";
		given(userAccountRepository.findById(username)).willReturn(Optional.empty());

		//When
		Optional<UserAccountDto> result = sut.searchUser(username);

		//Then
		assertThat(result).isEmpty();
		then(userAccountRepository).should().findById(username);
	}

	@DisplayName("회원 정보를 입력하면, 새로운 회원 정보를 저장하여 가입시키고 해당 회원 데이터를 리턴한다.")
	@Test
	void givenUserParams_whenSaving_thenSavesUserAccount() {
		//Given
		String username = "inwook";
		UserAccount userAccount = createUserAccount(username);
		UserAccount savedUserAccount = createSigningUpUserAccount(username);
		given(userAccountRepository.save(userAccount)).willReturn(savedUserAccount);

		//When
		UserAccountDto result = sut.saveUser(
				userAccount.getUserId(),
				userAccount.getUserPassword(),
				userAccount.getEmail(),
				userAccount.getNickname(),
				userAccount.getMemo()
		);

		//Then
		assertThat(result)
				.hasFieldOrPropertyWithValue("userId", savedUserAccount.getUserId())
				.hasFieldOrPropertyWithValue("userPassword", savedUserAccount.getUserPassword())
				.hasFieldOrPropertyWithValue("email", savedUserAccount.getEmail())
				.hasFieldOrPropertyWithValue("nickname", savedUserAccount.getNickname())
				.hasFieldOrPropertyWithValue("memo", savedUserAccount.getMemo())
				.hasFieldOrPropertyWithValue("createdBy", savedUserAccount.getUserId())
				.hasFieldOrPropertyWithValue("modifiedBy", savedUserAccount.getUserId());
		then(userAccountRepository).should().save(userAccount);
	}

	private UserAccount createUserAccount(String username) {
		return createUserAccount(username, null);
	}

	private UserAccount createSigningUpUserAccount(String username) {
		return createUserAccount(username, username);
	}

	private UserAccount createUserAccount(String username, String createdBy) {
		return UserAccount.of(
				username,
				"password",
				"inwook@email.com",
				"nickname",
				"memo",
				createdBy
		);
	}
}
