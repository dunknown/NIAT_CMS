package com.niat.cms.repo;

import com.niat.cms.domain.Material;
import com.niat.cms.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * This interface will be magically auto-implemented by Spring Data JPA
 *
 * @author dunknown
 */
public interface MaterialRepository extends JpaRepository<Material, Long> {
    Material findById(long id);
    List<Material> findByOnMainTrue();
    List<Material> findByOnMainFalse();
    List<Material> findByTag(Tag tag);
}