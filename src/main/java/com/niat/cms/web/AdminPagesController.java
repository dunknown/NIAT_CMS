package com.niat.cms.web;

import com.niat.cms.domain.Material;
import com.niat.cms.domain.Tag;
import com.niat.cms.domain.User;
import com.niat.cms.service.MaterialService;
import com.niat.cms.web.forms.AddMaterialForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(value = "/editmain")
    public String editMain() {
        return "edit_main";
    }

    @RequestMapping(value = "/users")
    public String users() {
        return "users";
    }

    @RequestMapping(value = "/addmaterial", method = RequestMethod.GET)
    public String addMaterialForm(Model model) {
        model.addAttribute("materialForm", new AddMaterialForm());
        return "addmaterial";
    }

    @RequestMapping(value = "/addmaterial", method = RequestMethod.POST)
    public String submitMaterial(@Valid AddMaterialForm materialForm, BindingResult bindingResult, @AuthenticationPrincipal User currentUser) {
        Material material = new Material(materialForm.getTitle(), materialForm.getText(), currentUser, materialForm.isOnMain());

        String[] tags = materialForm.getTags().split("\\s*,[,\\s]*");
        Set<Tag> tagsSet = new HashSet<>();
        for(String tag : tags)
            if (tag.length() != 0)
                tagsSet.add(new Tag(tag));

        if(tagsSet.isEmpty()) {
            bindingResult.addError(new FieldError("materialForm", "tags", "Введите хотя бы один тег"));
        }
        if(bindingResult.hasErrors()) {
            return "addmaterial";
        }

        material.setTags(tagsSet);
        materailService.save(material);
        return "redirect:/";
    }
}
