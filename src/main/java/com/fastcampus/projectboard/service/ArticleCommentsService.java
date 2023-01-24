package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.ArticleComment;
import com.fastcampus.projectboard.dto.ArticleCommentDto;
import com.fastcampus.projectboard.repository.ArticleCommentRepository;
import com.fastcampus.projectboard.repository.ArticleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleCommentsService {

	private final ArticleCommentRepository articleCommentRepository;

	private final ArticleRepository articleRepository;

	@Transactional(readOnly = true)
	public List<ArticleCommentDto> searchArticleComments(long articleId) {
		return List.of();
	}
	
	@Transactional
	public ArticleComment saveArticleComment(ArticleCommentDto dto) {
		return null;
	}

	public void deleteArticleComment(Long articleCommentId, String userId) {
	}
}
