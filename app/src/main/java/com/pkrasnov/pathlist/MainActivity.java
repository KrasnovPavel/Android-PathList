package com.pkrasnov.pathlist;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;


public class MainActivity extends Activity 
{
    
    static class EditableViewController
    {
        public static void onViewChoose(EditableView choosedView)
        {
            Iterator<EditableView> i = views.iterator();
            while (i.hasNext())
            {
                EditableView view = i.next();
                if (view.isChoosed())
                {
                    if (view == choosedView)
                    {
                        nameView.setText(choosedView.getName());
                    }
                    else
                    {
                        view.setChoose(false);
                    }
                }
            }
        }
        public static void addView(EditableView view)
        {
            views.add(view);
        }

        private static List<EditableView> views = new ArrayList<EditableView>();
        public static TextView nameView;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        EditableView editableView = new EditableView(this, "test");
        editableView.setText("test");
        ((LinearLayout)findViewById(R.id.mainLayout)).addView(editableView, 0);
        EditableViewController.addView(editableView);
        
        EditableView editableView1 = new EditableView(this, "text1");
        editableView1.setText("test1");
        ((LinearLayout)findViewById(R.id.mainLayout)).addView(editableView1, 1);
        EditableViewController.addView(editableView1);
        EditableViewController.nameView = (TextView)findViewById(R.id.viewName);
        EditableViewController.addView((EditableView)findViewById(R.id.test2));
    }
    
}
