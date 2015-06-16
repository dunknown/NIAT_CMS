package com.niat.cms.web;

import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author dunknown
 */
@Controller
public class ErrorController implements org.springframework.boot.autoconfigure.web.ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(PATH)
    public String error(HttpServletRequest request, Model model) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        ErrorAttributes errorAttributes = new DefaultErrorAttributes();
        Map<String, Object> attr = errorAttributes.getErrorAttributes(requestAttributes, false);
        model.addAttribute("error", attr.get("error"));
        model.addAttribute("status", attr.get("status"));
        String message = (String)attr.get("message");
        if (message == null || message.equals("No message available")) {
            model.addAttribute("message", "");
        } else {
            model.addAttribute("message", attr.get("message"));
        }
        return "error";
    }


    @Override
    public String getErrorPath() {
        return PATH;
    }
}
