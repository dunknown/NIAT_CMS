package com.niat.cms.web.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author gtament
 */
public class AddMaterialForm {

    @NotNull
    @Size(min = 4, max = 30)
    private String title;

    @NotNull
    @Size(min = 4)
    private String text;

    @NotNull
    @Size(min = 4)
    private String tags;

    private boolean onMain;

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

    public boolean isOnMain() {
        return onMain;
    }

    public void setOnMain(boolean onMain) {
        this.onMain = onMain;
    }
}
