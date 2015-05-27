package com.niat.cms.web;

import com.niat.cms.domain.Material;
import com.niat.cms.domain.Tag;
import com.niat.cms.domain.User;
import com.niat.cms.exceptions.NoSuchRoleException;
import com.niat.cms.exceptions.UserChangedOwnRoleException;
import com.niat.cms.service.MaterialService;
import com.niat.cms.service.TagService;
import com.niat.cms.service.UserService;
import com.niat.cms.web.forms.MaterialForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

/**
 * @author gtament
 */

@Controller
public class AdminPagesController {

    @Autowired
    private MaterialService materialService;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/editmain")
    public String editMain() {
        return "edit_main";
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users";
    }

    @RequestMapping(value = "/users/{userId}/setrole/{roleName}", method = RequestMethod.GET)
    public @ResponseBody void setRole(@PathVariable(value="userId") long id, @PathVariable String roleName, @AuthenticationPrincipal User currentUser) {
        if (currentUser.getId() == id)
            throw new UserChangedOwnRoleException();
        switch (roleName) {
            case "ROLE_READER":
                userService.setRole(id, User.Role.READER);
                break;
            case "ROLE_AUTHOR":
                userService.setRole(id, User.Role.AUTHOR);
                break;
            case "ROLE_CORRECTOR":
                userService.setRole(id, User.Role.CORRECTOR);
                break;
            case "ROLE_EDITOR":
                userService.setRole(id, User.Role.EDITOR);
                break;
            case "ROLE_ADMIN":
                userService.setRole(id, User.Role.ADMIN);
                break;
            default:
                throw new NoSuchRoleException();
        }
    }

    @RequestMapping(value = "/addmaterial", method = RequestMethod.GET)
    public String MaterialForm(Model model) {
        model.addAttribute("materialForm", new MaterialForm());
        return "addmaterial";
    }

    @RequestMapping(value = "/addmaterial", method = RequestMethod.POST)
    public String submitMaterial(@Valid MaterialForm materialForm, BindingResult bindingResult, @AuthenticationPrincipal User currentUser) {
        String[] texts = materialForm.getText().split("&lt;cut&gt;", 2);
        Material material = new Material(materialForm.getTitle(), texts[0], texts[1], currentUser, materialForm.isOnMain());

        String[] tags = materialForm.getTags().split("\\s*,[,\\s]*");
        Set<Tag> tagsSet = new HashSet<>();
        for(String tag : tags) {
            if (tag.length() != 0) {
                Tag t = tagService.findByText(tag);
                if (t == null) {
                    tagsSet.add(new Tag(tag));
                } else {
                    tagsSet.add(t);
                }
            }
        }

        if(tagsSet.isEmpty()) {
            bindingResult.addError(new FieldError("materialForm", "tags", "Введите хотя бы один тег"));
        }
        if(bindingResult.hasErrors()) {
            return "addmaterial";
        }

        material.setTags(tagsSet);
        materialService.save(material);
        return "redirect:/material/" + material.getId();
    }
}
