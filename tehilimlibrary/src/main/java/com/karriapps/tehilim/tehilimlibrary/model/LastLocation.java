package com.karriapps.tehilim.tehilimlibrary.model;

/**
 * Created by orelsara on 5/6/14.
 */
public class LastLocation {

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

    public enum GENERATOR_TYPE {CHAPTERS, NAME, YAHRZEIT}

    public void setPosition(int position) {
        this.mPosition = position;
    }

    public int getPosition() {
        return mPosition;
    }

    private String mName;
    private int[] mValues;
    private GENERATOR_TYPE mGeneratorType;
    private int mPosition;

    public LastLocation(String mName, int[] mValues, GENERATOR_TYPE mGeneratorType, int mPosition) {
        this.mName = mName;
        this.mValues = mValues;
        this.mGeneratorType = mGeneratorType;
        this.mPosition = mPosition;
    }

    public LastLocation() {
    }
}
