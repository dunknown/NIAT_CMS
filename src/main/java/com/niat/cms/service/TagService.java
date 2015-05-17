package com.niat.cms.service;

import com.niat.cms.domain.Tag;
import com.niat.cms.repo.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author dunknown
 */
@Service
@Transactional
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public Tag findByText(String text) {
        return tagRepository.findByText(text);
    }
}
