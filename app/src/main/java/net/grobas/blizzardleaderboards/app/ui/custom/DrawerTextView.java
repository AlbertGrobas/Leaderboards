package net.grobas.blizzardleaderboards.app.ui.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.widget.TextView;

public class DrawerTextView extends TextView {

    public DrawerTextView(Context context) {
        super(context);
    }

    public DrawerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public DrawerTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);

        Drawable[] listDrawables = getCompoundDrawables();
        for(Drawable drawable : listDrawables) {
            if(drawable != null)
                DrawableCompat.setTint(DrawableCompat.wrap(drawable), color);
        }

    }
}
