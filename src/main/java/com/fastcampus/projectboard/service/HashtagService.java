package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.Hashtag;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class HashtagService {

	@Transactional(readOnly = true)
	public Set<Hashtag> findHashtagByNames(Set<String> hashtagNames) {
		return new HashSet<>();
	}

	public Set<String> parseHashtagNames(String content) {
		return Collections.emptySet();
	}

	public void deleteHashtagWithoutArticles(Long hashtagId) {

	}
}
