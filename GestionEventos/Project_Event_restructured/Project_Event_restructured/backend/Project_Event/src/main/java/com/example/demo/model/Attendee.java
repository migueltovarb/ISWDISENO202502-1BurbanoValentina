package com.example.demo.model;

import java.time.Instant;

public class Attendee {
    private Long id;
    private String name;
    private String documentId;
    private String email;
    private String phone;
    private boolean emailNotifications;
    private Instant createdAt;
    private Instant updatedAt;

    public Attendee(){}

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getName(){return name;} public void setName(String name){this.name=name;}
    public String getDocumentId(){return documentId;} public void setDocumentId(String documentId){this.documentId=documentId;}
    public String getEmail(){return email;} public void setEmail(String email){this.email=email;}
    public String getPhone(){return phone;} public void setPhone(String phone){this.phone=phone;}
    public boolean isEmailNotifications(){return emailNotifications;} public void setEmailNotifications(boolean emailNotifications){this.emailNotifications=emailNotifications;}
    public Instant getCreatedAt(){return createdAt;} public void setCreatedAt(Instant createdAt){this.createdAt=createdAt;}
    public Instant getUpdatedAt(){return updatedAt;} public void setUpdatedAt(Instant updatedAt){this.updatedAt=updatedAt;}
}
