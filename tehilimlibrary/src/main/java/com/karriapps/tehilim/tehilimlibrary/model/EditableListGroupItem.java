package com.karriapps.tehilim.tehilimlibrary.model;

import java.util.List;

/**
 * Created by Orel on 28/08/2014.
 */
public class EditableListGroupItem {

    private String mTitle;
    private boolean mEditable;
    private List<IEditableChild> mChildren;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public boolean isEditable() {
        return mEditable;
    }

    public void setmEditable(boolean editable) {
        this.mEditable = editable;
    }

    public List<IEditableChild> getChildren() {
        return mChildren;
    }

    public void setChildren(List<IEditableChild> mChildren) {
        this.mChildren = mChildren;
    }
}
