package com.niat.cms.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author dunknown
 */
@Entity
public class User implements UserDetails {

    public static enum Role {
        ADMIN("ROLE_ADMIN", "Администратор"),
        EDITOR("ROLE_EDITOR", "Редактор"),
        AUTHOR("ROLE_AUTHOR", "Автор"),
        CORRECTOR("ROLE_CORRECTOR", "Корректор"),
        READER("ROLE_READER","Читатель");

        private String roleName;
        private String roleText;

        Role(String roleName, String roleText) {
            this.roleName = roleName;
            this.roleText = roleText;
        }

        public String getRoleName() {
            return roleName;
        }

        public String getRoleText() {
            return roleText;
        }
    }

    @Id @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Material> materials;

    public User() {
    }

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.materials = new HashSet<Material>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(Set<Material> materials) {
        this.materials = materials;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority(role.getRoleName()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (id != user.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
