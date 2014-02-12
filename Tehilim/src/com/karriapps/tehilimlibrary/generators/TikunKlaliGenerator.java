package com.karriapps.tehilimlibrary.generators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.karriapps.tehilimlibrary.Perek;
import com.karriapps.tehilimlibrary.Perek.EXPANDANBLE;
import com.karriapps.tehilimlibrary.utils.Alef;
import com.karriapps.tehilimlibrary.utils.App;
import com.karriapps.tehilimlibrary.R;

public class TikunKlaliGenerator extends TehilimGenerator {
	
	public TikunKlaliGenerator() {}
	
	private TikunKlaliGenerator(Parcel in) {
		readFromParcel(in);
	}
	
	private final int[] psalms = new int[] {16, 32, 41, 42, 59, 77, 90, 105, 137, 150};

	@Override
	public Map<String, Perek> generate() {
		
		mPsalmsList = new HashMap<String, Perek>();
		mKeys = new ArrayList<String>();
		
		Perek p = new Perek(R.string.yehiTitle, R.string.yehiBefore, false, EXPANDANBLE.COLLAPSE);
		p.setInScope(true);
		mPsalmsList.put(p.getTitle(), p);
		mKeys.add(p.getTitle());
		
		p = new Perek(R.string.tikunBeforeTitle, R.string.tikunBefore, false, EXPANDANBLE.NONE);
		p.setInScope(true);
		mPsalmsList.put(p.getTitle(), p);
		mKeys.add(p.getTitle());
		
		for(int i = 0; i < psalms.length; i++) {
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
		
		p = new Perek(R.string.tikunAfter, R.string.afterTikunKlali, false, EXPANDANBLE.COLLAPSE);
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

    public static final Parcelable.Creator<TikunKlaliGenerator> CREATOR = new Parcelable.Creator<TikunKlaliGenerator>() {
        public TikunKlaliGenerator createFromParcel(Parcel in) {
        return new TikunKlaliGenerator(in);
        }

        public TikunKlaliGenerator[] newArray(int size) {
        return new TikunKlaliGenerator[size];
        }
    };
}
