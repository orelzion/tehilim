package com.karriapps.tehilim.tehilimlibrary.generators;

import android.os.Parcel;
import android.os.Parcelable;

import com.karriapps.tehilim.tehilimlibrary.R;
import com.karriapps.tehilim.tehilimlibrary.model.Perek;
import com.karriapps.tehilim.tehilimlibrary.model.Perek.EXPANDANBLE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShiraGenerator extends TehilimGenerator {

    public ShiraGenerator() {
    }

    private ShiraGenerator(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public Map<String, Perek> generate() {

        mPsalmsList = new HashMap<String, Perek>();
        mKeys = new ArrayList<String>();

        Perek p = new Perek(R.string.shiraTitle, R.string.shiraStart, false, EXPANDANBLE.NONE);
        p.setInScope(true);
        mPsalmsList.put(p.getTitle(), p);
        mKeys.add(p.getTitle());

        p = new Perek(R.string.shira1Title, R.string.shira1, false, EXPANDANBLE.NONE);
        p.setInScope(true);
        mPsalmsList.put(p.getTitle(), p);
        mKeys.add(p.getTitle());

        p = new Perek(R.string.shira2Title, R.string.shira2, false, EXPANDANBLE.NONE);
        p.setInScope(true);
        mPsalmsList.put(p.getTitle(), p);
        mKeys.add(p.getTitle());

        p = new Perek(R.string.shira3Title, R.string.shira3, false, EXPANDANBLE.NONE);
        p.setInScope(true);
        mPsalmsList.put(p.getTitle(), p);
        mKeys.add(p.getTitle());

        p = new Perek(R.string.shira4Title, R.string.shira4, false, EXPANDANBLE.NONE);
        p.setInScope(true);
        mPsalmsList.put(p.getTitle(), p);
        mKeys.add(p.getTitle());

        p = new Perek(R.string.shira5Title, R.string.shira5, false, EXPANDANBLE.NONE);
        p.setInScope(true);
        mPsalmsList.put(p.getTitle(), p);
        mKeys.add(p.getTitle());

        p = new Perek(R.string.shira6Title, R.string.shira6, false, EXPANDANBLE.NONE);
        p.setInScope(true);
        mPsalmsList.put(p.getTitle(), p);
        mKeys.add(p.getTitle());

        p = new Perek(R.string.shiraSofTitle, R.string.shiraSof, false, EXPANDANBLE.NONE);
        p.setInScope(true);
        mPsalmsList.put(p.getTitle(), p);
        mKeys.add(p.getTitle());

        p = new Perek(R.string.shiraTfilaTitle, R.string.tfilaAfterShira, false, EXPANDANBLE.NONE);
        p.setInScope(true);
        mPsalmsList.put(p.getTitle(), p);
        mKeys.add(p.getTitle());

        return mPsalmsList;
    }

    @Override
    public Map<String, Perek> getList() {
        return mPsalmsList;
    }

    @Override
    public List<String> getKeys() {
        return mKeys;
    }

    public static final Parcelable.Creator<ShiraGenerator> CREATOR = new Parcelable.Creator<ShiraGenerator>() {
        public ShiraGenerator createFromParcel(Parcel in) {
            return new ShiraGenerator(in);
        }

        public ShiraGenerator[] newArray(int size) {
            return new ShiraGenerator[size];
        }
    };

    @Override
    public int getFirstChapterKeyPosition() {
        return 0;
    }

    @Override
    public int getLastChapterKeyPosition() {
        return mKeys.size() - 1;
    }

}
