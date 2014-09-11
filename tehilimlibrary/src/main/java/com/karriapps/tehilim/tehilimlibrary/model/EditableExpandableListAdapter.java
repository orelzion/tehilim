package com.karriapps.tehilim.tehilimlibrary.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.karriapps.tehilim.tehilimlibrary.R;

/**
 * Created by Orel on 28/08/2014.
 */
public class EditableExpandableListAdapter extends BaseExpandableListAdapter {

    private EditableListGroupItem[] mGroups;
    private LayoutInflater mInflater;

    public EditableExpandableListAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setGroups(EditableListGroupItem[] groups) {
        mGroups = groups;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return mGroups == null ? 0 : mGroups.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mGroups[groupPosition].getChildren().length;
    }

    @Override
    public EditableListGroupItem getGroup(int groupPosition) {
        return mGroups[groupPosition];
    }

    @Override
    public IEditableChild getChild(int groupPosition, int childPosition) {
        return mGroups[groupPosition].getChildren()[childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.editable_last_view_group, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.titleTextView.setText(mGroups[groupPosition].getTitle());
        holder.editImage.setVisibility(mGroups[groupPosition].isEditable() ? View.VISIBLE : View.GONE);
//        holder.expandImage.setVisibility(mGroups[groupPosition].getChildren().length > 0 ? View.VISIBLE : View.GONE);

//        holder.expandImage.setVisibility(View.GONE);
        return view;
    }

    class ViewHolder {
        TextView titleTextView;
        ImageView editImage;
        ImageView expandImage;

        public ViewHolder(View v) {
            titleTextView = (TextView) v.findViewById(R.id.groupTitle);
            editImage = (ImageView) v.findViewById(R.id.groupEditImage);
//            expandImage = (ImageView)v.findViewById(R.id.groupExpand);
        }
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {
        ChildViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.editable_child_view, viewGroup, false);
            holder = new ChildViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ChildViewHolder) view.getTag();
        }

        holder.titleTextView.setText(getChild(groupPosition, childPosition).getTitle());
        holder.subtitleTextView.setText(getChild(groupPosition, childPosition).getSubtitle());
        holder.sideTextView.setText(getChild(groupPosition, childPosition).getSideText());

        return view;
    }

    class ChildViewHolder {
        TextView titleTextView;
        TextView subtitleTextView;
        TextView sideTextView;

        public ChildViewHolder(View v) {
            titleTextView = (TextView) v.findViewById(R.id.child_title);
            subtitleTextView = (TextView) v.findViewById(R.id.child_subtitle);
            sideTextView = (TextView) v.findViewById(R.id.child_side_text);
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
