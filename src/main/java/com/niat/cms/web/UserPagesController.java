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
import org.springframework.data.domain.Page;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

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

    private List<Material> getFavourites(User currentUser) {
        List<Material> favs;
        if (currentUser != null)
            favs = new LinkedList<>(currentUser.getFavourites());
        else
            favs = new LinkedList<>();
        Collections.sort(favs, Collections.reverseOrder());
        return favs;
    }

    private User getCurrentUser(User user) {
        if(user == null) {
            return null;
        }
        return userService.findByUsername(user.getUsername());
    }

    @RequestMapping(value = "/")
    public String mainPage(Model model, @AuthenticationPrincipal User currentUser) {
        model.addAttribute("materials", materialService.findMaterialsOnMain());
        model.addAttribute("currentUser", getCurrentUser(currentUser));
        return "main";
    }

    @RequestMapping(value = "/archive", method = RequestMethod.GET)
    public String archivePageRedirect() {
        return "redirect:/archive/page1";
    }

    @RequestMapping(value = "/archive/page{num}")
    public String archivePage(Model model, @AuthenticationPrincipal User currentUser, @PathVariable Integer num) {
        Page page = materialService.findMaterialsInArchive(num - 1);
        model.addAttribute("materials", page.getContent());
        model.addAttribute("currentPage", page.getNumber() + 1);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("url", "/archive/page");
        model.addAttribute("currentUser", getCurrentUser(currentUser));
        return "archive";
    }

    @RequestMapping(value = "/material/{matId}")
    public String materialPage(Model model, @PathVariable Long matId, @AuthenticationPrincipal User currentUser) {
        Material material = materialService.findById(matId);
        if(!canSee(material, currentUser)) {
            throw new MaterialNotFoundException();
        }
        model.addAttribute("material", material);
        model.addAttribute("currentUser", getCurrentUser(currentUser));
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

    @RequestMapping(value = "/tag/{tagText}", method = RequestMethod.GET)
    public String materialsWithTagPageRedirect(@PathVariable String tagText) throws UnsupportedEncodingException {
        String tag = URLEncoder.encode(tagText, "UTF-8");
        return "redirect:/tag/" + tag +"/page1";
    }

    @RequestMapping(value = "/tag/{tagText}/page{num}")
    public String materialsWithTagPage(Model model, @PathVariable String tagText, @AuthenticationPrincipal User currentUser, @PathVariable Integer num) {
        Tag tag = tagService.findByText(tagText);
        if (tag == null)
            throw new TagNotFoundException();
        Page page = materialService.findMaterialsWithTag(tag, num - 1);
        model.addAttribute("materialswithtag", page.getContent());
        model.addAttribute("currentPage", page.getNumber() + 1);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("url", "/tag/" + tag.getText() + "/page");
        model.addAttribute("currentUser", getCurrentUser(currentUser));
        model.addAttribute("tag", tag);
        model.addAttribute("topTags", getSuggestedTags(tag));

        return "tag_page";
    }

    private List<Tag> getSuggestedTags(Tag tag) {
        List<Material> allMaterials = materialService.findMaterialsWithTag(tag);
        final Map<Tag, Integer> allTags = new HashMap<>();
        for(Material m : allMaterials) {
            for(Tag t : m.getTags()) {
                if(allTags.containsKey(t)) {
                    allTags.put(t, allTags.get(t) + 1);
                } else {
                    allTags.put(t, 1);
                }
            }
        }
        allTags.remove(tag);

        List<Tag> topTags = new ArrayList<>(allTags.keySet());
        Collections.sort(topTags, new Comparator<Tag>() {
            @Override
            public int compare(Tag o1, Tag o2) {
                return Integer.compare(allTags.get(o2), allTags.get(o1));
            }
        });

        int topCount = Math.min(3, topTags.size());
        topTags = topTags.subList(0, topCount);

        return topTags;
    }

    @RequestMapping(value = "/drafts", method = RequestMethod.GET)
    public String draftsRedirect() {
        return "redirect:/drafts/page1";
    }

    @RequestMapping(value = "/drafts/page{num}")
    public String drafts(Model model, @AuthenticationPrincipal User currentUser, @PathVariable Integer num) {
        Page page = materialService.findUserDrafts(currentUser, num - 1);
        model.addAttribute("materials", page.getContent());
        model.addAttribute("currentPage", page.getNumber() + 1);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("url", "/drafts/page");
        return "drafts";
    }

    @RequestMapping(value = "/favourites", method = RequestMethod.GET)
    public String favouritesRedirect() {
        return "redirect:/favourites/page1";
    }

    @RequestMapping(value = "/favourites/page{num}")
    public String favourites(Model model, @AuthenticationPrincipal User currentUser, @PathVariable Integer num) {
        User cur = getCurrentUser(currentUser);
        Page page = materialService.findUserFavourites(currentUser, num - 1);
        model.addAttribute("favourites", page.getContent());
        model.addAttribute("currentPage", page.getNumber() + 1);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("url", "/favourites/page");
        model.addAttribute("currentUser", cur);
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

    @RequestMapping("/search")
    public String search(@RequestParam(value = "q",required = false) String query, Model model, @AuthenticationPrincipal User currentUser) {
        if(query == null || query.equals("")) {
            model.addAttribute("materials", new ArrayList<Material>());
            model.addAttribute("query", "");
        } else {
            model.addAttribute("materials", materialService.search(query));
            model.addAttribute("query", query);
        }
        model.addAttribute("currentUser", currentUser);

        return "search_page";
    }
}
