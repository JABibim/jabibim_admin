package com.jabibim.admin.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {

  private final RedisTemplate<String, Object> redisTemplate;

  public void addToBlackList(String token, long remainingTime) {
    if (remainingTime > 0) {
      redisTemplate.opsForValue().setIfAbsent(
          "blacklist:" + token,
          "logged out",
          remainingTime,
          TimeUnit.MILLISECONDS);
    }
  }

  public boolean isBlackListed(String token) {
    return Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + token));
  }
}
