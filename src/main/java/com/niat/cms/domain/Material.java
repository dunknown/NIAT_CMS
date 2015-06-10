package com.niat.cms.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author dunknown
 */
@Entity
public class Material implements Comparable<Material>{

    public static enum Status {
        DRAFT,
        MODERATION_TASK,
        UNDER_MODERATION,
        ARCHIVE,
        MAIN
    }

    @Id @GeneratedValue
    private long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    @Column(nullable = false)
    private String shortText;

    @Lob
    @Column
    private String mainText;

    @ManyToOne
    private User author;

    @ManyToOne
    private User moderator;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    private boolean featured;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "material_tag",
               joinColumns = {@JoinColumn(name = "material_id")},
               inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private Set<Tag> tags;

    public Material() {
    }

    public Material(String title, String shortText, String mainText, User author, Status status) {
        this.title = title;
        this.shortText = shortText;
        this.mainText = mainText;
        this.author = author;
        this.status = status;
        this.date = new Date();
        this.tags = new HashSet<Tag>();
        this.featured = false;
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

    public User getModerator() {
        return moderator;
    }

    public void setModerator(User moderator) {
        this.moderator = moderator;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
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

    @Override
    public int compareTo(Material o) {
        return this.date.compareTo(o.date);
    }

}
