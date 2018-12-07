package com.karriapps.tehilim.tehilimlibrary.generators;

import android.os.Parcel;

import com.karriapps.tehilim.tehilimlibrary.R;
import com.karriapps.tehilim.tehilimlibrary.model.Perek;
import com.karriapps.tehilim.tehilimlibrary.model.Perek.EXPANDANBLE;
import com.karriapps.tehilim.tehilimlibrary.utils.Alef;
import com.karriapps.tehilim.tehilimlibrary.utils.App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SickPrayerGenerator extends TehilimGenerator {

    public SickPrayerGenerator() {
    }

    private SickPrayerGenerator(Parcel in) {
        readFromParcel(in);
    }

    private final int[] psalms = new int[]{20, 6, 9, 13, 16, 17, 18, 22, 23, 25, 30, 31, 32, 33, 36, 37, 38, 39, 41, 49, 55, 56,
            69, 86, 88, 89, 90, 91, 102, 103, 104, 107, 116, 118, 142, 143, 128};

    @Override
    public Map<String, Perek> generate() {

        mPsalmsList = new HashMap<>();
        mKeys = new ArrayList<>();

        Perek p = new Perek(R.string.yehiTitle, R.string.yehiBefore, false, EXPANDANBLE.NONE);
        p.setInScope(true);
        mPsalmsList.put(p.getTitle(), p);
        mKeys.add(p.getTitle());

        for (int i = 0; i < psalms.length; i++) {
            String chapterTitle = App.getInstance().getLocale().getLanguage().equals("iw") ? new Alef(psalms[i]).toString() : (psalms[i] + "");

            p = new Perek(
                    psalms[i],
                    String.format(App.getInstance().getString(R.string.chapter), chapterTitle),
                    App.getInstance().getPsalms().getChapterText(psalms[i]),
                    true,
                    EXPANDANBLE.NONE);
            p.setInScope(true);
            mPsalmsList.put(p.getTitle(), p);
            mKeys.add(p.getTitle());
        }

        p = new Perek(null, null, false, EXPANDANBLE.NONE);
        p.setTitle(App.getInstance().getString(R.string.sickTitle));
        p.setText(String.format(App.getInstance().getString(R.string.afterSick), App.getInstance().getString(R.string.for_sick)));
        p.setInScope(true);
        mPsalmsList.put(p.getTitle(), p);
        mKeys.add(p.getTitle());

        return mPsalmsList;
    }

    @Override
    public int getFirstChapterKeyPosition() {
        return 0;
    }

    @Override
    public int getLastChapterKeyPosition() {
        return mKeys.size() - 1;
    }

    public static final Creator<SickPrayerGenerator> CREATOR = new Creator<SickPrayerGenerator>() {
        public SickPrayerGenerator createFromParcel(Parcel in) {
            return new SickPrayerGenerator(in);
        }

        public SickPrayerGenerator[] newArray(int size) {
            return new SickPrayerGenerator[size];
        }
    };
}
