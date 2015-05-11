package com.niat.cms.repo;

import com.niat.cms.domain.Material;
import com.niat.cms.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This interface will be magically auto-implemented by Spring Data JPA
 *
 * @author dunknown
 */
@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    Material findById(long id);
    List<Material> findByOnMainTrueOrderByDateDesc();
    List<Material> findByOnMainFalseOrderByDateDesc();
    List<Material> findByTagsContainingOrderByDateDesc(Tag tag);
}
