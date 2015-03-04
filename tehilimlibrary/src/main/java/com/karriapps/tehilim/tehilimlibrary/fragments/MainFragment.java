package com.karriapps.tehilim.tehilimlibrary.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
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
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.karriapps.tehilim.tehilimlibrary.R;
import com.karriapps.tehilim.tehilimlibrary.generators.PsalmsGenerator;
import com.karriapps.tehilim.tehilimlibrary.generators.TehilimGenerator;
import com.karriapps.tehilim.tehilimlibrary.model.Perek;
import com.karriapps.tehilim.tehilimlibrary.model.callbacks.OnPositionChanged;
import com.karriapps.tehilim.tehilimlibrary.utils.App;
import com.karriapps.tehilim.tehilimlibrary.view.TehilimTextView;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.lang.ref.WeakReference;

public class MainFragment extends Fragment implements OnScrollListener, ObservableScrollViewCallbacks {

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;
    private static final boolean TOOLBAR_IS_STICKY = true;

    private ObservableListView mList;
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

    private View mToolbar;
    private ImageView mImageView;
    private View mOverlayView;
    private View mListBackgroundView;
    private TextView mTitleView;
    private int mActionBarSize;
    private int mFlexibleSpaceImageHeight;
    private int mFabMargin;
    private int mToolbarColor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tehilim, container, true);

        mList = (ObservableListView) view.findViewById(R.id.list);
        mUpperButton = (Button) view.findViewById(R.id.upperJumpTo);
        mBottomButton = (Button) view.findViewById(R.id.bottomJumpTo);
        mUpperButton.setOnClickListener(mOnClickListner);
        mBottomButton.setOnClickListener(mOnClickListner);

        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mActionBarSize = getActionBarSize();
        mToolbarColor = getResources().getColor(R.color.titleColor);

        mToolbar = view.findViewById(R.id.app_toolbar);

        mImageView = (ImageView) view.findViewById(R.id.image);
        mOverlayView = view.findViewById(R.id.overlay);

        mList.setScrollViewCallbacks(this);

        // Set padding view for ListView. This is the flexible space.
        View paddingView = new View(getActivity());
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                mFlexibleSpaceImageHeight);
        paddingView.setLayoutParams(lp);

        // This is required to disable header's list selector effect
        paddingView.setClickable(true);

        mList.addHeaderView(paddingView);
        mTitleView = (TextView) view.findViewById(R.id.text_view_title);

        // mListBackgroundView makes ListView's background except header view.
        mListBackgroundView = view.findViewById(R.id.list_background);
        final View contentView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        contentView.post(new Runnable() {
            @Override
            public void run() {
                // mListBackgroundView's should fill its parent vertically
                // but the height of the content view is 0 on 'onCreate'.
                // So we should get it with post().
                mListBackgroundView.getLayoutParams().height = contentView.getHeight();
            }
        });

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

    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = getActivity().obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    public void updateTitle() {
        final ActionBarActivity activity = (ActionBarActivity) getActivity();
        mTitleView.setText(activity.getSupportActionBar().getTitle());
        activity.getSupportActionBar().setTitle(null);
    }

    public void changeImage(int redID) {
        mImageView.setImageResource(redID);
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mPosition != firstVisibleItem) {
            mPosition = firstVisibleItem;
            if (mOnPositionChangedListener != null && mOnPositionChangedListener.get() != null) {
                mOnPositionChangedListener.get().onPositionChanged(mPosition);
            }
            if (mViewType.equals(VIEW_TYPE.TEHILIM_BOOK) && getTehilimGenerator() instanceof PsalmsGenerator) {

//                if (firstVisibleItem <= getTehilimGenerator().getFirstChapterKeyPosition()) {
//                    mUpperButton.setVisibility(View.VISIBLE);
//                } else {
//                    mUpperButton.setVisibility(View.GONE);
//                }
//                if (getTehilimGenerator().getLastChapterKeyPosition() <=
//                        (firstVisibleItem + (visibleItemCount - 1))) {
//                    mBottomButton.setVisibility(View.VISIBLE);
//                } else {
//                    mBottomButton.setVisibility(View.GONE);
//                }
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
        if (scroll) {
            if (mAutoScroller == null) {
                mAutoScroller = new AutoScroller(this);
            }
            mAutoScroller.sendEmptyMessage(AutoScroller.MESSAGE_SCROLL);
        } else {
            if (mAutoScroller != null) {
                if (mAutoScroller.hasMessages(AutoScroller.MESSAGE_SCROLL))
                    mAutoScroller.removeMessages(AutoScroller.MESSAGE_SCROLL);
            }
            mIsScrolling = false;
        }
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        // Translate overlay and image
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(mImageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Translate list background
        ViewHelper.setTranslationY(mListBackgroundView, Math.max(0, -scrollY + mFlexibleSpaceImageHeight));

        // Change alpha of overlay
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));

        // Scale title text
        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        setPivotXToTitle();
        ViewHelper.setPivotY(mTitleView, 0);
        ViewHelper.setScaleX(mTitleView, scale);
        ViewHelper.setScaleY(mTitleView, scale);

        // Translate title text
        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - mTitleView.getHeight() * scale);
        int titleTranslationY = maxTitleTranslationY - scrollY;
        if (TOOLBAR_IS_STICKY) {
            titleTranslationY = Math.max(0, titleTranslationY);
        }
        ViewHelper.setTranslationY(mTitleView, titleTranslationY);

        if (TOOLBAR_IS_STICKY) {
            // Change alpha of toolbar background
            if (-scrollY + mFlexibleSpaceImageHeight <= mActionBarSize) {
                mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(1, mToolbarColor));
                mToolbar.setVisibility(View.VISIBLE);
                mTitleView.setVisibility(View.INVISIBLE);
                ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(mTitleView.getText());
            } else {
                mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, mToolbarColor));
                mToolbar.setVisibility(View.INVISIBLE);
                mTitleView.setVisibility(View.VISIBLE);
            }
        } else {
            // Translate Toolbar
            if (scrollY < mFlexibleSpaceImageHeight) {
                ViewHelper.setTranslationY(mToolbar, 0);
            } else {
                ViewHelper.setTranslationY(mToolbar, -scrollY);
            }
        }
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        if (scrollState == ScrollState.DOWN) {
            showToolbar();
        } else if (scrollState == ScrollState.UP) {
            int toolbarHeight = mToolbar.getHeight();
            int scrollY = mList.getCurrentScrollY();
            if (toolbarHeight <= scrollY) {
                hideToolbar();
            } else {
                showToolbar();
            }
        } else {
            // Even if onScrollChanged occurs without scrollY changing, toolbar should be adjusted
            if (!toolbarIsShown() && !toolbarIsHidden()) {
                // Toolbar is moving but doesn't know which to move:
                // you can change this to hideToolbar()
                showToolbar();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setPivotXToTitle() {
        Configuration config = getResources().getConfiguration();
        if (Build.VERSION_CODES.JELLY_BEAN_MR1 <= Build.VERSION.SDK_INT
                && config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            ViewHelper.setPivotX(mTitleView, getActivity().getWindow().getDecorView().findViewById(android.R.id.content).getWidth());
        } else {
            ViewHelper.setPivotX(mTitleView, 0);
        }
    }

    private boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(mToolbar) == 0;
    }

    private boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(mToolbar) == -mToolbar.getHeight();
    }

    private void showToolbar() {
        float headerTranslationY = ViewHelper.getTranslationY(mToolbar);
        if (headerTranslationY != 0) {
            ViewPropertyAnimator.animate(mToolbar).cancel();
            ViewPropertyAnimator.animate(mToolbar).translationY(0).setDuration(200).start();
        }
    }

    private void hideToolbar() {
        float headerTranslationY = ViewHelper.getTranslationY(mToolbar);
        int toolbarHeight = mToolbar.getHeight();
        if (headerTranslationY != -toolbarHeight) {
            ViewPropertyAnimator.animate(mToolbar).cancel();
            ViewPropertyAnimator.animate(mToolbar).translationY(-toolbarHeight).setDuration(200).start();
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

            if (msg.what == MESSAGE_SCROLL) {
                if (mFragment != null && mFragment.get() != null &&
                        mFragment.get().mList != null) {
                    mFragment.get().mList.smoothScrollBy(2, 0);
                    sendEmptyMessageDelayed(MESSAGE_SCROLL, (long) (App.getInstance().getScrollValue() /
                            App.getInstance().getFontSize() * 10));
                    mFragment.get().mIsScrolling = true;
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mList.destroyDrawingCache();
            mAdapter.notifyDataSetChanged();
            mList.invalidateViews();
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
            holder.mTitleTextView.setTypeface(App.getInstance().getAlef());

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
            holder.mTextView.setTypeface(App.getInstance().getDefaultTypeface());

            return convertView;
        }

        class ViewHolder {
            TehilimTextView mTitleTextView;
            TehilimTextView mTextView;
            ImageView mExpandIcon;
        }
    }
}
