package com.niat.cms.web;

import com.niat.cms.domain.User;
import com.niat.cms.service.UserService;
import com.niat.cms.web.forms.RegistrationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * @author gtament
 */

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registrationForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "registration";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String processRegistration(@Valid RegistrationForm registrationForm, BindingResult bindingResult) {
        if (userService.findByUsername(registrationForm.getUsername()) != null) {
            bindingResult.addError(new FieldError("registrationForm", "username", "Username already in use"));
        }
        if (!registrationForm.getPassword().equals(registrationForm.getRepassword())) {
            bindingResult.addError(new FieldError("registrationForm", "password", "Passwords don't match"));
        }
        if(bindingResult.hasErrors()) {
            return "registration";
        }

        User user = new User(registrationForm.getUsername(), passwordEncoder.encode(registrationForm.getPassword()), User.Role.AUTHOR);
        userService.save(user);

        return "redirect:/login";
    }

    @RequestMapping(value = "/login")
    public String login() {
        return "/login";
    }
}
