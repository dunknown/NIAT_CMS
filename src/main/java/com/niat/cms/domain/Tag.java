package com.niat.cms.domain;

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

    @Column(nullable = false, unique = true)
    private String text;

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
}
