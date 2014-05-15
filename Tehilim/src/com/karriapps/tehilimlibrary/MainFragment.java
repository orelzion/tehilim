package com.karriapps.tehilimlibrary;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.karriapps.tehilimlibrary.generators.PsalmsGenerator;
import com.karriapps.tehilimlibrary.generators.TehilimGenerator;
import com.karriapps.tehilimlibrary.utils.App;
import com.karriapps.tehilimlibrary.utils.TehilimTextView;

import java.lang.ref.WeakReference;

public class MainFragment extends Fragment implements OnScrollListener {

    private ListView mList;
    private Button mUpperButton;
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
    private Button mBottomButton;

    private VIEW_TYPE mViewType;

    private TehilimGenerator mTehilimGenerator;

    private TehilimAdapter mAdapter;
    private ScaleGestureDetector mScaleDetector;

    private WeakReference<OnPositionChanged> mOnPositionChangedListener;

    private int mPosition;
    private boolean mIsScrolling;
    private AutoScroller mAutoScroller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tehilim, container, true);

        mList = (ListView) view.findViewById(R.id.list);
        mUpperButton = (Button) view.findViewById(R.id.upperJumpTo);
        mBottomButton = (Button) view.findViewById(R.id.bottomJumpTo);
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
                if (factor >= 1) {
                    App.getInstance().setFontSize(App.getInstance().getFontSize() + 0.5f);
                } else {
                    App.getInstance().setFontSize(App.getInstance().getFontSize() - 0.5f);
                }
                mAdapter.notifyDataSetChanged();
                return false;
            }
        });

        mAdapter = new TehilimAdapter();
        mList.setAdapter(mAdapter);
        mList.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getPointerCount() == 2)
                    return mScaleDetector.onTouchEvent(event);
                else
                    return false;
            }
        });
        mList.setOnScrollListener(this);

        return view;
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mPosition != firstVisibleItem) {
            mPosition = firstVisibleItem;
            if (mOnPositionChangedListener != null && mOnPositionChangedListener.get() != null) {
                mOnPositionChangedListener.get().onPositionChanged(mPosition);
            }
            if (mViewType.equals(VIEW_TYPE.TEHILIM_BOOK) && getTehilimGenerator() instanceof PsalmsGenerator) {

                if (firstVisibleItem <= getTehilimGenerator().getFirstChapterKeyPosition()) {
                    mUpperButton.setVisibility(View.VISIBLE);
                } else
                    mUpperButton.setVisibility(View.GONE);

                if (getTehilimGenerator().getLastChapterKeyPosition() <=
                        (firstVisibleItem + (visibleItemCount - 1))) {
                    mBottomButton.setVisibility(View.VISIBLE);
                } else
                    mBottomButton.setVisibility(View.GONE);

            }
        }
    }


    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }


    public void setOnPositionChangedListener(OnPositionChanged listener) {
        mOnPositionChangedListener = new WeakReference<OnPositionChanged>(listener);
    }

    public void setViewType(VIEW_TYPE type) {
        mViewType = type;

        if (!mViewType.equals(VIEW_TYPE.TEHILIM_BOOK)) {
            mUpperButton.setVisibility(View.GONE);
            mBottomButton.setVisibility(View.GONE);
        }
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(String key) {
        if (mTehilimGenerator != null) {
            setPosition(mTehilimGenerator.getKeys().indexOf(key));
        }
    }

    public void setPosition(int position) {
        mList.setSelection(position);
    }

    public boolean isScrolling() {
        return mIsScrolling;
    }

    /**
     * Auto scroll list view
     */
    public void toggleScroll(boolean scroll) {
        if(scroll) {
            if(mAutoScroller == null) {
                mAutoScroller = new AutoScroller(this);
            }
            mAutoScroller.sendEmptyMessage(AutoScroller.MESSAGE_SCROLL);
        } else {
            if(mAutoScroller != null) {
                if(mAutoScroller.hasMessages(AutoScroller.MESSAGE_SCROLL))
                    mAutoScroller.removeMessages(AutoScroller.MESSAGE_SCROLL);
            }
            mIsScrolling = false;
        }
    }

    private static class AutoScroller extends Handler {
        private WeakReference<MainFragment> mFragment;
        public static final int MESSAGE_SCROLL = 0;

        public AutoScroller(MainFragment fragment) {
            mFragment = new WeakReference<MainFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what == MESSAGE_SCROLL) {
              if(mFragment != null && mFragment.get() != null &&
                      mFragment.get().mList != null) {
                  mFragment.get().mList.smoothScrollBy(2, 0);
                  sendEmptyMessageDelayed(MESSAGE_SCROLL, (long) (App.getInstance().getScrollValue() /
                                    App.getInstance().getFontSize() * 10));
                  mFragment.get().mIsScrolling = true;
              }
            }
        }
    }

    public TehilimGenerator getTehilimGenerator() {
        return mTehilimGenerator;
    }

    public void setTehilimGenerator(TehilimGenerator mTehilimGenerator) {
        this.mTehilimGenerator = mTehilimGenerator;
        mAdapter.notifyDataSetChanged();
        setPosition(0);
    }

    public enum VIEW_TYPE {TEHILIM_BOOK, OTHER}

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
            if (getTehilimGenerator() instanceof PsalmsGenerator)
                return getTehilimGenerator().getList().get(getTehilimGenerator().getKeys().get(position)).getchapterID();
            else
                return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.tehilim_row, parent, false);
                holder.mTextView = (TehilimTextView) convertView.findViewById(R.id.text);
                holder.mTitleTextView = (TehilimTextView) convertView.findViewById(R.id.title);
                holder.mExpandIcon = (ImageView) convertView.findViewById(R.id.expand);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String tText = getItem(position).getTitle();
            if (!getItem(position).isInScope())
                tText = "<font color=\"#aaaaaa\">" + tText + "</font>";
            holder.mTitleTextView.setText(Html.fromHtml(tText));
            holder.mTitleTextView.setTextSize(App.getInstance().getFontSize());

            if (!getItem(position).getExpand().equals(Perek.EXPANDANBLE.NONE)) {
                holder.mTitleTextView.setTag(position);
                holder.mTitleTextView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int position = (Integer) v.getTag();
                        Perek.EXPANDANBLE expand = getItem(position).getExpand();
                        if (expand.equals(Perek.EXPANDANBLE.EXPAND)) {
                            getItem(position).setExpand(Perek.EXPANDANBLE.COLLAPSE);
                            notifyDataSetChanged();
                        } else if (expand.equals(Perek.EXPANDANBLE.COLLAPSE)) {
                            getItem(position).setExpand(Perek.EXPANDANBLE.EXPAND);
                            notifyDataSetChanged();
                        }
                    }
                });
            }

            holder.mTextView.setVisibility(getItem(position).getExpand().equals(Perek.EXPANDANBLE.COLLAPSE) ?
                    View.GONE : View.VISIBLE);


            holder.mExpandIcon.setVisibility(getItem(position).getExpand().equals(Perek.EXPANDANBLE.NONE) ?
                    View.GONE : View.VISIBLE);


            String pText = getItem(position).getText();
            if (!getItem(position).isInScope())
                pText = "<font color=\"#aaaaaa\">" + pText + "</font>";
            holder.mTextView.setText(Html.fromHtml(pText));
            holder.mTextView.setTextSize(App.getInstance().getFontSize());

            return convertView;
        }

        class ViewHolder {
            TehilimTextView mTitleTextView;
            TehilimTextView mTextView;
            ImageView mExpandIcon;
        }
    }
}
