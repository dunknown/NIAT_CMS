package com.niat.cms.web;

import com.niat.cms.domain.Material;
import com.niat.cms.domain.User;
import com.niat.cms.exceptions.NotModeratorTaskException;
import com.niat.cms.service.MaterialService;
import com.niat.cms.service.TagService;
import com.niat.cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    public String waitingForModeration(Model model) {
        model.addAttribute("materials", materialService.findModerationTasks());
        return ("modertasks");
    }

    @RequestMapping(value = "/material/{id}/taketask", method = RequestMethod.GET)
    public void takeTask(Long id, @AuthenticationPrincipal User currentUser) {
        Material material = materialService.findById(id);
        if (material.getStatus() == Material.Status.MODERATION_TASK) {
            material.setStatus(Material.Status.UNDER_MODERATION);
            material.setModerator(currentUser);
        }
        else
            throw new NotModeratorTaskException();
    }

    @RequestMapping(value = "/moderate", method = RequestMethod.GET)
    public String moderate(Model model, @AuthenticationPrincipal User currentUser) {
        model.addAttribute("materials", materialService.findUserModerationTasks(currentUser));
        return "moderate";
    }

    @RequestMapping(value = "/material/{id}/accept", method = RequestMethod.GET)
    public void acceptMaterial(Long id, @AuthenticationPrincipal User currentUser) {
        Material material = materialService.findById(id);
        if (material.getStatus() == Material.Status.UNDER_MODERATION && material.getModerator().equals(currentUser)) {
            material.setStatus(Material.Status.ARCHIVE);
            material.setModerator(null);
        }
        else
            throw new NotModeratorTaskException();
    }

    @RequestMapping(value = "/material/{id}/decline", method = RequestMethod.GET)
    public void declineMaterial(Long id, @AuthenticationPrincipal User currentUser) {
        Material material = materialService.findById(id);
        if (material.getStatus() == Material.Status.UNDER_MODERATION && material.getModerator().equals(currentUser)) {
            material.setStatus(Material.Status.DRAFT);
            material.setModerator(null);
        }
        else
            throw new NotModeratorTaskException();
    }
}
