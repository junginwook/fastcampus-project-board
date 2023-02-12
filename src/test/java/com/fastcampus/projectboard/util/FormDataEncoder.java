package com.fastcampus.projectboard.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

@TestComponent
public class FormDataEncoder {

	private final ObjectMapper objectMapper;

	public FormDataEncoder(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public String encode(Object obj) {
		Map<String, String> fieldMap = objectMapper.convertValue(obj, new TypeReference<>() {});
		MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
		valueMap.setAll(fieldMap);

		return UriComponentsBuilder.newInstance()
				.queryParams(valueMap)
				.encode()
				.build()
				.getQuery();
	}
}
