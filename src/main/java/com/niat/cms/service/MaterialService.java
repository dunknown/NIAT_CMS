package com.niat.cms.service;

import com.niat.cms.domain.Material;
import com.niat.cms.domain.Tag;
import com.niat.cms.domain.User;
import com.niat.cms.repo.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author dunknown
 */
@Service
@Transactional
public class MaterialService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private MaterialRepository materialRepository;

    public void save(Material material) {
        materialRepository.save(material);
    }

    public void delete(long id) {
        materialRepository.delete(id);
    }

    public Material findById(long id) {
        return materialRepository.findById(id);
    }

    public List<Material> findMaterialsOnMain() {
        return materialRepository.findByStatusOrderByDateDesc(Material.Status.MAIN);
    }
    public Page<Material> findMaterialsOnMain(int page) {
        return materialRepository.findByStatusOrderByDateDesc(Material.Status.MAIN, new PageRequest(page, PAGE_SIZE));
    }

    public List<Material> findMaterialsInArchive() {
        return materialRepository.findByStatusOrderByDateDesc(Material.Status.ARCHIVE);
    }
    public Page<Material> findMaterialsInArchive(int page) {
        return materialRepository.findByStatusOrderByDateDesc(Material.Status.ARCHIVE, new PageRequest(page, PAGE_SIZE));
    }


    public List<Material> findModerationTasks() {
        return materialRepository.findByStatusOrderByDateDesc(Material.Status.MODERATION_TASK);
    }
    public Page<Material> findModerationTasks(int page) {
        return materialRepository.findByStatusOrderByDateDesc(Material.Status.MODERATION_TASK, new PageRequest(page, PAGE_SIZE));
    }

    public List<Material> findUserModerationTasks(User user) {
        return materialRepository.findByStatusAndModeratorOrderByDateDesc(Material.Status.UNDER_MODERATION, user);
    }
    public Page<Material> findUserModerationTasks(User user, int page) {
        return materialRepository.findByStatusAndModeratorOrderByDateDesc(Material.Status.UNDER_MODERATION, user, new PageRequest(page, PAGE_SIZE));
    }

    public List<Material> findUserDrafts(User user) {
        return materialRepository.findByStatusAndAuthorOrderByDateDesc(Material.Status.DRAFT, user);
    }
    public Page<Material> findUserDrafts(User user, int page) {
        return materialRepository.findByStatusAndAuthorOrderByDateDesc(Material.Status.DRAFT, user, new PageRequest(page, PAGE_SIZE));
    }

    public List<Material> findMaterialsWithTag(Tag tag) {
        return materialRepository.findByTagOrderByDateDesc(tag);
    }
    public Page<Material> findMaterialsWithTag(Tag tag, int page) {
        return materialRepository.findByTagOrderByDateDesc(tag, new PageRequest(page, PAGE_SIZE));
    }

    public List<Material> findUserFavourites(User user) {
        return materialRepository.findUserFavouritesOrderByDateDesc(user);
    }
    public Page<Material> findUserFavourites(User user, int page) {
        return materialRepository.findUserFavouritesOrderByDateDesc(user, new PageRequest(page, PAGE_SIZE));
    }

    public void setMaterialStatus(long id, Material.Status status) {
        Material m = materialRepository.findById(id);
        if(m != null) {
            m.setStatus(status);
        }
    }

    public void setMaterialModerator(long id, User moderator) {
        Material m = materialRepository.findById(id);
        if(m != null) {
            m.setModerator(moderator);
        }
    }

    public void setMaterialTitle(long id, String title) {
        Material m = materialRepository.findById(id);
        if(m != null) {
            m.setTitle(title);
        }
    }

    public void setMaterialTags(long id, Set<Tag> tags) {
        Material m = materialRepository.findById(id);
        if(m != null) {
            m.setTags(tags);
        }
    }

    public void setMaterialShortText(long id, String shortText) {
        Material m = materialRepository.findById(id);
        if(m != null) {
            m.setShortText(shortText);
        }
    }

    public void setMaterialMainText(long id, String mainText) {
        Material m = materialRepository.findById(id);
        if(m != null) {
            m.setMainText(mainText);
        }
    }

    public void setMaterialFeatured(long id, boolean featured) {
        Material m = materialRepository.findById(id);
        if(m != null) {
            m.setFeatured(featured);
        }
    }
}
