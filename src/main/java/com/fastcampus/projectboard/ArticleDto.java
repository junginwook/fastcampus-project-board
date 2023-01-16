package com.fastcampus.projectboard;

import java.io.Serial;
import java.time.LocalDateTime;

public record ArticleDto(
		String title,
		String content,
		String hashtag,
		LocalDateTime createdAt,
		String createdBy,
		LocalDateTime updatedAt,
		String updatedBy
) {

}
