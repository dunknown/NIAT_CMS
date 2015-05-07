package com.niat.cms.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author gtament
 */

@Controller
public class UserPagesController {

    @RequestMapping(value = "/")
    public String main() {
        return "main";
    }

    @RequestMapping(value = "/archive")
    public String archive() {
        return "archive";
    }
}
