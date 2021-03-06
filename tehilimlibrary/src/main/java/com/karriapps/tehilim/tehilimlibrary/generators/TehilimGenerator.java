package com.karriapps.tehilim.tehilimlibrary.generators;

import android.os.Parcel;
import android.os.Parcelable;

import com.karriapps.tehilim.tehilimlibrary.model.Perek;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TehilimGenerator implements ITehilimGenerator, Parcelable {

    protected Map<String, Perek> mPsalmsList;
    protected List<String> mKeys;

    public TehilimGenerator() {
        mPsalmsList = new HashMap<>();
        mKeys = new ArrayList<>();
    }

    @Override
    public abstract Map<String, Perek> generate();

    public List<String> getKeys() {
        return mKeys;
    }

    public Map<String, Perek> getList() {
        return mPsalmsList;
    }

    public abstract int getFirstChapterKeyPosition();

    public abstract int getLastChapterKeyPosition();

    @Override
    public void writeToParcel(Parcel out, int flag) {
        out.writeStringList(mKeys);
        for (int i = 0; i < mPsalmsList.size(); i++) {
            out.writeParcelable(mPsalmsList.get(mKeys.get(i)), 0);
        }
    }

    protected void readFromParcel(Parcel in) {
        mKeys = new ArrayList<String>();
        in.readStringList(mKeys);
        mPsalmsList = new HashMap<String, Perek>();
        for (int i = 0; i < mKeys.size(); i++) {
            mPsalmsList.put(mKeys.get(i), (Perek) in.readParcelable(Perek.class.getClassLoader()));
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
