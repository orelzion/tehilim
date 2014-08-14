package com.karriapps.tehilim.tehilimlibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.karriapps.tehilim.tehilimlibrary.utils.App;

/**
 * Created by orelsara on 05/08/14.
 */
public class TehilimEditText extends EditText {

    public TehilimEditText(Context context) {
        super(context);
        init();
    }

    public TehilimEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TehilimEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setTypeface(App.getInstance().getAlef());
        setBackgroundDrawable(null);
    }
}
