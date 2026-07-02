package org.example.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.Service.ITokenBlacklistService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenBlacklistServiceImpl implements ITokenBlacklistService
{
    private final StringRedisTemplate redisTemplate;

    @Override
    public void blackList(String token, long expirationMs)
    {
        redisTemplate.opsForValue().set(
                "blacklist:" + token,
                "true",
                expirationMs,
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    public boolean isBlacklisted(String token)
    {
        return Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + token));
    }
}