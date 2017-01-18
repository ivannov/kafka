package com.nosoftskills.kafka.forum.model;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Version;

import com.nosoftskills.kafka.forum.model.User;

import javax.persistence.OneToOne;

@Entity
@Table(name = "COMMENTS")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private int version;

    @Column(nullable = false)
    private String content;

    @OneToOne
    private User byUser;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getByUser() {
        return byUser;
    }

    public void setByUser(User byUser) {
        this.byUser = byUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(content, comment.content) &&
                Objects.equals(byUser, comment.byUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, byUser);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "content='" + content + '\'' +
                ", byUser=" + byUser +
                '}';
    }
}