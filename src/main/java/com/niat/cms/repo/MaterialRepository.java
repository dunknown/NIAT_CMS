package com.niat.cms.repo;

import com.niat.cms.domain.Material;
import com.niat.cms.domain.Tag;
import com.niat.cms.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    List<Material> findByStatusOrderByDateDesc(Material.Status status);
    List<Material> findByStatusOrderByDateDesc(Material.Status status, Pageable pageable);

    List<Material> findByStatusAndAuthorOrderByDateDesc(Material.Status status, User author);
    List<Material> findByStatusAndAuthorOrderByDateDesc(Material.Status status, User author, Pageable pageable);

    List<Material> findByStatusAndModeratorOrderByDateDesc(Material.Status status, User moderator);
    List<Material> findByStatusAndModeratorOrderByDateDesc(Material.Status status, User moderator, Pageable pageable);

    @Query("select m from Material m where :tag member m.tags order by m.date desc")
    List<Material> findByTagOrderByDateDesc(@Param("tag") Tag tag);
    @Query("select m from Material m where :tag member m.tags order by m.date desc")
    List<Material> findByTagOrderByDateDesc(@Param("tag") Tag tag, Pageable pageable);
}
