package com.niat.cms.domain;

import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author dunknown
 */
@Entity
public class Tag {
    @Id @GeneratedValue
    private long id;

    @Field
    @Column(nullable = false, unique = true)
    private String text;

    @ContainedIn
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private Set<Material> materials;

    public Tag() {
    }

    public Tag(String text) {
        this.text = text;
        materials = new HashSet<Material>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(Set<Material> materials) {
        this.materials = materials;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;

        Tag tag = (Tag) o;

        if (!text.equals(tag.text)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return text.hashCode();
    }
}
