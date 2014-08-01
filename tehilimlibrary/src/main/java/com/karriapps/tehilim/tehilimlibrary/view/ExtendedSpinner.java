package com.karriapps.tehilim.tehilimlibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.lang.ref.WeakReference;

/**
 * An extended spinner, to support the option knowing whether
 * the selection of an item was made by user
 *
 * @author Orel Zion
 * @since 16.02.2014
 */
public class ExtendedSpinner extends Spinner {

    private boolean isUserTouched;
    private boolean mSendEvent;
    private WeakReference<OnItemSelectedListener> mListener;

    /**
     * An interface to replace the default interface for adapter spinner
     * \nThis one has a boolean indicating wheather the selection was made by the user
     *
     * @author Orel Zion
     * @serial 16.02.2014
     */
    public interface OnItemSelectedListener {
        public void OnItemSelected(AdapterView<?> adapter, View view, int position, long itemID, boolean byUser);

        public void onNothingSelected(AdapterView<?> adapter);
    }

    public ExtendedSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnItemSelectedListener(mOnItemSlectedListener);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelected) {
        mListener = new WeakReference<OnItemSelectedListener>(onItemSelected);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        isUserTouched = true;
        return super.onTouchEvent(event);
    }

    AdapterView.OnItemSelectedListener mOnItemSlectedListener = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            if (mListener != null && mListener.get() != null) {
                mListener.get().OnItemSelected(arg0, arg1, arg2, arg3, isUserTouched || mSendEvent);
            }
            isUserTouched = false;
            mSendEvent = false;
        }

        ;

        public void onNothingSelected(AdapterView<?> arg0) {
            if (mListener != null && mListener.get() != null) {
                mListener.get().onNothingSelected(arg0);
            }
        }

        ;
    };

    public void setSelection(int position, boolean sendEvent) {
        mSendEvent = sendEvent;
        super.setSelection(position);
    }

    public void setSelection(int position, boolean animate, boolean sendEvent) {
        mSendEvent = sendEvent;
        super.setSelection(position, animate);
    }
}
