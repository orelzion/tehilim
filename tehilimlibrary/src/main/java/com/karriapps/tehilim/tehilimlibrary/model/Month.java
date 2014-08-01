package com.karriapps.tehilim.tehilimlibrary.model;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.karriapps.tehilim.tehilimlibrary.R;
import com.karriapps.tehilim.tehilimlibrary.model.callbacks.MonthSelected;
import com.karriapps.tehilim.tehilimlibrary.utils.Alef;
import com.karriapps.tehilim.tehilimlibrary.utils.App;

import java.lang.ref.WeakReference;

public class Month extends DialogFragment {

    GridView monthView;
    String[] days;
    WeakReference<MonthSelected> listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        monthView = (GridView) inflater.inflate(R.layout.month, container, false);

        int daysInMonth = App.getInstance().getJewishCalendar().getDaysInJewishMonth();

        days = new String[daysInMonth];
        for (int i = 0; i < daysInMonth; i++) {
            days[i] = App.getInstance().isHebrew() ? new Alef(i + 1).toString() : String.valueOf(i + 1);
        }

        monthView.setAdapter(new MonthAdapter());
        monthView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                if (listener != null)
                    listener.get().OnMonthSelected(position + 1);
            }
        });

        getDialog().setTitle(R.string.choose_day);

        return monthView;
    }

    public void setListener(MonthSelected listener) {
        this.listener = new WeakReference<MonthSelected>(listener);
    }

    private class MonthAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return days.length;
        }

        @Override
        public String getItem(int position) {
            return days[position];
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int posiion, View convertVIew, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertVIew = inflater.inflate(R.layout.month_day, parent, false);

            ((TextView) convertVIew).setText(getItem(posiion));

            return convertVIew;
        }

    }
}
