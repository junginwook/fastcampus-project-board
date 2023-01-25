package com.fastcampus.projectboard.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@ToString(callSuper = true)
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

	@ManyToOne(optional = false)
	@Setter @Column(nullable = false) private UserAccount userAccount;
	@Setter @Column(nullable = false) private String title; //제목
	@Setter @Column(nullable = false, length = 10000) private String content; //내용
	@ToString.Exclude
	@JoinTable(
			name = "article_hashtag",
			joinColumns = @JoinColumn(name = "articleId"),
			inverseJoinColumns = @JoinColumn(name = "hashtagId")
	)
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@Setter @Column private Set<Hashtag> hashtags = new LinkedHashSet<>(); //해시태그

	@ToString.Exclude
	@OrderBy("createdAt DESC")
	@OneToMany(mappedBy = "article", cascade = CascadeType.PERSIST)
	private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

	protected Article() {}

	private Article(UserAccount userAccount, String title, String content) {
		this.title = title;
		this.content = content;
		this.userAccount = userAccount;
	}

	public static Article of(UserAccount userAccount, String title, String content) {
		return new Article(userAccount, title, content);
	}

	public void addHashtag(Hashtag hashtag) {
		this.getHashtags().add(hashtag);
	}

	public void addHashtags(Set<Hashtag> hashtags) {
		this.getHashtags().addAll(hashtags);
	}

	public void clearHashtags() {
		this.getHashtags().clear();
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
