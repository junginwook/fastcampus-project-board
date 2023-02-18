package com.fastcampus.projectboard.repository.querydsl;

import com.fastcampus.projectboard.domain.Hashtag;
import com.fastcampus.projectboard.domain.QHashtag;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class HashtagRepositoryCustomImpl extends QuerydslRepositorySupport implements HashtagRepositoryCustom {

	public HashtagRepositoryCustomImpl() {
		super(Hashtag.class);
	}

	@Override
	public List<String> findAllHashtagNames() {
		QHashtag hashtag = QHashtag.hashtag;

		return from(hashtag)
				.select(hashtag.hashtagName)
				.fetch();
	}
}
