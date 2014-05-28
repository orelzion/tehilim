package com.karriapps.easytehilim.tehilimlibrary.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.karriapps.easytehilim.tehilimlibrary.utils.App;

public class Perek implements Parcelable {

    public enum EXPANDANBLE {EXPAND, COLLAPSE, NONE}

    private String title, text;
    private EXPANDANBLE expand;
    private int chapterID;
    private boolean showFavoriteButton;
    private boolean mInScope;

    public Perek(String title, String text, boolean showFavoriteButton, EXPANDANBLE expand) {
        init(-1, title, text, showFavoriteButton, expand);
    }

    public Perek(int chapterID, String title, String text, boolean showFavoriteButton, EXPANDANBLE expand) {
        init(chapterID, title, text, showFavoriteButton, expand);
    }

    public Perek(int chapterID, int title, int text, boolean showFavoriteButton, EXPANDANBLE expand) {
        Context context = App.getInstance().getApplicationContext();
        init(chapterID, context.getString(title), context.getString(text), showFavoriteButton, expand);
    }

    public Perek(int title, int text, boolean showFavoriteButton, EXPANDANBLE expand) {
        Context context = App.getInstance().getApplicationContext();
        init(-1, context.getString(title), context.getString(text), showFavoriteButton, expand);
    }

    private Perek(Parcel in) {
        readFromParcel(in);
    }

    private void init(int chapterID, String title, String text, boolean showFavoriteButton, EXPANDANBLE expand) {
        this.setchapterID(chapterID);
        this.setTitle(title);
        this.setText(text);
        this.setShowFavoriteButton(showFavoriteButton);
        this.setExpand(expand);
    }

    public String getTitle() {
        return title;
    }

    public Perek setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getText() {
        return text;
    }

    public Perek setText(String text) {
        this.text = text;
        return this;
    }

    public EXPANDANBLE getExpand() {
        return expand;
    }

    public Perek setExpand(EXPANDANBLE expand) {
        this.expand = expand;
        return this;
    }

    public int getchapterID() {
        return chapterID;
    }

    public Perek setchapterID(int chapterID) {
        this.chapterID = chapterID;
        return this;
    }

    public boolean isShowFavoriteButton() {
        return showFavoriteButton;
    }

    public Perek setShowFavoriteButton(boolean showFavoriteButton) {
        this.showFavoriteButton = showFavoriteButton;
        return this;
    }

    public Perek setInScope(boolean InScope) {
        this.mInScope = InScope;
        return this;
    }

    public Boolean isInScope() {
        return mInScope;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Perek> CREATOR = new Creator<Perek>() {
        public Perek createFromParcel(Parcel in) {
            return new Perek(in);
        }

        public Perek[] newArray(int size) {
            return new Perek[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(text);
        dest.writeString(expand.name());
        dest.writeInt(chapterID);
        dest.writeInt(showFavoriteButton ? 0 : 1);
        dest.writeInt(mInScope ? 0 : 1);
    }

    private void readFromParcel(Parcel in) {
        title = in.readString();
        text = in.readString();
        expand = EXPANDANBLE.valueOf(in.readString());
        chapterID = in.readInt();
        showFavoriteButton = in.readInt() == 0;
        mInScope = in.readInt() == 0;
    }
}
