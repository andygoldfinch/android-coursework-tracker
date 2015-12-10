package uk.ac.leeds.atcg_gdl.courseworktracker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.DatePicker;

/**
 * A DatePicker extension which works correctly inside a scroll view.
 */
public class ScrollableDatePicker extends DatePicker {

    /**
     * Creates a ScrollableDatePicker instance.
     *
     * @param context
     * @param attrs
     */
    public ScrollableDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Prevents the scroll view from moving when swiping on the picker.
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            ViewParent p = getParent();
            if (p != null)
                p.requestDisallowInterceptTouchEvent(true);
        }

        return false;
    }
}
