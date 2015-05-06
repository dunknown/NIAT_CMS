package com.niat.cms.web;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author gtament
 */
public class EditMainController {

    @RequestMapping(value = "/editmain")
    public String editMain() {
        return "edit_main";
    }
}
