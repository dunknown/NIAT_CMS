package com.niat.cms.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author dunknown
 */
@Controller
public class MainController {

    @RequestMapping("/")
    public String main() {
        return "main";
    }
}
