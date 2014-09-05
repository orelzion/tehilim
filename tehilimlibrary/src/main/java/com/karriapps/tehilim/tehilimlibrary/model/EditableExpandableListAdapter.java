package com.karriapps.tehilim.tehilimlibrary.model;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Orel on 28/08/2014.
 */
public class EditableExpandableListAdapter extends BaseExpandableListAdapter {

    List<EditableListGroupItem> mGroups = new ArrayList<EditableListGroupItem>();

    @Override
    public int getGroupCount() {
        return mGroups == null ? 0 : mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mGroups.get(groupPosition).getChildren().size();
    }

    @Override
    public EditableListGroupItem getGroup(int groupPosition) {
        return mGroups.get(groupPosition);
    }

    @Override
    public IEditableChild getChild(int groupPosition, int childPosition) {
        return mGroups.get(groupPosition).getChildren().get(childPosition);
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
        return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
