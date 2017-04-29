package com.pkrasnov.pathlist;

import android.widget.TextView;
import android.content.*;
import android.util.*;
import android.graphics.*;


public class EditableView extends TextView
{
    public EditableView(Context context) 
    {
        super(context);
    }
    public EditableView(Context context, AttributeSet attrs) 
    {
        super(context, attrs);
    }
    public EditableView(Context context, AttributeSet attrs, int defStyleAttr) 
    {
        super(context, attrs, defStyleAttr);
    }
    public EditableView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) 
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
    }
}
