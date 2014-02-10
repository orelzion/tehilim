package com.karriapps.tehilimlibrary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.karriapps.tehilimlibrary.R;
import com.karriapps.tehilimlibrary.Perek.EXPANDANBLE;
import com.karriapps.tehilimlibrary.utils.Alef;
import com.karriapps.tehilimlibrary.utils.App;
import com.karriapps.tehilimlibrary.utils.Tools;
import com.karriapps.tehilimlibrary.utils.App.LANGUAGES;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Tehilim extends Fragment implements OnTehilimUpdated{
		
	public static final String FIRST = "first";
	public static final String LAST = "last";
	public static final String KUF_FIRST = "kuf_S";
	public static final String KUF_LAST = "kuf_l";
	
	public static final String TITLE = "title";
	public static final String TEXT = "text";
	public static final String EXPAND = "expand";
	public static final String FAVORITE = "favorite";
	
	private ListView mList;
	private Button mUpperJumpTo, mBottomJumpTo;
	ArrayList<Perek> psalms = new ArrayList<Perek>();
	TehilimAdapter mAdapter;
	Typeface mFont;
	private int lastPosition = 0;
	private int firstChapter, lastChapter;
	private YehiRazonFragment mYehiDialog;
	private IFragmentViewCreated mViewCreatedListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.tehilim, container, false);
	}
	
	public void addListener(IFragmentViewCreated listener) {
		mViewCreatedListener = listener;
	}
	
	public void onViewCreated(View view, Bundle savedInstanceState) {
		mList = (ListView) view.findViewById(R.id.list);
		mAdapter = new TehilimAdapter();
		mList.setAdapter(mAdapter);
		mUpperJumpTo = (Button)view.findViewById(R.id.upperJumpTo);
		mUpperJumpTo.setOnClickListener(onClick);
		mBottomJumpTo = (Button)view.findViewById(R.id.bottomJumpTo);
		mBottomJumpTo.setOnClickListener(onClick);
		mList.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(view.getAdapter().getItemId(firstVisibleItem) <= firstChapter){
					mUpperJumpTo.setVisibility(View.VISIBLE);
				} else
					mUpperJumpTo.setVisibility(View.GONE);
				
				if(lastChapter <= view.getAdapter().getItemId((firstVisibleItem + (visibleItemCount - 1)))){
					mBottomJumpTo.setVisibility(View.VISIBLE);
				} else
					mBottomJumpTo.setVisibility(View.GONE);
			}
		});
		switch (App.getInstance().getAppTheme()) {
			case DARK:
				App.getInstance().setTheme(R.style.AppBaseThemeDark);
				break;
	
			case LIGHT:
				App.getInstance().setTheme(R.style.AppBaseTheme);
				break;
		}
		switch(App.getInstance().getFont()){
			case TIMES:
				mFont = Typeface.createFromAsset(App.getInstance().getAssets(),"TIMES.ttf");
				break;
			case GUTTMAN:
				mFont = Typeface.createFromAsset(App.getInstance().getAssets(),"Guttman Keren-Normal.ttf");
				break;
			case NARKIS:
				mFont = Typeface.createFromAsset(App.getInstance().getAssets(),"NRKIS.ttf"); 
				break;
			case ALEF:
				mFont = Typeface.createFromAsset(App.getInstance().getAssets(),"Alef-Regular.ttf");
				break;
			case KETER:
				mFont = Typeface.createFromAsset(App.getInstance().getAssets(),"KeterYG-Medium.ttf");
				break;
		}
		
		buildTehilimList();
	};
	
	OnClickListener onClick = new OnClickListener() {
		Bundle b = new Bundle();
		
		@Override
		public void onClick(View v) {
			b.putBoolean("before", v.equals(mUpperJumpTo));
			mYehiDialog = new YehiRazonFragment();
			mYehiDialog.setArguments(b);
			mYehiDialog.show(getFragmentManager(), "yehi");
		}
	};
	
	@Override
	public void onTehilimUpdated(int firstChapterID, int firstKufYudPosition) {
		mAdapter.notifyDataSetChanged();
		final int currentPosition = mAdapter.getItemPositionByID(firstChapterID);
		mList.setSelection(currentPosition);
	}
	
	public void updateTehilimList(List<Integer> ChaptersNumbers, List<Integer> KufYudNumbers){
		firstChapter = ChaptersNumbers.get(0);
		lastChapter = ChaptersNumbers.get(ChaptersNumbers.size() - 1);
		
		for(int i = 0; i < mAdapter.getCount(); i++){
			int chapterID = (int) mAdapter.getItemId(i);
			mAdapter.getItem(i).setInScope(ChaptersNumbers.contains(chapterID));
			if(chapterID == 119){
				int kufID = i - mAdapter.getItemPositionByID(119) + 1;
				mAdapter.getItem(i).setInScope(KufYudNumbers.contains(kufID));
			}
		}
		onTehilimUpdated(firstChapter, KufYudNumbers.size() > 0 ? KufYudNumbers.get(0) : 0);
	}
	
	private void buildTehilimList(){
		
		Context context = App.getInstance().getAplicationContext();
		Perek perek = null;
		for(int i = 1; i < 151; i++){
			
			String chapterTitle = App.getInstance().getLang().equals(LANGUAGES.HEBREW) ? new Alef(i).toString() : (i + "");
			
			if(i == 119){
				for(int j = 1; j < 23; j++){
					String kufLetter = Tools.getKufYudLetter(j);
					perek = new Perek(
							119,
							String.format(context.getString(R.string.chapterKuf), chapterTitle, kufLetter), 
							App.getInstance().getPsalms().getKufYudTetText(j), 
							true, 
							EXPANDANBLE.NONE);
					psalms.add(perek);
				}
			}
			else
			{
				perek = new Perek(
						i,
						String.format(context.getString(R.string.chapter), chapterTitle), 
						App.getInstance().getPsalms().getChapterText(i), 
						true, 
						EXPANDANBLE.NONE);
				psalms.add(perek);
			}
		}
	}
	
	public void scrollToChapter(int chapterID){
		int position = mAdapter.getItemPositionByID(chapterID);
		if(position != 0)
			mList.setSelection(position);
	}
	
	public int getLastPosition() {
		return lastPosition;
	}

	private void setLastPosition(int lastPosition) {
		this.lastPosition = lastPosition;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		setLastPosition(mList.getFirstVisiblePosition());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		scrollToChapter(getLastPosition());
	}

	class TehilimAdapter extends BaseAdapter{
		
		TextView title, text;
		ImageView favorite, expand;

		@Override
		public int getCount() {
			return psalms.size();
		}

		@Override
		public Perek getItem(int position) {
			return psalms.get(position);
		}

		@Override
		public long getItemId(int position) {
			if(psalms != null && psalms.size() > 0 && psalms.get(position) != null)
				return psalms.get(position).getchapterID();
			
			return 0;
		}
		
		public Perek getItemByID(int chapterID){
			Iterator<Perek> i = psalms.iterator();
			Perek p = null;
			while(i.hasNext())
			{
				p = i.next();
				if(p.getchapterID() == chapterID)
					break;
			}
			return p;
		}
		
		public int getItemPositionByID(int chapterID){
			for(int i = 0; i < psalms.size(); i++)
			{
				if(getItem(i).getchapterID() == chapterID)
					return i;
			}
			
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View view = convertView;
			LayoutInflater inflater = (LayoutInflater) App.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if(convertView == null){
				view = inflater.inflate(R.layout.tehilim_row, parent, false);
			}
			
			title = (TextView)view.findViewById(R.id.title);
			text = (TextView)view.findViewById(R.id.text);
//			expand = (ImageView)view.findViewById(R.id.expand);
//			favorite = (ImageView)view.findViewById(R.id.favorite);
			
			String tText = psalms.get(position).getTitle();
			if(!psalms.get(position).isInScope())
				tText = "<font color=\"#aaaaaa\">" + tText + "</font>";
			title.setText(Html.fromHtml(tText));
			
//			switch(psalms.get(position).getExpand()){
//				case EXPAND:
//					expand.setSelected(false);
//					expand.setVisibility(View.VISIBLE);
//					text.setVisibility(View.GONE);
//				break;
//				case COLLAPSE:
//					expand.setSelected(true);
//					expand.setVisibility(View.VISIBLE);
//					text.setVisibility(View.VISIBLE);
//				break;
//				case NONE:
//					expand.setVisibility(View.GONE);
//					text.setVisibility(View.VISIBLE);
//				break;
//			}
//			expand.setTag(position);
//			expand.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					int pos = (Integer) v.getTag();
//					if(psalms.get(pos).getExpand().equals(EXPANDANBLE.EXPAND))
//						psalms.get(pos).setExpand(EXPANDANBLE.COLLAPSE);
//					else 
//						psalms.get(pos).setExpand(EXPANDANBLE.EXPAND);
//					notifyDataSetChanged();
//				}
//			});
			
//			favorite.setVisibility(View.VISIBLE);
//			if(psalms.get(position).isShowFavoriteButton())
//				favorite.setVisibility(View.VISIBLE);
//			else
//				favorite.setVisibility(View.GONE);
//			favorite.setSelected(App.getInstance().isFavorite(psalms.get(position).getchapterID()));
//			favorite.setTag(position);
//			favorite.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					int pos = (Integer) v.getTag();
//					App.getInstance().toggleFavorite(pos);
//					notifyDataSetChanged();
//				}
//			});
			
			String pText = psalms.get(position).getText();
			if(!psalms.get(position).isInScope())
				pText = "<font color=\"#aaaaaa\">" + pText + "</font>";
			text.setText(Html.fromHtml(pText));
			text.setTypeface(mFont);
			
			return view;
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
