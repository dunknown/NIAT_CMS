package com.niat.cms.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author gtament
 */

@Controller
@RequestMapping(value = "/admin")
public class AdminPagesController {

    @RequestMapping(value = "/editmain")
    public String editMain() {
        return "edit_main";
    }

    @RequestMapping(value = "/users")
    public String users() {
        return "users";
    }

    @RequestMapping(value = "/addmaterial")
    public String addMaterial() {
        return "addmaterial";
    }
}
