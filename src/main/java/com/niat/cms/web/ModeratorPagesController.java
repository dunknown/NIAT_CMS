package com.niat.cms.web;

import com.niat.cms.domain.Material;
import com.niat.cms.domain.User;
import com.niat.cms.exceptions.NotModeratorTaskException;
import com.niat.cms.service.MaterialService;
import com.niat.cms.service.TagService;
import com.niat.cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author gtament
 */

@Controller
public class ModeratorPagesController {

    @Autowired
    private MaterialService materialService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/modertasks", method = RequestMethod.GET)
    public String waitingForModerationRedirect() {
        return "redirect:/modertasks/page1";
    }

    @RequestMapping(value = "/modertasks/page{num}", method = RequestMethod.GET)
    public String waitingForModeration(Model model, @PathVariable Integer num) {
        Page page = materialService.findModerationTasks(num - 1);
        model.addAttribute("materials", page.getContent());
        model.addAttribute("currentPage", page.getNumber() + 1);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("url", "/modertasks/page");
        return ("modertasks");
    }

    @RequestMapping(value = "/material/{id}/taketask", method = RequestMethod.GET)
    public @ResponseBody void takeTask(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        Material material = materialService.findById(id);
        if (material.getStatus() == Material.Status.MODERATION_TASK
                && (currentUser.getRole() == User.Role.EDITOR || currentUser.getRole() == User.Role.CORRECTOR || currentUser.getRole() == User.Role.ADMIN)) {
            materialService.setMaterialStatus(id, Material.Status.UNDER_MODERATION);
            materialService.setMaterialModerator(id, currentUser);
        }
        else
            throw new NotModeratorTaskException();
    }

    @RequestMapping(value = "/moderate", method = RequestMethod.GET)
    public String moderateRedirect() {
        return "redirect:/moderate/page1";
    }

    @RequestMapping(value = "/moderate/page{num}", method = RequestMethod.GET)
    public String moderate(Model model, @AuthenticationPrincipal User currentUser, @PathVariable Integer num) {
        Page page = materialService.findUserModerationTasks(currentUser, num - 1);
        model.addAttribute("materials", page.getContent());
        model.addAttribute("currentPage", page.getNumber() + 1);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("url", "/moderate/page");
        return "moderate";
    }

    @RequestMapping(value = "/material/{id}/accept", method = RequestMethod.GET)
    public @ResponseBody void acceptMaterial(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        Material material = materialService.findById(id);
        if (material.getStatus() == Material.Status.UNDER_MODERATION && material.getModerator().equals(currentUser)) {
            materialService.setMaterialStatus(id, Material.Status.ARCHIVE);
            materialService.setMaterialModerator(id, null);
        }
        else
            throw new NotModeratorTaskException();
    }

    @RequestMapping(value = "/material/{id}/decline", method = RequestMethod.GET)
    public @ResponseBody void declineMaterial(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        Material material = materialService.findById(id);
        if (material.getStatus() == Material.Status.UNDER_MODERATION && material.getModerator().equals(currentUser)) {
            materialService.setMaterialStatus(id, Material.Status.DRAFT);
            materialService.setMaterialModerator(id, null);
        }
        else
            throw new NotModeratorTaskException();
    }
}
