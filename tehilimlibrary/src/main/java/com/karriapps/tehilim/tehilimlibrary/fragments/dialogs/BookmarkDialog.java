package com.karriapps.tehilim.tehilimlibrary.fragments.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.karriapps.tehilim.tehilimlibrary.R;
import com.karriapps.tehilim.tehilimlibrary.model.FavoriteListItem;
import com.karriapps.tehilim.tehilimlibrary.utils.App;

import java.util.ArrayList;

/**
 * Created by orelsara on 05/08/14.
 */
public class BookmarkDialog extends DialogFragment {

    private ListView mList;
    private Button mCancel;
    private Button mSave;
    private Button mAddButton;
    private BookmarkAdapter mAdapter;
    private ArrayList<FavoriteListItem> mFavorites = new ArrayList<FavoriteListItem>();
    private DialogInterface.OnDismissListener mOnDismissListener;

    public static BookmarkDialog newInstance(DialogInterface.OnDismissListener callbacks) {
        BookmarkDialog d = new BookmarkDialog();
        d.mOnDismissListener = callbacks;
        return d;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(mOnDismissListener != null) {
            mOnDismissListener.onDismiss(dialog);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.choose_bookmark, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList = (ListView) view.findViewById(R.id.bookmark_listview);
        mAddButton = (Button) view.findViewById(R.id.bookmark_dialog_edit);
        mCancel = (Button) view.findViewById(R.id.bookmark_cancel);
        mSave = (Button) view.findViewById(R.id.bookmark_ok);
        setOnClickListener(mAddButton, mCancel, mSave);
        mFavorites.clear();
        mFavorites.addAll(App.getInstance().getFavorites());
        mAdapter = new BookmarkAdapter();
        mList.setAdapter(mAdapter);
    }

    private void setOnClickListener(Button... buttons) {
        for(Button b : buttons) {
            b.setOnClickListener(mOnClickListener);
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.equals(mAddButton)) {
                buildDataFromViews();
                mFavorites.add(new FavoriteListItem());
                mAdapter.notifyDataSetChanged();
            } else if(view.equals(mCancel)) {
                if(getDialog() != null && getDialog().isShowing())
                    getDialog().dismiss();
            } else if(view.equals(mSave)) {
                buildDataFromViews();
                App.getInstance().saveFavorites(mFavorites);
                dismiss();
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    private void buildDataFromViews() {
        if(mList == null) {
            return;
        }
        int count = mAdapter.getCount();
        mFavorites = new ArrayList<FavoriteListItem>();
        for(int i = 0; i < count; i++) {
            View view = mList.getChildAt(i);
            FavoriteListItem item = new FavoriteListItem();
            item.setTitle(((EditText) view.findViewById(R.id.bookmark_row_name)).getText().toString());
            try {
                item.setValuesFromString(((EditText) view.findViewById(R.id.bookmark_row_text)).getText().toString());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            mFavorites.add(item);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().requestFeature(Window.FEATURE_CUSTOM_TITLE);
//        dialog.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.bookmarks_title_layout);
//        Button edit = (Button) dialog.findViewById(R.id.bookmark_dialog_edit);
//        if(edit != null) {
//            edit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(!isAdded() || mAdapter == null) {
//                        return;
//                    }
//                    TypedArray ta = getActivity().obtainStyledAttributes(new int[] {R.attr.action_edit_icon, R.attr.action_save_icon});
//                    if(mAdapter.isInEditMode()) {
//                        view.setBackgroundResource(ta.getIndex(1));
//                    } else {
//                        view.setBackgroundResource(ta.getIndex(0));
//                    }
//                    ta.recycle();
//                    mAdapter.setInEditMode(!mAdapter.isInEditMode());
//                }
//            });
//        }
        return dialog;
    }

    private class BookmarkAdapter extends BaseAdapter {

        private boolean mIsInEditMode;

        public boolean isInEditMode() {
            return mIsInEditMode;
        }

        public void setInEditMode(boolean isInEditMode) {
            mIsInEditMode = isInEditMode;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mFavorites == null ? 0 : mFavorites.size();
        }

        @Override
        public FavoriteListItem getItem(int i) {
            return mFavorites.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {

            View view = convertView;
            ViewHolder holder;

            if (view == null) {
                view = ((LayoutInflater) App.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.bookmark_row, viewGroup, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.name.setText(getItem(position).getTitle());
            holder.text.setText(getItem(position).getSubtitle());
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isAdded() && mFavorites != null) {
                        mFavorites.remove(position);
                        notifyDataSetChanged();
                    }
                }
            });

            return view;
        }

        private class ViewHolder {
            EditText name, text;
            ImageButton delete;

            public ViewHolder(View view) {
                name = (EditText) view.findViewById(R.id.bookmark_row_name);
                text = (EditText) view.findViewById(R.id.bookmark_row_text);
                delete = (ImageButton) view.findViewById(R.id.bookmark_row_delete);
            }
        }
    }
}
