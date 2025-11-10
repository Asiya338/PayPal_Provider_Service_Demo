package com.example.demo.util;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JsonUtil {

	private final ObjectMapper objectMapper;

	public String toJson(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			log.error("Error converting to json : {} ", e.getMessage(), e);
			throw new RuntimeException("ERROR CONVERTING TO JSON" + e.getMessage());
		}
	}

	public <T> T fromJson(String json, Class<T> clazz) {
		try {
			return objectMapper.readValue(json, clazz);
		} catch (Exception e) {
			log.error("Error converting from json to : {} ", e.getMessage(), e);
			throw new RuntimeException("ERROR CONVERTING FROM JSON TO:" + e.getMessage());
		}
	}
}
