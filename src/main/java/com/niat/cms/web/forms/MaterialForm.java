package com.niat.cms.web.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author gtament
 */
public class MaterialForm {

    @NotNull
    @Size(min = 4, max = 100, message = "Заголовок должен быть от {min} до {max} символов в длину")
    private String title;

    @NotNull
    @Size(min = 4, message = "Текст должен быть не менее {min} символов в длину")
    private String text;

    @NotNull
    private String tags;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
