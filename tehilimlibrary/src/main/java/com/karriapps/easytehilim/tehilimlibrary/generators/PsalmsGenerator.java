package com.karriapps.easytehilim.tehilimlibrary.generators;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.karriapps.easytehilim.tehilimlibrary.R;
import com.karriapps.easytehilim.tehilimlibrary.model.Perek;
import com.karriapps.easytehilim.tehilimlibrary.model.Perek.EXPANDANBLE;
import com.karriapps.easytehilim.tehilimlibrary.utils.Alef;
import com.karriapps.easytehilim.tehilimlibrary.utils.App;
import com.karriapps.easytehilim.tehilimlibrary.utils.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PsalmsGenerator extends TehilimGenerator {

    private int mStartChapter;
    private int mEndChapter;
    private int mStartKufYud;
    private int mEndKufYud;
    private int mFirstChapterKey = -1;
    private int mLastChapterKey = -1;

    public PsalmsGenerator(int firstChapter, int lastChapter, int firstKufYud, int lastKufYud) {
        this.mStartChapter = firstChapter;
        this.mEndChapter = lastChapter;
        this.mStartKufYud = firstKufYud;
        this.mEndKufYud = lastKufYud;
        mPsalmsList = new HashMap<String, Perek>();
        mKeys = new ArrayList<String>();
    }

    private PsalmsGenerator(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public Map<String, Perek> generate() {

        Context context = App.getInstance().getAplicationContext();
        Perek perek = null;
        for (int i = 1; i < 151; i++) {

            String chapterTitle = App.getInstance().getLocale().getLanguage().equals("iw") ? new Alef(i).toString() : (i + "");

            if (i == 119) {
                for (int j = 1; j < 23; j++) {
                    String kufLetter = Tools.getKufYudLetter(j);
                    perek = new Perek(
                            119,
                            context.getString(R.string.chapterKuf, chapterTitle, kufLetter),
                            App.getInstance().getPsalms().getKufYudTetText(j),
                            true,
                            EXPANDANBLE.NONE);
                    perek.setInScope(j >= mStartKufYud && j <= mEndKufYud);
                    mPsalmsList.put(perek.getTitle(), perek);
                    mKeys.add(perek.getTitle());

                    if (i == mStartChapter && j == mStartKufYud) {
                        mFirstChapterKey = mKeys.size() - 1;
                    } else if (i == mEndChapter && j == mEndKufYud) {
                        mLastChapterKey = mKeys.size() - 1;
                    }
                }
            } else {
                perek = new Perek(
                        i,
                        String.format(context.getString(R.string.chapter), chapterTitle),
                        App.getInstance().getPsalms().getChapterText(i),
                        true,
                        EXPANDANBLE.NONE);
                perek.setInScope(i >= mStartChapter && i <= mEndChapter);
                mPsalmsList.put(perek.getTitle(), perek);
                mKeys.add(perek.getTitle());
            }

            if (i == mStartChapter && mFirstChapterKey == -1) {
                mFirstChapterKey = mKeys.size() - 1;
            } else if (i == mEndChapter && mLastChapterKey == -1) {
                mLastChapterKey = mKeys.size() - 1;
            }
        }

        return mPsalmsList;
    }

    public int getFirstVisibleChapter() {
        return mStartChapter;
    }

    public int getLastVisibleChapter() {
        return mEndChapter;
    }

    public int getFirstVisibleKufYudPart() {
        return mStartKufYud;
    }

    public int getLastVisibleKufYud() {
        return mEndKufYud;
    }

    @Override
    public int getFirstChapterKeyPosition() {
        return mFirstChapterKey;
    }

    @Override
    public int getLastChapterKeyPosition() {
        return mLastChapterKey;
    }

    public static final Parcelable.Creator<PsalmsGenerator> CREATOR = new Parcelable.Creator<PsalmsGenerator>() {
        public PsalmsGenerator createFromParcel(Parcel in) {
            return new PsalmsGenerator(in);
        }

        public PsalmsGenerator[] newArray(int size) {
            return new PsalmsGenerator[size];
        }
    };

    @Override
    public void writeToParcel(Parcel out, int flag) {
        super.writeToParcel(out, flag);
        out.writeInt(mStartChapter);
        out.writeInt(mEndChapter);
        out.writeInt(mFirstChapterKey);
        out.writeInt(mLastChapterKey);
        out.writeInt(mStartKufYud);
        out.writeInt(mEndKufYud);
    }

    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        mStartChapter = in.readInt();
        mEndChapter = in.readInt();
        mFirstChapterKey = in.readInt();
        mLastChapterKey = in.readInt();
        mStartKufYud = in.readInt();
        mEndKufYud = in.readInt();
    }
}
