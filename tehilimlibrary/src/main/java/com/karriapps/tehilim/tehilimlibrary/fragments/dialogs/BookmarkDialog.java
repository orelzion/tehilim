package com.karriapps.tehilim.tehilimlibrary.fragments.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.karriapps.tehilim.tehilimlibrary.R;
import com.karriapps.tehilim.tehilimlibrary.fragments.NavigationDrawerFragment;
import com.karriapps.tehilim.tehilimlibrary.utils.App;

/**
 * Created by orelsara on 05/08/14.
 */
public class BookmarkDialog extends DialogFragment {

    private ListView mList;
    private BookmarkAdapter mAdapter;
    private NavigationDrawerFragment.NavigationDrawerCallbacks mCallbacks;

    public static BookmarkDialog newInstance(NavigationDrawerFragment.NavigationDrawerCallbacks callbacks) {
        BookmarkDialog d = new BookmarkDialog();
        d.setCallbacks(callbacks);
        return d;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.choose_bookmark, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList = (ListView) view.findViewById(R.id.bookmark_listview);
        mAdapter = new BookmarkAdapter();
        mList.setAdapter(mAdapter);
    }

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Dialog dialog = super.onCreateDialog(savedInstanceState);
//        dialog.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
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
//        return dialog;
//    }

    public void setCallbacks(NavigationDrawerFragment.NavigationDrawerCallbacks callbacks) {
        mCallbacks = callbacks;
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
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {

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

            holder.name.setEnabled(mIsInEditMode);
            holder.text.setEnabled(mIsInEditMode);

            return view;
        }

        private class ViewHolder {
            EditText name, text;

            public ViewHolder(View view) {
                name = (EditText) view.findViewById(R.id.bookmark_row_name);
                text = (EditText) view.findViewById(R.id.bookmark_row_text);
            }
        }
    }
}
