package com.niat.cms.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * @author dunknown
 */
@Entity
public class Material {
    @Id @GeneratedValue
    private long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private String text;

    @ManyToOne
    private User author;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(nullable = false)
    private boolean onMain;

    public Material() {
    }

    public Material(String title, String text, User author, boolean onMain) {
        this.title = title;
        this.text = text;
        this.author = author;
        this.onMain = onMain;
        this.date = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isOnMain() {
        return onMain;
    }

    public void setOnMain(boolean onMain) {
        this.onMain = onMain;
    }
}
