package com.nosoftskills.kafka.forum.model;

import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Version;

@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private int version;

    @Column(nullable = false, updatable = false)
    private String userName;

    @Column(nullable = false)
    private String displayName;

    @Column
    private int points;

    public User() {
    }

    public User(String userName, String displayName) {
        this(userName, displayName, 0);
    }

    public User(String userName, String displayName, int points) {
        this.userName = userName;
        this.displayName = displayName;
        this.points = points;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return points == user.points &&
                Objects.equals(userName, user.userName) &&
                Objects.equals(displayName, user.displayName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, displayName, points);
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", points=" + points +
                '}';
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("id", getId())
                .add("userName", getUserName())
                .add("displayName", getDisplayName())
                .add("points", getPoints())
                .build();
    }
}