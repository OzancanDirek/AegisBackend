package org.example.Service;

public interface ITokenBlacklistService
{
    void blackList(String token, long expirationMs);

    boolean isBlacklisted(String token);
}
