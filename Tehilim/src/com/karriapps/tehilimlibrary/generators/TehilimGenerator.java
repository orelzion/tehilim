package com.karriapps.tehilimlibrary.generators;

import java.util.List;
import java.util.Map;

import android.os.Parcelable;

import com.karriapps.tehilimlibrary.Perek;

public abstract class TehilimGenerator implements ITehilimGenerator, Parcelable {
	
	protected Map<String, Perek> mPsalmsList;
	protected List<String> mKeys;

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
}
