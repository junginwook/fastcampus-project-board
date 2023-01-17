package com.fastcampus.projectboard.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
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
		@Index(columnList = "title"),
		@Index(columnList = "hashtag"),
		@Index(columnList = "createdAt"),
		@Index(columnList = "createdBy"),
})
@Entity
public class Article extends AuditingFields {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Setter @Column(nullable = false) private String title; //제목
	@Setter @Column(nullable = false, length = 10000) private String content; //내용
	@Setter @Column private String hashtag; //해시태그

	@OrderBy("id")
	@OneToMany(mappedBy = "article", cascade = CascadeType.PERSIST)
	@ToString.Exclude
	private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

	protected Article() {}

	private Article(String title, String content, String hashtag) {
		this.title = title;
		this.content = content;
		this.hashtag = hashtag;
	}

	public static Article of(String title, String content, String hashtag) {
		return new Article(title, content, hashtag);
	}

	//영속화된 엔티티만 equals가 true가 될 수 있다.
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Article article)) return false;
		return id != null && id.equals(article.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
