package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.QArticle;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.repository.querydsl.ArticleRepositoryCustom;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends
		JpaRepository<Article, Long>,
		ArticleRepositoryCustom,
		//entityd에 대한 기본 검색기능을 추가해준다
		//부분 검색은 지원하고 있지 ㅇ낳음
		QuerydslPredicateExecutor<Article>,
		//입맛에 맞는 검색 기능을 추가하기 위해서
		QuerydslBinderCustomizer<QArticle> {

	@Override
	default void customize(QuerydslBindings bindings, QArticle root) {
		//모든 필드들에 대한 검색이 열러 있는데
		bindings.excludeUnlistedProperties(true);
//		bindings.including(root.title, root.content, root.hashtag, root.createdAt, root.createdBy);
		bindings.bind(root.title).first(StringExpression::containsIgnoreCase); // '%{v}%'
		bindings.bind(root.content).first(StringExpression::containsIgnoreCase); // like
		bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase); // like
		bindings.bind(root.createdAt).first(DateTimeExpression::eq);
		bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
	}

	Page<Article> findByTitleContaining(String title, Pageable pageable);
	Page<Article> findByContentContaining(String content, Pageable pageable);
	Page<Article> findByUserAccount_UserIdContaining(String userId, Pageable pageable);
	Page<Article> findByUserAccount_NicknameContaining(String nickname, Pageable pageable);
	Page<Article> findByHashtag(String searchKeyword, Pageable pageable);

	void deleteByIdAndUserAccount_UserId(Long articleId, String userId);
}
