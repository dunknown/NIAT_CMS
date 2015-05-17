package com.niat.cms.repo;

import com.niat.cms.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This interface will be magically auto-implemented by Spring Data JPA
 *
 * @author dunknown
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByText(String text);
}
