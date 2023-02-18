package com.fastcampus.projectboard.dto.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.fastcampus.projectboard.dto.ArticleCommentDto;
import com.fastcampus.projectboard.dto.ArticleWithCommentsDto;
import com.fastcampus.projectboard.dto.HashtagDto;
import com.fastcampus.projectboard.dto.UserAccountDto;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DTO - 댓글을 포함한 게시글 응답 테스트")
class ArticleWithCommentsResponseTest {

	@DisplayName("자식 댓글이 없는 게시글 + 댓글 dto를 api 응답으로 변환할 때, 댓글을 시간 내림차순 + ID 오름차순으로 정리한다.")
	@Test
	void givenArticleWithCommentsDtoWithoutChildComments_whenMapping_thenOrganizesCommentsWithCertainOrder() {
		//Given
		LocalDateTime now = LocalDateTime.now();
		Set<ArticleCommentDto> articleCommentDtos = Set.of(
				createArticleCommentDto(1L, null, now),
				createArticleCommentDto(2L, null, now.plusDays(1L)),
				createArticleCommentDto(3L, null, now.plusDays(3L)),
				createArticleCommentDto(4L, null, now),
				createArticleCommentDto(5L, null, now.plusDays(5L)),
				createArticleCommentDto(6L, null, now.plusDays(4L)),
				createArticleCommentDto(7L, null, now.plusDays(2L)),
				createArticleCommentDto(8L, null, now.plusDays(7L))
		);
		ArticleWithCommentsDto input = createArticleWithCommentsDto(articleCommentDtos);

		//When
		ArticleWithCommentsResponse actual = ArticleWithCommentsResponse.from(input);

		System.out.println("res:" + actual.articleCommentResponse().size());
		//Then
		assertThat(actual.articleCommentResponse()).hasSize(8);
		assertThat(actual.articleCommentResponse())
				.containsExactly(
						createArticleCommentResponse(8L, null, now.plusDays(7L)),
						createArticleCommentResponse(5L, null, now.plusDays(5L)),
						createArticleCommentResponse(6L, null, now.plusDays(4L)),
						createArticleCommentResponse(3L, null, now.plusDays(3L)),
						createArticleCommentResponse(7L, null, now.plusDays(2L)),
						createArticleCommentResponse(2L, null, now.plusDays(1L)),
						createArticleCommentResponse(1L, null, now),
						createArticleCommentResponse(4L, null, now)				);
	}

	@DisplayName("게시글 + 댓글 dto를 api 응답으로 변환할 때, 댓글 부모 자식 관계를 규칙으로 정렬하여 정리한다.")
	@Test
	void givenArticleWithCommentsDto_whenMapping_thenOrganizesParentAndChildCommentsWithCertainOrders() {
		//Given
		LocalDateTime now = LocalDateTime.now();
		Set<ArticleCommentDto> articleCommentDtos = Set.of(
				createArticleCommentDto(1L, null, now),
				createArticleCommentDto(2L, 1L, now.plusDays(1L)),
				createArticleCommentDto(3L, 1L, now.plusDays(3L)),
				createArticleCommentDto(4L, 1L, now),
				createArticleCommentDto(5L, null, now.plusDays(5L)),
				createArticleCommentDto(6L, null, now.plusDays(4L)),
				createArticleCommentDto(7L, 6L, now.plusDays(2L)),
				createArticleCommentDto(8L, 6L, now.plusDays(7L))
		);
		ArticleWithCommentsDto input = createArticleWithCommentsDto(articleCommentDtos);

		//When
		ArticleWithCommentsResponse actual = ArticleWithCommentsResponse.from(input);

		//Then
		assertThat(actual.articleCommentResponse())
				.containsExactly(
						createArticleCommentResponse(5L, null, now.plusDays(5)),
						createArticleCommentResponse(6L, null, now.plusDays(4)),
						createArticleCommentResponse(1L, null, now)
				)
				//부모에 포함된 자식들을 빼서 평평하게 하여 비교한다.
				.flatExtracting(ArticleCommentResponse::childComments)
				.containsExactly(
						createArticleCommentResponse(7L, 6L, now.plusDays(2L)),
						createArticleCommentResponse(8L, 6L, now.plusDays(7L)),
						createArticleCommentResponse(4L, 1L, now),
						createArticleCommentResponse(2L, 1L, now.plusDays(1L)),
						createArticleCommentResponse(3L, 1L, now.plusDays(3L))
				);
	}

	private ArticleWithCommentsDto createArticleWithCommentsDto(Set<ArticleCommentDto> articleCommentDtos) {
		return ArticleWithCommentsDto.of(
			1L,
				createUserAccountDto(),
				articleCommentDtos,
				"title",
				"content",
				Set.of(HashtagDto.of("java")),
				LocalDateTime.now(),
				"uno",
				LocalDateTime.now(),
				"uno"
		);
	}

	private UserAccountDto createUserAccountDto() {
		return UserAccountDto.of(
				"uno",
				"password",
				"inwook@email.com",
				"Uno",
				"This is memo",
				LocalDateTime.now(),
				"uno",
				LocalDateTime.now(),
				"uno"
		);
	}

	private ArticleCommentDto createArticleCommentDto(Long id, Long parentCommentId, LocalDateTime createdAt) {
		return ArticleCommentDto.of(
				id,
				1L,
				createUserAccountDto(),
				parentCommentId,
				"test comment" + id,
				createdAt,
				"inwook",
				createdAt,
				"inwook"
		);
	}

	private ArticleCommentResponse createArticleCommentResponse(Long id, Long parentCommentId, LocalDateTime createdAt) {
		return ArticleCommentResponse.of(
				id,
				"test comment" + id,
				createdAt,
				"inwook@mail.com",
				"Inwook",
				"inwook",
				parentCommentId
		);
	}
}