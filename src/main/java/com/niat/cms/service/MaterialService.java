package com.niat.cms.service;

import com.niat.cms.domain.Material;
import com.niat.cms.domain.Tag;
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
        return materialRepository.findByOnMainTrueOrderByDateDesc();
    }

    public List<Material> findMaterialsInArchive() {
        return materialRepository.findByOnMainFalseOrderByDateDesc();
    }

    public List<Material> findMaterialsWithTag(Tag tag) {
        return materialRepository.findByTagsContainingOrderByDateDesc(tag);
    }
}
