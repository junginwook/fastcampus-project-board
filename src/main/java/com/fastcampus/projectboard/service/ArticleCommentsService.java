package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.ArticleComment;
import com.fastcampus.projectboard.domain.UserAccount;
import com.fastcampus.projectboard.dto.ArticleCommentDto;
import com.fastcampus.projectboard.repository.ArticleCommentRepository;
import com.fastcampus.projectboard.repository.ArticleRepository;
import com.fastcampus.projectboard.repository.UserAccountRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleCommentsService {

	private final ArticleCommentRepository articleCommentRepository;
	private final ArticleRepository articleRepository;
	private final UserAccountRepository userAccountRepository;

	@Transactional(readOnly = true)
	public List<ArticleCommentDto> searchArticleComments(long articleId) {
		return articleCommentRepository.findByArticle_Id(articleId)
				.stream().map(ArticleCommentDto::from).collect(Collectors.toList());
	}
	
	@Transactional
	public void saveArticleComment(ArticleCommentDto dto) {
		try {
			Article article = articleRepository.getReferenceById(dto.articleId());
			UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());
			ArticleComment articleComment = dto.toEntity(article, userAccount);
			if (dto.parentId() != null) {
				ArticleComment parentComment = articleCommentRepository.getReferenceById(dto.parentId());
				parentComment.addChildComment(articleComment);
			} else {
				articleCommentRepository.save(articleComment);
			}
		} catch (EntityExistsException e) {
			log.warn("댓글 저장 실패. 댓글 작성에 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
		}
	}

	public void deleteArticleComment(Long articleCommentId, String userId) {
		articleCommentRepository.deleteByIdAndUserAccount_UserId(articleCommentId, userId);
	}
}
