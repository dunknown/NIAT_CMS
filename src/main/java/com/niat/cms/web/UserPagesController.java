package com.niat.cms.web;

import com.niat.cms.domain.Material;
import com.niat.cms.domain.Tag;
import com.niat.cms.exceptions.MaterialNotFoundException;
import com.niat.cms.exceptions.TagNotFoundException;
import com.niat.cms.service.MaterialService;
import com.niat.cms.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author gtament
 */

@Controller
public class UserPagesController {

    @Autowired
    private MaterialService materialService;

    @Autowired
    private TagService tagService;

    @RequestMapping(value = "/")
    public String mainPage(Model model) {
        model.addAttribute("materials", materialService.findMaterialsOnMain());
        return "main";
    }

    @RequestMapping(value = "/archive")
    public String archivePage(Model model) {
        model.addAttribute("materials", materialService.findMaterialsInArchive());
        return "archive";
    }

    @RequestMapping(value = "/material/{matId}")
    public String materialPage(Model model, @PathVariable(value="matId") Long matId) {
        Material material = materialService.findById(matId);
        if (material == null)
            throw new MaterialNotFoundException();
        model.addAttribute("material", material);
        return "material";
    }

    @RequestMapping(value = "/tag/{tagText}")
    public String materialsWithTagPage(Model model, @PathVariable String tagText) {
        Tag tag = tagService.findByText(tagText);
        if (tag == null)
            throw new TagNotFoundException();
        List<Material> materials = materialService.findMaterialsWithTag(tag);
        model.addAttribute("materialswithtag", materials);
        return "materailswtag";
    }
}
