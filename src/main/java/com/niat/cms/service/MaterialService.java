package com.niat.cms.service;

import com.niat.cms.domain.Material;
import com.niat.cms.domain.Tag;
import com.niat.cms.domain.User;
import com.niat.cms.repo.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author dunknown
 */
@Service
@Transactional
public class MaterialService {
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

    public List<Material> findMaterialsInArchive() {
        return materialRepository.findByStatusOrderByDateDesc(Material.Status.ARCHIVE);
    }

    public List<Material> findModerationTasks() {
        return materialRepository.findByStatusOrderByDateDesc(Material.Status.MODERATION_TASK);
    }

    public List<Material> findUserModerationTasks(User user) {
        return materialRepository.findByStatusAndAuthorOrderByDateDesc(Material.Status.UNDER_MODERATION, user);
    }

    public List<Material> findUserDrafts(User user) {
        return materialRepository.findByStatusAndAuthorOrderByDateDesc(Material.Status.DRAFT, user);
    }

    public List<Material> findMaterialsWithTag(Tag tag) {
        return materialRepository.findByTagOrderByDateDesc(tag);
    }

    public void setMaterialStatus(long id, Material.Status status) {
        Material m = materialRepository.findById(id);
        if(m != null) {
            m.setStatus(status);
        }
    }
}
