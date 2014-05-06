package com.karriapps.tehilimlibrary.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.karriapps.tehilimlibrary.Perek;
import com.karriapps.tehilimlibrary.R;
import com.karriapps.tehilimlibrary.Perek.EXPANDANBLE;
import com.karriapps.tehilimlibrary.utils.Alef;
import com.karriapps.tehilimlibrary.utils.App;
import com.karriapps.tehilimlibrary.utils.Tools;

public class YahrzeitGenerator extends TehilimGenerator {
	
	private List<Integer> mCharsList;
	private final int[] psalms = new int[] {16, 17, 32, 74, 91, 104, 130};
	
	public YahrzeitGenerator(List<Integer> psalms) {
		super();
		mCharsList = psalms;
	}
	
	private YahrzeitGenerator(Parcel in) {
		super();
        readFromParcel(in);
	}

	@Override
	public Map<String, Perek> generate() {
		
		Context context = App.getInstance().getAplicationContext();
		Perek perek = null;
		
		for(int i = 0; i < psalms.length; i++) {
			String chapterTitle = App.getInstance().getLocale().getLanguage().equals("iw") ? new Alef(psalms[i]).toString() : (psalms[i] + "");

				perek = new Perek(
						psalms[i],
						String.format(App.getInstance().getString(R.string.chapter), chapterTitle), 
						App.getInstance().getPsalms().getChapterText(psalms[i]), 
						true, 
						EXPANDANBLE.NONE);
				perek.setInScope(true);
				mPsalmsList.put(perek.getTitle(), perek);
				mKeys.add(perek.getTitle());
		}

		for(int i = 0; i < mCharsList.size(); i++) {
			String kufLetter = Tools.getKufYudLetter(mCharsList.get(i));
			perek = new Perek(
					119,
					context.getString(R.string.kufLetter, kufLetter), 
					App.getInstance().getPsalms().getKufYudTetText(mCharsList.get(i)), 
					true, 
					EXPANDANBLE.NONE);
			perek.setInScope(true);
			mPsalmsList.put(perek.getTitle(), perek);
			mKeys.add(perek.getTitle());
		}
		
		String kufLetter = Tools.getKufYudLetter(14);
		perek = new Perek(
				119,
				context.getString(R.string.neshma, kufLetter), 
				App.getInstance().getPsalms().getKufYudTetText(14), 
				true, 
				EXPANDANBLE.NONE);
		perek.setInScope(true);
		mPsalmsList.put(perek.getTitle(), perek);
		mKeys.add(perek.getTitle());
		
		kufLetter = Tools.getKufYudLetter(21);
		perek = new Perek(
				119,
				context.getString(R.string.neshma, kufLetter), 
				App.getInstance().getPsalms().getKufYudTetText(21), 
				true, 
				EXPANDANBLE.NONE);
		perek.setInScope(true);
		mPsalmsList.put(perek.getTitle(), perek);
		mKeys.add(perek.getTitle());
		
		kufLetter = Tools.getKufYudLetter(13);
		perek = new Perek(
				119,
				context.getString(R.string.neshma, kufLetter), 
				App.getInstance().getPsalms().getKufYudTetText(13), 
				true, 
				EXPANDANBLE.NONE);
		perek.setInScope(true);
		mPsalmsList.put(perek.getTitle(), perek);
		mKeys.add(perek.getTitle());
		
		kufLetter = Tools.getKufYudLetter(5);
		perek = new Perek(
				119,
				context.getString(R.string.neshma, kufLetter), 
				App.getInstance().getPsalms().getKufYudTetText(5), 
				true, 
				EXPANDANBLE.NONE);
		perek.setInScope(true);
		mPsalmsList.put(perek.getTitle(), perek);
		mKeys.add(perek.getTitle());

        perek = new Perek(
                R.string.kadishAshkenazTitle,
                R.string.kadishAshkenaz,
                false,
                EXPANDANBLE.COLLAPSE);
        perek.setInScope(true);
        mPsalmsList.put(perek.getTitle(), perek);
        mKeys.add(perek.getTitle());

		perek = new Perek(
                R.string.kadishEdotTitle,
                R.string.kadishEdot,
                false,
                EXPANDANBLE.COLLAPSE);
        perek.setInScope(true);
        mPsalmsList.put(perek.getTitle(), perek);
        mKeys.add(perek.getTitle());

        perek = new Perek(
                R.string.kadishChabadTitle,
                R.string.kadishChabad,
                false,
                EXPANDANBLE.COLLAPSE);
        perek.setInScope(true);
        mPsalmsList.put(perek.getTitle(), perek);
        mKeys.add(perek.getTitle());

        perek = new Perek(
                R.string.kadishSefaradTitle,
                R.string.kadishSefarad,
                false,
                EXPANDANBLE.COLLAPSE);
        perek.setInScope(true);
        mPsalmsList.put(perek.getTitle(), perek);
        mKeys.add(perek.getTitle());

        perek = new Perek(
                R.string.elMaleManTitle,
                R.string.elMaleMan,
                false,
                EXPANDANBLE.COLLAPSE);
        perek.setInScope(true);
        mPsalmsList.put(perek.getTitle(), perek);
        mKeys.add(perek.getTitle());

        perek = new Perek(
                R.string.elMaleWomanTitle,
                R.string.elMaleWoman,
                false,
                EXPANDANBLE.COLLAPSE);
        perek.setInScope(true);
        mPsalmsList.put(perek.getTitle(), perek);
        mKeys.add(perek.getTitle());

        perek = new Perek(
                R.string.hashkavaManTitle,
                R.string.hashkavaMan,
                false,
                EXPANDANBLE.COLLAPSE);
        perek.setInScope(true);
        mPsalmsList.put(perek.getTitle(), perek);
        mKeys.add(perek.getTitle());

        perek = new Perek(
                R.string.hashkavaWomanTitle,
                R.string.haskavaWoman,
                false,
                EXPANDANBLE.COLLAPSE);
        perek.setInScope(true);
        mPsalmsList.put(perek.getTitle(), perek);
        mKeys.add(perek.getTitle());

		return mPsalmsList;
	}

	@Override
	public int getFirstChapterKeyPosition() {
		return 0;
	}

	@Override
	public int getLastChapterKeyPosition() {
		return 0;
	}

    public static final Parcelable.Creator<YahrzeitGenerator> CREATOR = new Parcelable.Creator<YahrzeitGenerator>() {
        public YahrzeitGenerator createFromParcel(Parcel in) {
        return new YahrzeitGenerator(in);
        }

        public YahrzeitGenerator[] newArray(int size) {
        return new YahrzeitGenerator[size];
        }
    };
    
    @Override
    public void writeToParcel(Parcel out, int flag) {
    	super.writeToParcel(out, flag);
    	out.writeSerializable((ArrayList<Integer>) mCharsList);
    };
    
    @Override
    protected void readFromParcel(Parcel in) {
    	super.readFromParcel(in);
    	mCharsList = (ArrayList<Integer>) in.readSerializable();
    };
}