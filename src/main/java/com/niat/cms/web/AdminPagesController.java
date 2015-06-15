package com.niat.cms.web;

import com.niat.cms.domain.Material;
import com.niat.cms.domain.Tag;
import com.niat.cms.domain.User;
import com.niat.cms.exceptions.BadSortMainIndexException;
import com.niat.cms.exceptions.NoSuchRoleException;
import com.niat.cms.exceptions.UnauthorisedMEditException;
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
import java.util.List;
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

        if(currentUser.getRole() == User.Role.AUTHOR) {
            return "redirect:/";
        } else {
            return "redirect:/material/" + material.getId();
        }
    }

    @RequestMapping(value = "/todrafts", method = RequestMethod.POST)
    public String submitMaterialToDrafts(@Valid MaterialForm materialForm, BindingResult bindingResult, @AuthenticationPrincipal User currentUser) {
        String[] texts = materialForm.getText().split("&lt;cut&gt;", 2);
        String mainText;
        if (texts.length < 2)
            mainText = "";
        else
            mainText = texts[1];
        Material material = new Material(materialForm.getTitle(), texts[0], mainText, currentUser, Material.Status.DRAFT);

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

    private boolean canEdit(Material material, User currentUser){
        if(material == null || currentUser == null) {
            return false;
        }
        switch (material.getStatus()) {
            case DRAFT:
                return material.getAuthor().equals(currentUser);
            case UNDER_MODERATION:
                return material.getModerator().equals(currentUser);
            case MAIN:
            case ARCHIVE:
                return currentUser.getRole() == User.Role.ADMIN || currentUser.getRole() == User.Role.EDITOR;
            case MODERATION_TASK:
            default:
                return false;
        }
    }

    @RequestMapping(value = "/material/{id}/edit", method = RequestMethod.GET)
    public String editMaterialForm(Model model, @PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        Material material = materialService.findById(id);
        if (!canEdit(material, currentUser))
            throw new UnauthorisedMEditException();
        MaterialForm form = new MaterialForm();
        form.setTitle(material.getTitle());
        if(material.getMainText() == null || material.getMainText().equals("")) {
            form.setText(material.getShortText());
        } else {
            form.setText(material.getShortText() + "&lt;cut&gt;" + material.getMainText());
        }
        StringBuilder tags = new StringBuilder();
        for (Tag t : material.getTags()) {
            tags.append(t.getText() + ", ");
        }
        form.setTags(tags.toString());
        model.addAttribute("materialForm", form);
        model.addAttribute("matId", id);
        model.addAttribute("matStatus", material.getStatus());
        return "edit_page";
    }

    @RequestMapping(value = "/material/{id}/edit", method = RequestMethod.POST)
    public String editMaterial(@Valid MaterialForm materialForm, BindingResult bindingResult, @PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        Material material = materialService.findById(id);
        if (!canEdit(material, currentUser))
            throw new UnauthorisedMEditException();
        materialService.setMaterialTitle(id, materialForm.getTitle());
        String[] texts = materialForm.getText().split("&lt;cut&gt;", 2);
        String mainText;
        if (texts.length < 2)
            mainText = "";
        else
            mainText = texts[1];
        materialService.setMaterialShortText(id, texts[0]);
        materialService.setMaterialMainText(id, mainText);
        materialService.setMaterialTags(id, getTagsFromString(materialForm.getTags()));

        return "redirect:/material/" + id;
    }

    @RequestMapping(value = "/material/{id}/tomoderation", method = RequestMethod.POST)
    public String editMaterialAndSendToModeration(@Valid MaterialForm materialForm, BindingResult bindingResult, @PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        Material material = materialService.findById(id);
        if (!canEdit(material, currentUser))
            throw new UnauthorisedMEditException();
        materialService.setMaterialTitle(id, materialForm.getTitle());
        String[] texts = materialForm.getText().split("&lt;cut&gt;", 2);
        String mainText;
        if (texts.length < 2)
            mainText = "";
        else
            mainText = texts[1];
        materialService.setMaterialShortText(id, texts[0]);
        materialService.setMaterialMainText(id, mainText);
        materialService.setMaterialTags(id, getTagsFromString(materialForm.getTags()));
        materialService.setMaterialStatus(id, Material.Status.MODERATION_TASK);

        if(canEdit(material, currentUser)) {
            return "redirect:/material/" + id;
        } else {
            return "redirect:/";
        }
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


    private boolean canDelete(Material material, User currentUser) {
        if(material == null || currentUser == null) {
            return false;
        }
        switch (material.getStatus()) {
            case DRAFT:
                return material.getAuthor().equals(currentUser);
            case MAIN:
            case ARCHIVE:
                return currentUser.getRole() == User.Role.ADMIN ||
                        currentUser.getRole() == User.Role.EDITOR;
            default:
                return false;
        }
    }

    @RequestMapping(value = "/material/{id}/delete", method = RequestMethod.GET)
    public @ResponseBody void deleteMaterial(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        Material material = materialService.findById(id);
        if (!canDelete(material, currentUser)) {
            throw new UnauthorisedMEditException();
        }
        Material.Status status = material.getStatus();
        materialService.delete(id);
        if (status == Material.Status.MAIN) {
            List<Material> onMain = materialService.findMaterialsOnMainForSorting();
            for (int i = 0; i < onMain.size(); i++)
                materialService.decMaterialMainIndex(onMain.get(i).getId());
        }
    }

    private void resortMain(int startIndex) {
        List<Material> onMain = materialService.findMaterialsOnMainForSorting();
        for (int i = startIndex; i < onMain.size(); i++)
            materialService.incMaterialMainIndex(onMain.get(i).getId());
    }

    @RequestMapping(value = "/material/{id}/tomain", method = RequestMethod.GET)
    public @ResponseBody void toMainMaterial(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        Material material = materialService.findById(id);
        if (material == null || material.getStatus() != Material.Status.ARCHIVE) {
            throw new UnauthorisedMEditException();
        }
        List<Material> onMain = materialService.findMaterialsOnMainForSorting();
        for (int i = 0; i < onMain.size(); i++)
            materialService.incMaterialMainIndex(onMain.get(i).getId());
        materialService.setMaterialMainIndex(id, 0);
        materialService.setMaterialStatus(id, Material.Status.MAIN);
    }

    @RequestMapping(value = "/material/{id}/toarchive", method = RequestMethod.GET)
    public @ResponseBody void toArchiveMaterial(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        Material material = materialService.findById(id);
        if (material == null || material.getStatus() != Material.Status.MAIN) {
            throw new UnauthorisedMEditException();
        }
        materialService.setMaterialMainIndex(id, 0);
        materialService.setMaterialStatus(id, Material.Status.ARCHIVE);
        List<Material> onMain = materialService.findMaterialsOnMainForSorting();
        for (int i = 0; i < onMain.size(); i++)
            materialService.decMaterialMainIndex(onMain.get(i).getId());
    }

    @RequestMapping(value = "/material/{id}/feature", method = RequestMethod.GET)
    public @ResponseBody void featureMaterial(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        Material material = materialService.findById(id);
        if (material == null || material.getStatus() != Material.Status.MAIN) {
            throw new UnauthorisedMEditException();
        }
        materialService.setMaterialFeatured(id, true);
    }

    @RequestMapping(value = "/material/{id}/unfeature", method = RequestMethod.GET)
    public @ResponseBody void unfeatureMaterial(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        Material material = materialService.findById(id);
        if (material == null || material.getStatus() != Material.Status.MAIN) {
            throw new UnauthorisedMEditException();
        }
        materialService.setMaterialFeatured(id, false);
    }

    @RequestMapping(value = "/sortmain")
    public @ResponseBody void sortMain(@RequestParam("oldindex") Integer oldIndex, @RequestParam("newindex") Integer newIndex) {
        List<Material> onMain = materialService.findMaterialsOnMainForSorting();

        if (oldIndex >= onMain.size() || newIndex >= onMain.size())
            throw new BadSortMainIndexException();

        Material draggedMaterial = onMain.get(oldIndex);
        if (newIndex > oldIndex)
            for (int i = oldIndex + 1; i <= newIndex; i++)
                materialService.decMaterialMainIndex(onMain.get(i).getId());
        else if (newIndex == oldIndex)
            return;
        else
            for (int i = newIndex; i < oldIndex; i++)
                materialService.incMaterialMainIndex(onMain.get(i).getId());
        materialService.setMaterialMainIndex(draggedMaterial.getId(), newIndex);
    }
}
