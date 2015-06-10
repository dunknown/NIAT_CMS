package com.niat.cms.web;

import com.niat.cms.domain.Material;
import com.niat.cms.domain.Tag;
import com.niat.cms.domain.User;
import com.niat.cms.exceptions.MaterialNotFoundException;
import com.niat.cms.exceptions.TagNotFoundException;
import com.niat.cms.service.MaterialService;
import com.niat.cms.service.TagService;
import com.niat.cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author gtament
 */

@Controller
public class UserPagesController {

    @Autowired
    private MaterialService materialService;

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

    @RequestMapping(value = "/")
    public String mainPage(Model model, @AuthenticationPrincipal User currentUser) {
        model.addAttribute("materials", materialService.findMaterialsOnMain());
        model.addAttribute("currentUser", userService.findByUsername(currentUser.getUsername()));
        return "main";
    }

    @RequestMapping(value = "/archive")
    public String archivePage(Model model, @AuthenticationPrincipal User currentUser) {
        model.addAttribute("materials", materialService.findMaterialsInArchive());
        model.addAttribute("currentUser", userService.findByUsername(currentUser.getUsername()));
        return "archive";
    }

    @RequestMapping(value = "/material/{matId}")
    public String materialPage(Model model, @PathVariable Long matId, @AuthenticationPrincipal User currentUser) {
        Material material = materialService.findById(matId);
        if(!canSee(material, currentUser)) {
            throw new MaterialNotFoundException();
        }
        model.addAttribute("material", material);
        model.addAttribute("currentUser", userService.findByUsername(currentUser.getUsername()));
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
    public String materialsWithTagPage(Model model, @PathVariable String tagText, @AuthenticationPrincipal User currentUser) {
        Tag tag = tagService.findByText(tagText);
        if (tag == null)
            throw new TagNotFoundException();
        List<Material> materials = materialService.findMaterialsWithTag(tag);
        model.addAttribute("materialswithtag", materials);
        model.addAttribute("currentUser", userService.findByUsername(currentUser.getUsername()));
        return "tag_page";
    }

    @RequestMapping(value = "/drafts")
    public String drafts(Model model, @AuthenticationPrincipal User currentUser) {
        model.addAttribute("materials", materialService.findUserDrafts(currentUser));
        return "drafts";
    }

    @RequestMapping(value = "/favourites")
    public String favourites(Model model, @AuthenticationPrincipal User currentUser) {
        model.addAttribute("materials", userService.findByUsername(currentUser.getUsername()).getFavourites());
        model.addAttribute("currentUser", userService.findByUsername(currentUser.getUsername()));
        return "favourites";
    }

    @RequestMapping(value = "/material/{matId}/addtofavs")
    public @ResponseBody void addToFavs(@PathVariable Long matId, @AuthenticationPrincipal User currentUser) {
        userService.addToFavourites(currentUser.getId(), materialService.findById(matId));
    }

    @RequestMapping(value = "/material/{matId}/unfav")
    public @ResponseBody void unFav(@PathVariable Long matId, @AuthenticationPrincipal User currentUser) {
        userService.removeFromFavourites(currentUser.getId(), materialService.findById(matId));
    }

}
