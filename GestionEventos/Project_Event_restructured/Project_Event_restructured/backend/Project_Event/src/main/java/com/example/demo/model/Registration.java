package com.example.demo.model;

import java.time.Instant;

public class Registration {
    private Long id;
    private Long eventId;
    private Long attendeeId;
    private RegistrationStatus status;
    private Double paidAmount;
    private Instant createdAt;

    public Registration(){}

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public Long getEventId(){return eventId;} public void setEventId(Long eventId){this.eventId=eventId;}
    public Long getAttendeeId(){return attendeeId;} public void setAttendeeId(Long attendeeId){this.attendeeId=attendeeId;}
    public RegistrationStatus getStatus(){return status;} public void setStatus(RegistrationStatus status){this.status=status;}
    public Double getPaidAmount(){return paidAmount;} public void setPaidAmount(Double paidAmount){this.paidAmount=paidAmount;}
    public Instant getCreatedAt(){return createdAt;} public void setCreatedAt(Instant createdAt){this.createdAt=createdAt;}
}
