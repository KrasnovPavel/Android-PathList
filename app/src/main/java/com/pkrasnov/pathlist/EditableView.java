package com.pkrasnov.pathlist;

import android.widget.TextView;
import android.content.*;
import android.util.*;
import android.graphics.*;
import android.view.*;
import android.content.res.*;


public class EditableView extends TextView
{
    private void initAttrs(Context context, AttributeSet attrs)
    {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, 
                       R.styleable.EditableView,
                       0, 0);
        try{
            name = a.getString(R.styleable.EditableView_viewName);
            numberBefore = a.getInteger(R.styleable.EditableView_numberBefore, 3);
            numberAfter = a.getInteger(R.styleable.EditableView_numberAfter, 3);
        }
        finally{
            a.recycle();
        }
        
    }
    
    public EditableView(Context context, AttributeSet attrs, int defStyleAttr) 
    {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }
    
    public EditableView(Context context) 
    {
        this(context, null, R.attr.EditableViewTheme);
    }
    
    public EditableView(Context context, String name, int numberBefore, int numberAfter) 
    {
        this(context, null, R.attr.EditableViewTheme);
        this.name = name;
        this.numberBefore = numberBefore;
        this.numberAfter = numberAfter;
    }
    
    public EditableView(Context context, AttributeSet attrs) 
    {
        this(context, attrs, R.attr.EditableViewTheme);
    }
    
    public EditableView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) 
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(context, attrs);
    }

    public String getName()
    {
        return name;
    }
    
    public int getNumberBefore()
    {
        return numberBefore;
    }

    public int getNumberAfter()
    {
        return numberAfter;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        setChoose(true);
        return super.onTouchEvent(event);
    }
    
    public void setChoose(boolean state)
    {
        if (bIsChoosed != state)
        {
            bIsChoosed = state;
            if (bIsChoosed)
            {
                setBackgroundResource(R.drawable.editable_view_choosed);
                MainActivity.EditableViewController.onViewChoose(this);
            }
            else
            {
                setBackgroundResource(R.drawable.editable_view_unchoosed);
            }
        }
    }
    
    public boolean isChoosed()
    {
        return bIsChoosed;
    }
    
    protected boolean bIsChoosed;
    protected String name;
    protected int numberBefore, numberAfter;
}
