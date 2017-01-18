package com.nosoftskills.kafka.user.model;

import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.persistence.*;
import java.io.Serializable;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Version
    private int version;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @OneToMany
    private Set<Payment> cards = new HashSet<Payment>();

    public User() {
    }

    public User(String userName, String password, String firstName, String lastName, String email) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Payment> getCards() {
        return this.cards;
    }

    public void setCards(final Set<Payment> cards) {
        this.cards = cards;
    }

    public void addCard(Payment card) {
        this.cards.add(card);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userName, user.userName) &&
                Objects.equals(password, user.password) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, password, firstName, lastName, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", cards=" + cards +
                '}';
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("id", getId())
                .add("userName", getUserName())
                .add("firstName", getFirstName())
                .add("lastName", getLastName())
                .add("email", getEmail())
                .build();
    }

    public static User fromJsonString(String userJson) {
        JsonObject jsonObject = Json.createReader(new StringReader(userJson))
                .readObject();
        User user = new User();
        JsonNumber idNumber = jsonObject.getJsonNumber("id");
        if (idNumber != null) {
            user.setId(idNumber.longValue());
        }
        user.setUserName(jsonObject.getString("userName"));
        JsonString password = jsonObject.getJsonString("password");
        if (password != null) {
            user.setPassword(password.getString());
        }
        user.setFirstName(jsonObject.getString("firstName"));
        user.setLastName(jsonObject.getString("lastName"));
        user.setEmail(jsonObject.getString("email"));
        return user;
    }
}