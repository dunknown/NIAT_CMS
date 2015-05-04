package com.niat.cms.web.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author gtament
 */
public class RegistrationForm {

    @NotNull
    @Size(min = 4, max = 30, message = "Username must be between {min} and {max} characters long")
    private String username;

    @NotNull
    @Size(min = 4, max = 30, message = "Password must be between {min} and {max} characters long")
    private String password;

    @NotNull
    @Size(min = 4, max = 30)
    private String repassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }
}