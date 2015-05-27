package com.niat.cms.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author dunknown
 */
@Entity
public class Material {
    @Id @GeneratedValue
    private long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    @Column(nullable = false)
    private String shortText;

    @Lob
    @Column(nullable = false)
    private String mainText;

    @ManyToOne
    private User author;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(nullable = false)
    private boolean onMain;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "material_tag",
               joinColumns = {@JoinColumn(name = "material_id")},
               inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private Set<Tag> tags;

    public Material() {
    }

    public Material(String title, String shortText, String mainText, User author, boolean onMain) {
        this.title = title;
        this.shortText = shortText;
        this.mainText = mainText;
        this.author = author;
        this.onMain = onMain;
        this.date = new Date();
        this.tags = new HashSet<Tag>();
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

    public String getShortText() {
        return shortText;
    }

    public void setShortText(String shortText) {
        this.shortText = shortText;
    }

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
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

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Material)) return false;

        Material material = (Material) o;

        if (id != material.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
