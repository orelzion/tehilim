package com.karriapps.tehilimlibrary;

import java.lang.ref.WeakReference;

import com.karriapps.tehilimlibrary.generators.PsalmsGenerator;
import com.karriapps.tehilimlibrary.generators.TehilimGenerator;
import com.karriapps.tehilimlibrary.utils.App;
import com.karriapps.tehilimlibrary.utils.TehilimTextView;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

public class MainFragment extends Fragment {

	public enum VIEW_TYPE {TEHILIM_BOOK, OTHER};

	private ListView mList;
	private Button mUpperButton;
	private Button mBottomButton;

	private VIEW_TYPE mViewType;

	private TehilimGenerator mTehilimGenerator;

	private TehilimAdapter mAdapter;
	private ScaleGestureDetector mScaleDetector;
	
	private WeakReference<OnPositionChanged> mOnPositionChangedListener;
	
	private int mPosition;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.tehilim, container, false);

		mList = (ListView)view.findViewById(R.id.list);
		mUpperButton = (Button)view.findViewById(R.id.upperJumpTo);
		mBottomButton = (Button)view.findViewById(R.id.bottomJumpTo);
		mUpperButton.setOnClickListener(mOnClickListner);
		mBottomButton.setOnClickListener(mOnClickListner);

		mScaleDetector = new ScaleGestureDetector(getActivity(), new OnScaleGestureListener() {
			@Override
			public void onScaleEnd(ScaleGestureDetector detector) {
			}
			@Override
			public boolean onScaleBegin(ScaleGestureDetector detector) {
				return true;
			}
			@Override
			public boolean onScale(ScaleGestureDetector detector) {
				float factor = detector.getScaleFactor();
				//				if(Math.abs((detector.getCurrentSpan() - detector.getPreviousSpan())) >= 30) {
				if(factor >= 1) {
					App.getInstance().setFontSize(App.getInstance().getFontSize() + 0.5f);
				} else {
					App.getInstance().setFontSize(App.getInstance().getFontSize() - 0.5f);
				}
				//				}
				mAdapter.notifyDataSetChanged();
				return false;
			}
		});

		mAdapter = new TehilimAdapter();
		mList.setAdapter(mAdapter);
		mList.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getPointerCount() == 2)
					return mScaleDetector.onTouchEvent(event);
				else 
					return false;
			}
		});
		mList.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {}
			
			@Override
			public void onScroll(AbsListView list, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if(mPosition != firstVisibleItem) {
					mPosition = firstVisibleItem;
					if(mOnPositionChangedListener != null && mOnPositionChangedListener.get() != null) {
						mOnPositionChangedListener.get().onPositionChanged(mPosition);
					}
				}
			}
		});

		return view;
	}

	OnClickListener mOnClickListner = new OnClickListener() {

		@Override
		public void onClick(View v) {
			YehiRazonFragment fragment = new YehiRazonFragment();
			Bundle b = new Bundle();
			b.putBoolean(YehiRazonFragment.TYPE_KEY, v.equals(mUpperButton));
			fragment.setArguments(b);
			fragment.show(getActivity().getSupportFragmentManager(), "yehi");
		}
	};
	
	public void setOnPositionChangedListener(OnPositionChanged listener) {
		mOnPositionChangedListener = new WeakReference<OnPositionChanged>(listener);
	}

	public void setViewType(VIEW_TYPE type) {
		mViewType = type;

		if(mViewType.equals(VIEW_TYPE.TEHILIM_BOOK)) {
			mList.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {

					if(getTehilimGenerator() instanceof PsalmsGenerator) {
						if(firstVisibleItem <= getTehilimGenerator().getFirstChapterKeyPosition() ){
							mUpperButton.setVisibility(View.VISIBLE);
						} else
							mUpperButton.setVisibility(View.GONE);

						if(getTehilimGenerator().getLastChapterKeyPosition() <= 
								(firstVisibleItem + (visibleItemCount - 1))){
							mBottomButton.setVisibility(View.VISIBLE);
						} else
							mBottomButton.setVisibility(View.GONE);
					}
				}
			});
		} else {
			mUpperButton.setVisibility(View.GONE);
			mBottomButton.setVisibility(View.GONE);
		}
	}

	public int getPosition() {
		return mPosition;
	}

	public void setPosition(int position) {
		mList.setSelection(position);
	}
	
	public void setPosition(String key) {
		if(mTehilimGenerator != null) {
			setPosition(mTehilimGenerator.getKeys().indexOf(key));
		}
	}

	public TehilimGenerator getTehilimGenerator() {
		return mTehilimGenerator;
	}

	public void setTehilimGenerator(TehilimGenerator mTehilimGenerator) {
		this.mTehilimGenerator = mTehilimGenerator;
		mAdapter.notifyDataSetChanged();
	}

	private class TehilimAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return getTehilimGenerator() == null ? 0 : getTehilimGenerator().getKeys().size();
		}

		@Override
		public Perek getItem(int position) {
			return getTehilimGenerator().getList().get(getTehilimGenerator().getKeys().get(position));
		}

		@Override
		public long getItemId(int position) {
			if(getTehilimGenerator() instanceof PsalmsGenerator)
				return getTehilimGenerator().getList().get(getTehilimGenerator().getKeys().get(position)).getchapterID();
			else 
				return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;

			if(convertView == null) {
				LayoutInflater inflater = (LayoutInflater) App.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.tehilim_row, parent, false);
				holder.mTextView = (TehilimTextView)convertView.findViewById(R.id.text);
				holder.mTitleTextView = (TehilimTextView)convertView.findViewById(R.id.title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			String tText = getItem(position).getTitle();
			if(!getItem(position).isInScope())
				tText = "<font color=\"#aaaaaa\">" + tText + "</font>";
			holder.mTitleTextView.setText(Html.fromHtml(tText));
			holder.mTitleTextView.setTextSize(App.getInstance().getFontSize());

			//			if(!getItem(position).getExpand().equals(EXPANDANBLE.NONE)) {
			//				holder.mTitleTextView.setTag(position);
			//				holder.mTitleTextView.setOnClickListener(new OnClickListener() {
			//					
			//					@Override
			//					public void onClick(View v) {
			//						int position = v.getTag();
			//						EXPANDANBLE expand = getItem(position).getExpand();
			//						if(expand.equals(EXPANDANBLE.EXPAND)) {
			//							getItem(position)
			//						}
			//					}
			//				});
			//			}

			String pText = getItem(position).getText();
			if(!getItem(position).isInScope())
				pText = "<font color=\"#aaaaaa\">" + pText + "</font>";
			holder.mTextView.setText(Html.fromHtml(pText));
			holder.mTextView.setTextSize(App.getInstance().getFontSize());

			return convertView;
		}

		class ViewHolder {
			TehilimTextView mTitleTextView;
			TehilimTextView mTextView;
		}
	}
}
