package com.fastcampus.projectboard.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Table(indexes = {
		@Index(columnList = "content"),
		@Index(columnList = "createdAt"),
		@Index(columnList = "createdBy"),
})
@Entity
public class ArticleComment extends AuditingFields {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@Setter @Column(nullable = false) private UserAccount userAccount;
	@Setter @ManyToOne(optional = false) private Article article; //게시글 Id
	@Setter @Column(nullable = false, length = 500) private String content; //내용

	@Setter
	@Column(updatable = false)
	private Long parentCommentId;

	protected ArticleComment() {
	}

	private ArticleComment(UserAccount userAccount, Article article, String content) {
		this.userAccount = userAccount;
		this.article = article;
		this.content = content;
	}

	public static ArticleComment of(Article article, UserAccount userAccount, String content) {
		return new ArticleComment(userAccount, article, content);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {return true;}
		if (!(o instanceof ArticleComment that)) return false;
		return id != null && id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}