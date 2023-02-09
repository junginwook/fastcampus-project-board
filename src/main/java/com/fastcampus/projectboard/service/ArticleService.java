package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.Hashtag;
import com.fastcampus.projectboard.domain.UserAccount;
import com.fastcampus.projectboard.domain.type.SearchType;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.ArticleWithCommentsDto;
import com.fastcampus.projectboard.repository.ArticleRepository;
import com.fastcampus.projectboard.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

	private final ArticleRepository articleRepository;
	private final UserAccountRepository userAccountRepository;

	private final HashtagService hashtagService;

	@Transactional(readOnly = true)
	public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {

		if(searchKeyword == null || searchKeyword.isBlank()) {
			return articleRepository.findAll(pageable).map(ArticleDto::from);
		}

		return switch (searchType) {
			case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::from);
			case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::from);
			case ID -> articleRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable).map(ArticleDto::from);
			case NICKNAME -> articleRepository.findByUserAccount_NicknameContaining(searchKeyword, pageable).map(ArticleDto::from);
			case HASHTAG -> null;
		};
	}

	@Transactional(readOnly = true)
	public ArticleWithCommentsDto searchArticle(long articleId) {
		return articleRepository.findById(articleId)
				.map(ArticleWithCommentsDto::from)
				.orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다. - articleId:" + articleId));
	}

	public void saveArticle(ArticleDto dto) {
		UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());
		Set<Hashtag> hashtags = renewHashtagsFromContent(dto.content());

		Article article = dto.toEntity(userAccount);
		article.addHashtags(hashtags);
		articleRepository.save(article);
	}

	public void updateArticle(Long articleId, ArticleDto dto) {
		try {
			Article article = articleRepository.getReferenceById(articleId);
			UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());

			if (article.getUserAccount().equals(userAccount)) {
				if (dto.title() != null) {
					article.setTitle(dto.title());
				}
				if (dto.content() != null) {
					article.setContent(dto.content());
				}

				Set<Long> hashtagIds = article.getHashtags().stream()
						.map(Hashtag::getId)
						.collect(Collectors.toSet());
				article.clearHashtags();
				articleRepository.flush();

				hashtagIds.forEach(hashtagService::deleteHashtagWithoutArticles);

				Set<Hashtag> hashtags = renewHashtagsFromContent(dto.content());
				article.addHashtags(hashtags);
			}
		} catch (EntityNotFoundException e) {
			log.warn("게시글 업데이트 실패, 게시글을 수정하는데 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
		}
	}

	public void deleteArticle(long articleId, String userId) {
		Article article = articleRepository.getReferenceById(articleId);
		Set<Long> hashtagIds = article.getHashtags().stream()
				.map(Hashtag::getId)
				.collect(Collectors.toUnmodifiableSet());

		articleRepository.deleteByIdAndUserAccount_UserId(articleId, userId);
		articleRepository.flush();

//		hashtagIds.forEach();
	}

	@Transactional(readOnly = true)
	public Page<ArticleDto> searchArticlesViaHashtag(String hashtagName, Pageable pageable) {
		if (hashtagName == null || hashtagName.isBlank()) {
			return Page.empty(pageable);
		}

		return articleRepository.findByHashtag(hashtagName, pageable).map(ArticleDto::from);
	}
	public ArticleWithCommentsDto getArticleWithComments(Long articleId) {
		return articleRepository.findById(articleId)
				.map(ArticleWithCommentsDto::from)
				.orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
	}

	@Transactional(readOnly = true)
	public ArticleDto getArticle(Long articleId) {
		return articleRepository.findById(articleId)
				.map(ArticleDto::from)
				.orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다. - articleId" + articleId));
	}


	private Set<Hashtag> renewHashtagsFromContent(String content) {
		return Collections.emptySet();
	}

	public List<String> getHashtags() {
		return articleRepository.findAllDistinctHashtags();
	}
}
