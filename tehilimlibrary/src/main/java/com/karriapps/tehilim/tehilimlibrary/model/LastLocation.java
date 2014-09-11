package com.karriapps.tehilim.tehilimlibrary.model;

import android.text.format.DateFormat;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by orelsara on 5/6/14.
 * Updated to use GSON on 18/08/14.
 * Added dateCreated field on 22/08/14.
 */
public class LastLocation implements IEditableChild {

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int[] getValues() {
        return mValues;
    }

    public void setValues(int... values) {
        this.mValues = values;
    }

    public GENERATOR_TYPE getGeneratorType() {
        return mGeneratorType;
    }

    public void setGeneratorType(GENERATOR_TYPE generatorType) {
        this.mGeneratorType = generatorType;
    }

    public Date getDateCreated() {
        return mDateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        mDateCreated = dateCreated;
    }

    public enum GENERATOR_TYPE {
        @SerializedName("0")
        CHAPTERS,
        @SerializedName("1")
        NAME,
        @SerializedName("2")
        YAHRZEIT
    }

    public void setPosition(int position) {
        this.mPosition = position;
    }

    public int getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mName;
    }

    @Override
    public String getSubtitle() {
        StringBuilder builder = new StringBuilder();
        builder.append(mValues[0]);
        builder.append(" - ");
        builder.append(mValues[1]);
        return builder.toString();
    }

    @Override
    public String getSideText() {
        String format = "dd/MM/yy HH:mm";
        return DateFormat.format(format, mDateCreated).toString();
    }

    @SerializedName("name")
    private String mName;
    @SerializedName("values")
    private int[] mValues;
    @SerializedName("type")
    private GENERATOR_TYPE mGeneratorType;
    @SerializedName("position")
    private int mPosition;
    @SerializedName("date")
    private Date mDateCreated;

    public LastLocation(String mName, int[] mValues, GENERATOR_TYPE mGeneratorType, int mPosition) {
        this.mName = mName;
        this.mValues = mValues;
        this.mGeneratorType = mGeneratorType;
        this.mPosition = mPosition;
        init();
    }

    public LastLocation() {
        init();
    }

    private void init() {
        mDateCreated = new Date();
    }
}
