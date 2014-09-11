package com.karriapps.tehilim.tehilimlibrary.model;

/**
 * Created by Orel on 28/08/2014.
 */
public class EditableListGroupItem {

    private String mTitle;
    private boolean mEditable;
    private IEditableChild[] mChildren;

    public String getTitle() {
        return mTitle;
    }

    public EditableListGroupItem setTitle(String title) {
        this.mTitle = title;
        return this;
    }

    public boolean isEditable() {
        return mEditable;
    }

    public EditableListGroupItem setmEditable(boolean editable) {
        this.mEditable = editable;
        return this;
    }

    public IEditableChild[] getChildren() {
        return mChildren == null ? new IEditableChild[0] : mChildren;
    }

    public EditableListGroupItem setChildren(IEditableChild[] mChildren) {
        this.mChildren = mChildren;
        return this;
    }
}
