package com.example.demo.model;

import java.time.Instant;

public class PasswordResetToken {
    private Long id;
    private Long userId;
    private String token;
    private Instant expiresAt;
    private boolean used;
    private Instant createdAt;

    public PasswordResetToken(){}
    public PasswordResetToken(Long id, Long userId, String token, Instant expiresAt, boolean used, Instant createdAt){ this.id=id; this.userId=userId; this.token=token; this.expiresAt=expiresAt; this.used=used; this.createdAt=createdAt; }

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public Long getUserId(){return userId;} public void setUserId(Long userId){this.userId=userId;}
    public String getToken(){return token;} public void setToken(String token){this.token=token;}
    public Instant getExpiresAt(){return expiresAt;} public void setExpiresAt(Instant expiresAt){this.expiresAt=expiresAt;}
    public boolean isUsed(){return used;} public void setUsed(boolean used){this.used=used;}
    public Instant getCreatedAt(){return createdAt;} public void setCreatedAt(Instant createdAt){this.createdAt=createdAt;}
}
