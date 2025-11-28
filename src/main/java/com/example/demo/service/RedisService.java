package com.example.demo.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RedisService {
	private final RedisTemplate<String, String> redisTemplate;
	private final ValueOperations<String, String> valueOperations;

	public RedisService(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
		this.valueOperations = redisTemplate.opsForValue();
	}

	// setValue with expiry
	public void setValueWithExpiry(String key, String value, long timeoutInSecs) {
		valueOperations.set(key, value, timeoutInSecs, TimeUnit.SECONDS);
	}

	public String getValue(String key) {
		return valueOperations.get(key);
	}
}