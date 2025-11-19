package com.example.demo.model;

import java.time.Instant;

public class InvitationCode {
    private String code;
    private Long eventId;
    private int maxUses;
    private int used;
    private Instant expiresAt;

    public InvitationCode(){}

    public String getCode(){return code;} public void setCode(String code){this.code=code;}
    public Long getEventId(){return eventId;} public void setEventId(Long eventId){this.eventId=eventId;}
    public int getMaxUses(){return maxUses;} public void setMaxUses(int maxUses){this.maxUses=maxUses;}
    public int getUsed(){return used;} public void setUsed(int used){this.used=used;}
    public Instant getExpiresAt(){return expiresAt;} public void setExpiresAt(Instant expiresAt){this.expiresAt=expiresAt;}
}
