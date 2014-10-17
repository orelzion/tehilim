package com.karriapps.tehilim.tehilimlibrary.model;

import com.google.gson.annotations.SerializedName;
import com.karriapps.tehilim.tehilimlibrary.utils.Tools;

import java.util.Arrays;


/**
 * Created by Orel on 29/08/2014.
 */
public class FavoriteListItem implements IEditableChild {

    @SerializedName("title")
    private String mTitle;
    @SerializedName("values")
    private int[] mValues;

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSubtitle() {
        return mValues == null ? "" : Arrays.toString(mValues).replace("[", "").replace("]", "");
    }

    @Override
    public String getSideText() {
        return null;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setValues(int[] values) {
        mValues = values;
    }

    public void setValuesFromString(String values) throws IllegalArgumentException {
        mValues = Tools.getIntArrayFromCommaSeparatedString(values);
    }

    public int[] getValues() {
        return mValues;
    }
}
