package com.niat.cms.repo;

import com.niat.cms.domain.Material;
import com.niat.cms.domain.Tag;
import com.niat.cms.domain.User;
import org.springframework.data.domain.Page;
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
    Page<Material> findByStatusOrderByDateDesc(Material.Status status, Pageable pageable);

    List<Material> findByStatusAndAuthorOrderByDateDesc(Material.Status status, User author);
    Page<Material> findByStatusAndAuthorOrderByDateDesc(Material.Status status, User author, Pageable pageable);

    List<Material> findByStatusAndModeratorOrderByDateDesc(Material.Status status, User moderator);
    Page<Material> findByStatusAndModeratorOrderByDateDesc(Material.Status status, User moderator, Pageable pageable);

    @Query("select m from Material m where :user member m.favedUsers order by m.date desc")
    List<Material> findUserFavouritesOrderByDateDesc(@Param("user") User user);
    @Query("select m from Material m where :user member m.favedUsers order by m.date desc")
    Page<Material> findUserFavouritesOrderByDateDesc(@Param("user") User user, Pageable pageable);

    @Query("select m from Material m " +
                "where :tag member m.tags and " +
                      "(m.status = com.niat.cms.domain.Material$Status.MAIN or " +
                      "m.status = com.niat.cms.domain.Material$Status.ARCHIVE) " +
                "order by m.date desc")
    List<Material> findByTagOrderByDateDesc(@Param("tag") Tag tag);
    @Query("select m from Material m " +
            "where :tag member m.tags and " +
            "(m.status = com.niat.cms.domain.Material$Status.MAIN or " +
            "m.status = com.niat.cms.domain.Material$Status.ARCHIVE) " +
            "order by m.date desc")
    Page<Material> findByTagOrderByDateDesc(@Param("tag") Tag tag, Pageable pageable);

    @Query("select m from Material m " +
                "where m.status = com.niat.cms.domain.Material$Status.MAIN " +
                "order by m.mainIndex asc")
    List<Material> findOnMainOrderByMainIndexAsc();
}
