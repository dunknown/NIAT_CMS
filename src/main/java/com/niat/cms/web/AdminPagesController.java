package com.niat.cms.web;

import com.niat.cms.domain.Material;
import com.niat.cms.domain.Tag;
import com.niat.cms.domain.User;
import com.niat.cms.service.MaterialService;
import com.niat.cms.service.UserService;
import com.niat.cms.web.forms.MaterialForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

/**
 * @author gtament
 */

@Controller
@RequestMapping(value = "/admin")
public class AdminPagesController {

    @Autowired
    private MaterialService materailService;

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

    @RequestMapping(value = "/users/{userId}/setrole", method = RequestMethod.POST)
    public @ResponseBody void setRole(@PathVariable(value="userId") Long id, @RequestParam String role, @AuthenticationPrincipal User currentUser) {
        if (currentUser.getId() == id.longValue())
            return;
        switch (role) {
            case "ROLE_READER":
                //userService.setRole(id, User.Role.READER);
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
        }
        return;
    }

    @RequestMapping(value = "/addmaterial", method = RequestMethod.GET)
    public String MaterialForm(Model model) {
        model.addAttribute("materialForm", new MaterialForm());
        return "addmaterial";
    }

    @RequestMapping(value = "/addmaterial", method = RequestMethod.POST)
    public String submitMaterial(@Valid MaterialForm MaterialForm, BindingResult bindingResult, @AuthenticationPrincipal User currentUser) {
        Material material = new Material(MaterialForm.getTitle(), MaterialForm.getText(), currentUser, MaterialForm.isOnMain());
        String[] tags = MaterialForm.getTags().split("\\s*,[,\\s]*");
        Set<Tag> tagsSet =new HashSet<>();
        for(String tag : tags)
            if (tag.length() != 0)
                tagsSet.add(new Tag(tag));
        materailService.save(material);
        return "redirect:/";
    }
}
