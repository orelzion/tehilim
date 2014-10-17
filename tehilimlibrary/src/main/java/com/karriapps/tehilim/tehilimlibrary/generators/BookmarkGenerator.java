package com.karriapps.tehilim.tehilimlibrary.generators;

import android.os.Parcel;

import com.karriapps.tehilim.tehilimlibrary.R;
import com.karriapps.tehilim.tehilimlibrary.model.Perek;
import com.karriapps.tehilim.tehilimlibrary.utils.Alef;
import com.karriapps.tehilim.tehilimlibrary.utils.App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Orel on 07/10/2014.
 */
public class BookmarkGenerator extends TehilimGenerator {

    int[] psalms;

    public BookmarkGenerator(int... chapters) {
        super();
        psalms = chapters;
    }

    private BookmarkGenerator(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public Map<String, Perek> generate() {
        mPsalmsList = new HashMap<String, Perek>();
        mKeys = new ArrayList<String>();

        Perek p = new Perek(R.string.yehiTitle, R.string.yehiBefore, false, Perek.EXPANDANBLE.NONE);
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
                    Perek.EXPANDANBLE.NONE);
            p.setInScope(true);
            mPsalmsList.put(p.getTitle(), p);
            mKeys.add(p.getTitle());
        }

        p = new Perek(null, null, false, Perek.EXPANDANBLE.NONE);
        p.setTitle(App.getInstance().getString(R.string.yehiAfterTitle));
        p.setText(App.getInstance().getString(R.string.yehiAfter, App.getInstance().getString(R.string.for_partial_tehilim), App.getInstance().getString(R.string.for_full_tehilim)));
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

    public static final Creator<BookmarkGenerator> CREATOR = new Creator<BookmarkGenerator>() {
        public BookmarkGenerator createFromParcel(Parcel in) {
            return new BookmarkGenerator(in);
        }

        public BookmarkGenerator[] newArray(int size) {
            return new BookmarkGenerator[size];
        }
    };
}
