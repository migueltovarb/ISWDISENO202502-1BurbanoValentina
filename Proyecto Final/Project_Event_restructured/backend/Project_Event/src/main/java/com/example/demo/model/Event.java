package com.example.demo.model;

import java.time.Instant;

public class Event {
    private Long id;
    private String title;
    private String description;
    private String category;
    private int capacity;
    private Instant startAt;
    private Instant endAt;
    private LocationType locationType;
    private String address;
    private String link;
    private String room;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

    public Event(){}

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getTitle(){return title;} public void setTitle(String title){this.title=title;}
    public String getDescription(){return description;} public void setDescription(String description){this.description=description;}
    public String getCategory(){return category;} public void setCategory(String category){this.category=category;}
    public int getCapacity(){return capacity;} public void setCapacity(int capacity){this.capacity=capacity;}
    public Instant getStartAt(){return startAt;} public void setStartAt(Instant startAt){this.startAt=startAt;}
    public Instant getEndAt(){return endAt;} public void setEndAt(Instant endAt){this.endAt=endAt;}
    public LocationType getLocationType(){return locationType;} public void setLocationType(LocationType locationType){this.locationType=locationType;}
    public String getAddress(){return address;} public void setAddress(String address){this.address=address;}
    public String getLink(){return link;} public void setLink(String link){this.link=link;}
    public String getRoom(){return room;} public void setRoom(String room){this.room=room;}
    public boolean isActive(){return active;} public void setActive(boolean active){this.active=active;}
    public Instant getCreatedAt(){return createdAt;} public void setCreatedAt(Instant createdAt){this.createdAt=createdAt;}
    public Instant getUpdatedAt(){return updatedAt;} public void setUpdatedAt(Instant updatedAt){this.updatedAt=updatedAt;}
}
