package com.niat.cms.web;

import com.niat.cms.domain.Material;
import com.niat.cms.domain.Tag;
import com.niat.cms.domain.User;
import com.niat.cms.exceptions.MaterialNotFoundException;
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
    public String materialForm(Model model) {
        model.addAttribute("materialForm", new MaterialForm());
        return "addmaterial";
    }

    @RequestMapping(value = "/addmaterial", method = RequestMethod.POST)
    public String submitMaterial(@Valid MaterialForm materialForm, BindingResult bindingResult, @AuthenticationPrincipal User currentUser) {
        String[] texts = materialForm.getText().split("&lt;cut&gt;", 2);
        String mainText;
        if (texts.length < 2)
            mainText = "";
        else
            mainText = texts[1];
        Material material = new Material(materialForm.getTitle(), texts[0], mainText, currentUser, Material.Status.MODERATION_TASK);

        Set<Tag> tagsSet = getTagsFromString(materialForm.getTags());
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

    @RequestMapping(value = "/material/{id}/edit", method = RequestMethod.GET)
    public String editMaterialForm(Model model, @PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        Material material = materialService.findById(id);
        if (material == null || (material.getStatus() == Material.Status.DRAFT && !material.getAuthor().equals(currentUser))
                || (material.getStatus() == Material.Status.UNDER_MODERATION && !material.getModerator().equals(currentUser))
                || (material.getStatus() == Material.Status.ARCHIVE || material.getStatus() == Material.Status.MAIN
                    && (currentUser.getRole() != User.Role.ADMIN || currentUser.getRole() != User.Role.EDITOR)))
            throw new MaterialNotFoundException();
        MaterialForm form = new MaterialForm();
        form.setTitle(material.getTitle());
        form.setText(material.getShortText() + "<cut>" + material.getMainText());
        form.setTags(material.getTags().toString());
        model.addAttribute("materialForm", form);
        return "edit_page";
    }

    @RequestMapping(value = "/material/{id}/edit", method = RequestMethod.POST)
    public String editMaterial(@Valid MaterialForm materialForm, BindingResult bindingResult, @PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        Material material = materialService.findById(id);
        if (material == null)
            throw new MaterialNotFoundException();
        material.setTitle(materialForm.getTitle());
        String[] texts = materialForm.getText().split("&lt;cut&gt;", 2);
        String mainText;
        if (texts.length < 2)
            mainText = "";
        else
            mainText = texts[1];
        material.setShortText(texts[0]);
        material.setMainText(mainText);
        material.setTags(getTagsFromString(materialForm.getTags()));
        return "redirect:/material/" + id;
    }

    private Set<Tag> getTagsFromString(String tagsText) {
        String[] tags = tagsText.split("\\s*,[,\\s]*");
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
        return tagsSet;
    }

}
