package com.fastcampus.projectboard.controller;

import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.dto.request.ArticleCommentRequest;
import com.fastcampus.projectboard.service.ArticleCommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/comments")
@Controller
public class ArticleCommentController {

	private final ArticleCommentsService articleCommentsService;

	@PostMapping("/new")
	public String postNewArticleComment(ArticleCommentRequest articleCommentRequest) {

		//TODO: 인증 정보를 넣어줘야 한다.
		articleCommentsService.saveArticleComment(
				articleCommentRequest.toDto(
						UserAccountDto.of(
								"uno",
								"pw",
								"inwook@namver.com",
								null,
								null
						)
				)
		);

		return "redirect:/articles" + articleCommentRequest.articleId();
	}

	@PostMapping("/{commentId}/delete")
	public String deleteArticleComment(@PathVariable Long commentId, Long articleId) {

		articleCommentsService.deleteArticleComment(commentId, null);

		return "redirect:/articles/" + articleId;
	}
}
