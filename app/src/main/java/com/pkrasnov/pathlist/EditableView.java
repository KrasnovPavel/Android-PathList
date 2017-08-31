package com.pkrasnov.pathlist;

import java.util.*;
import android.widget.TextView;
import android.content.*;
import android.util.*;
import android.graphics.*;
import android.view.*;
import android.content.res.*;
import android.webkit.*;


public class EditableView extends TextView
{
    protected boolean bIsChoosed;
    protected boolean bSpecialSelect;
    protected boolean bNeedSpecialSelect;
    protected String name;
    protected float value;
    protected static List<EditableView> allViews;
    protected static TextView nameView;

    public static void setNameView(TextView nameView)
    {
        EditableView.nameView = nameView;
    }

    public static void setAllViews(List<EditableView> allViews)
    {
        EditableView.allViews = allViews;
    }
    
    public static List<EditableView> getAllViews()
    {
        return allViews;
    }
    
    public static String format(float d)
    {
        if(d == (int) d)
            return String.format("%d",(long)d);
        else
            return String.format("%s",d);
    }
    
    public static void clearStatic()
    {
        allViews = null;
        nameView = null;
    }
    
    public void clear()
    {
        value = 0;
        setText("");
    }
    
    public void setSpecialSelect(boolean specialSelect)
    {
        bSpecialSelect = specialSelect;
        updateBackground();
    }

    public boolean isSpecialSelected()
    {
        return bSpecialSelect;
    }
    
    private void initAttrs(Context context, AttributeSet attrs)
    {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, 
                       R.styleable.EditableView,
                       0, 0);
        try{
            name = a.getString(R.styleable.EditableView_viewName);
            bNeedSpecialSelect = a.getBoolean(R.styleable.EditableView_needSpecialSelect, false);
        }
        finally{
            a.recycle();
        }
        allViews.add(this);
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
    
    public EditableView(Context context, String name, boolean bNeedSpecialSelect) 
    {
        this(context, null, R.attr.EditableViewTheme);
        this.name = name;
        this.bNeedSpecialSelect = bNeedSpecialSelect;
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
    
    public boolean needSpecialSelect()
    {
        return bNeedSpecialSelect;
    }

    public String getName()
    {
        return name;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        setChoosed(true);
        return super.onTouchEvent(event);
    }
    
    public void setChoosed(boolean state)
    {
        if (bIsChoosed != state)
        {
            bIsChoosed = state;
            updateBackground();
        }
        if (bIsChoosed == true)
        {
            Iterator<EditableView> i = allViews.iterator();
            while (i.hasNext())
            {
                EditableView view = i.next();
                if (view != this) view.setChoosed(false);
            }
            nameView.setText(this.name);
            ((MainActivity)getContext()).checkButtons();
        }
    }
    
    public boolean isChoosed()
    {
        return bIsChoosed;
    }
    
    public float getValue()
    {
        return value;
    }
    
    public void addChar(String ch)
    {
        if (ch.equals("."))
        {   
            if (value == 0 && !hasDot()) setText("0.");
            else if (!hasDot()) setText(getText() + ch);
        }
        else
        {
            setText(getText() + ch);
        }
        
        updateValue();
    }
    
    public boolean hasDot()
    {
        return getText().toString().indexOf(".") != -1;
    }
    
    public boolean needDot()
    {
        return !hasDot();
    }
    
    public void removeChar()
    {
        int length = getText().length();
        if (length > 0)
        {
            setText(getText().subSequence(0, length - 1));
            updateValue();
        }
    }
    
    protected void updateValue()
    {
        try 
        {
            value = Float.valueOf(getText().toString());
        }
        catch(Exception e)
        {
            value = 0;
        }
    }
    
    protected void updateBackground()
    {
        if (bIsChoosed)
        {
            setBackgroundResource(R.drawable.editable_view_choosed);
        }
        else if (!bSpecialSelect)
        {
            setBackgroundResource(R.drawable.editable_view_unchoosed);
        }
        else 
        {
            setBackgroundResource(R.drawable.edirable_view_special_select);
        }
    }
    
    public void setValue(float value)
    {
        if (value <= 0f)
        {
            this.value = 0f;
            setText("");
        }
        else
        {
            this.value = value;
            setText(format(this.value));
        }
    }
}

