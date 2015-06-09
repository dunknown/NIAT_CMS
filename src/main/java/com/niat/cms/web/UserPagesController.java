package com.niat.cms.web;

import com.niat.cms.domain.Material;
import com.niat.cms.domain.Tag;
import com.niat.cms.domain.User;
import com.niat.cms.exceptions.MaterialNotFoundException;
import com.niat.cms.exceptions.TagNotFoundException;
import com.niat.cms.service.MaterialService;
import com.niat.cms.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
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
    public String mainPage(Model model, @AuthenticationPrincipal User currentUser) {
        model.addAttribute("materials", materialService.findMaterialsOnMain());
        model.addAttribute("currentUser", currentUser);
        return "main";
    }

    @RequestMapping(value = "/archive")
    public String archivePage(Model model, @AuthenticationPrincipal User currentUser) {
        model.addAttribute("materials", materialService.findMaterialsInArchive());
        model.addAttribute("currentUser", currentUser);
        return "archive";
    }

    @RequestMapping(value = "/material/{matId}")
    public String materialPage(Model model, @PathVariable Long matId, @AuthenticationPrincipal User currentUser) {
        Material material = materialService.findById(matId);
        if(!canSee(material, currentUser)) {
            throw new MaterialNotFoundException();
        }
        model.addAttribute("material", material);
        model.addAttribute("currentUser", currentUser);
        return "material_page";
    }

    private boolean canSee(Material material, User currentUser) {
        if(material == null) {
            return false;
        }
        switch (material.getStatus()) {
            case DRAFT:
                return currentUser != null && material.getAuthor().equals(currentUser);
            case UNDER_MODERATION:
                return currentUser != null && material.getModerator().equals(currentUser);
            case MAIN:
            case ARCHIVE:
                return true;
            case MODERATION_TASK:
                return currentUser != null && (currentUser.getRole() == User.Role.ADMIN ||
                                               currentUser.getRole() == User.Role.EDITOR ||
                                               currentUser.getRole() == User.Role.CORRECTOR);
            default:
                return false;
        }
    }

    @RequestMapping(value = "/tag/{tagText}")
    public String materialsWithTagPage(Model model, @PathVariable String tagText) {
        Tag tag = tagService.findByText(tagText);
        if (tag == null)
            throw new TagNotFoundException();
        List<Material> materials = materialService.findMaterialsWithTag(tag);
        model.addAttribute("materialswithtag", materials);
        return "tag_page";
    }

    @RequestMapping(value = "/drafts")
    public String drafts(Model model, @AuthenticationPrincipal User currentUser) {
        model.addAttribute("materials", materialService.findUserDrafts(currentUser));
        return "drafts";
    }

    @RequestMapping(value = "/favourites")
    public String favourites(Model model, @AuthenticationPrincipal User currentUser) {
        model.addAttribute("materials", currentUser.getFavs());
        return "favourites";
    }
}
