package com.fastcampus.projectboard.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
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
	@Setter @JoinColumn(name = "article_id") private Article article; //게시글 Id
	@ManyToOne(optional = false)
	@Setter @JoinColumn(name = "user_id") private UserAccount userAccount;
	@Setter
	@Column(updatable = false)
	private Long parentCommentId;

	@ToString.Exclude
	@OrderBy("createdAt ASC")
	@OneToMany(mappedBy = "parentCommentId", cascade = CascadeType.ALL)
	private Set<ArticleComment> childComments = new LinkedHashSet<>();
	@Setter @Column(nullable = false, length = 500) private String content; //내용


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

	public void addChildComment(ArticleComment child) {
		child.setParentCommentId(this.getId());
		this.childComments.add(child);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {return true;}
		if (!(o instanceof ArticleComment that)) return false;
		return this.getId() != null && this.getId().equals(that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.getId());
	}
}