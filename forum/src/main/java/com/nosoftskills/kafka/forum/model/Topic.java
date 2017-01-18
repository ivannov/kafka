package com.nosoftskills.kafka.forum.model;

import javax.persistence.Entity;
import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Version;

import com.nosoftskills.kafka.forum.model.Comment;

import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.OneToMany;

@Entity
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private int version;

    @Column(nullable = false)
    private String title;

    @OneToMany
    private Set<Comment> comments = new HashSet<Comment>();

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(final Set<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Topic topic = (Topic) o;
        return Objects.equals(title, topic.title) &&
                Objects.equals(comments, topic.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, comments);
    }

    @Override
    public String toString() {
        return "Topic{" +
                "title='" + title + '\'' +
                ", comments=" + comments +
                '}';
    }
}