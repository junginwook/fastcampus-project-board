package com.fastcampus.projectboard.dto.security;

import com.fastcampus.projectboard.dto.UserAccountDto;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record BoardPrincipal(
		String username,
		String password,
		Collection<? extends GrantedAuthority> authorities,
		String email,
		String nickname,
		String memo,
		Map<String, Object> oAuth2Attributes
) implements UserDetails {

	public static BoardPrincipal of(String username, String password, String email, String nickname, String memo) {
		return BoardPrincipal.of(username, password, email, nickname, memo, Map.of());
	}

	public static BoardPrincipal of(String username, String password, String email, String nickname, String memo,  Map<String, Object> oAuth2Attributes) {
		// 지금은 인증만 하고 권한을 다루고 있지 ㅇ낳기 때문에 임시로 세팅
		Set<RoleType> roleTypes = Set.of(RoleType.USER);

		return new BoardPrincipal(
				username,
				password,
				roleTypes.stream()
						.map(RoleType::getName)
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toUnmodifiableSet())
				,
				email,
				nickname,
				memo,
				oAuth2Attributes
		);
	}

	public static BoardPrincipal from(UserAccountDto dto) {
		return BoardPrincipal.of(
				dto.userId(),
				dto.userPassword(),
				dto.email(),
				dto.nickname(),
				dto.memo()
		);
	}

	public UserAccountDto toDto() {
		return UserAccountDto.of(
				username,
				password,
				email,
				nickname,
				memo
		);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public enum RoleType {
		USER("ROLE_USER");

		@Getter private final String name;

		RoleType(String name) {
			this.name = name;
		}
	}
}
