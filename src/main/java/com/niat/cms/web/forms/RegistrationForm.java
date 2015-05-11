package com.niat.cms.web.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author gtament
 */
public class RegistrationForm {

    @NotNull
    @Size(min = 4, max = 30, message = "Логин должен быть от {min} до {max} символов в длину")
    private String username;

    @NotNull
    @Size(min = 4, max = 30, message = "Пароль должен быть от {min} до {max} символов в длину")
    private String password;

    @NotNull
    private String passwordConfirm;

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

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}