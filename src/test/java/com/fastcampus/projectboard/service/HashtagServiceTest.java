package com.fastcampus.projectboard.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;
import static org.mockito.BDDMockito.then;

import com.fastcampus.projectboard.repository.HashtagRepository;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("비즈니스 로직 - 해시태그")
@ExtendWith(MockitoExtension.class)
class HashtagServiceTest {

	@InjectMocks private HashtagService sut;

	@Mock private HashtagRepository hashtagRepository;

	@DisplayName("본문을 파싱하면, 해시태그 이름들을 중복 없이 반환한다.")
	@MethodSource
	@ParameterizedTest(name = "[{index}] \"{0}\" => {1}")
	void givenContent_whenParsing_thenReturnsUniqueHashtagNames(String input, Set<String> expected) {
		//Given

		//When
		Set<String> actual = sut.parseHashtagNames(input);

		//Then
		assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
		then(hashtagRepository).shouldHaveNoInteractions();
	}

	static Stream<Arguments> givenContent_whenParsing_thenReturnsUniqueHashtagNames() {
		return Stream.of(
				arguments(null, Set.of()),
				arguments("", Set.of()),
				arguments(" ", Set.of()),
				arguments("", Set.of()),
				arguments("#java", Set.of("java")),
				arguments("#java_spring", Set.of("java_spring")),
				arguments("#java#spring", Set.of("java", "spring"))
		);
	}
}