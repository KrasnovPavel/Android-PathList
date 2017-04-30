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
        public static void onViewChoose(EditableView newChoosedView)
        {
            Iterator<EditableView> i = views.iterator();
            while (i.hasNext())
            {
                EditableView view = i.next();
                if (view.isChoosed())
                {
                    if (view == newChoosedView)
                    {
                        nameView.setText(newChoosedView.getName());
                        choosedView = newChoosedView;
                        checkComma();
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
        
        public static EditableView getChoosedView()
        {
            return choosedView;
        }
        
        public static void checkComma()
        {
            
            if (choosedView.getText().toString().indexOf(",") != -1 || choosedView.getNumberAfter() == 0)
            {
                commaButton.setEnabled(false);
            } 
            else
            {
                commaButton.setEnabled(true);
            }
        }
        
        public static void addNumber(String str)
        {
            int commaPos = choosedView.getText().toString().indexOf(",");
            int numBefore, numAfter;
            if (commaPos == -1)
            {
                numBefore = choosedView.getNumberBefore() - choosedView.getText().length();
                numAfter  = choosedView.getNumberAfter();
            }
            else
            {
                numBefore = choosedView.getNumberBefore() - commaPos + 1;
                numAfter  = choosedView.getNumberAfter() - (choosedView.getText().length() - commaPos) + 1;
            }
            
            if (str.equals(","))
            {
                if (choosedView.getNumberAfter() > 0)
                {
                    choosedView.setText(choosedView.getText() + str);
                    checkComma();
                }
            }
            else
            {
                if (commaPos == -1)
                {
                    if (numBefore > 0)
                    {
                        choosedView.setText(choosedView.getText().toString() + str);
                    }
                }
                else
                {
                    if (numAfter > 0)
                    {
                        choosedView.setText(choosedView.getText().toString() + str);
                    }
                }
            }
        }
           
        public static void removeNumber()
        {
            if (choosedView.getText().length() > 0)
            {
                choosedView.setText(choosedView.getText().subSequence(0, choosedView.getText().length()-1));
                checkComma();
            }
        }
        
        public static void nextView()
        {
            if (choosedViewNumber() < views.size()-1)
            {
                views.get(choosedViewNumber()+1).setChoose(true);
            }
        }
        
        public static void previousView()
        {
            if (choosedViewNumber() > 0)
            {
                views.get(choosedViewNumber()-1).setChoose(true);
            }
        }
        
        private static int choosedViewNumber()
        {
            return views.indexOf(choosedView);
        }

        private static List<EditableView> views = new ArrayList<EditableView>();
        public static TextView nameView;
        public static Button commaButton;
        private static EditableView choosedView;
        
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        EditableView editableView = new EditableView(this, "test", 6, 0);
        ((LinearLayout)findViewById(R.id.mainLayout)).addView(editableView, 0);
 
        EditableView editableView1 = new EditableView(this, "test1", 2, 2);
        ((LinearLayout)findViewById(R.id.mainLayout)).addView(editableView1, 1);
        
        EditableViewController.nameView = (TextView)findViewById(R.id.viewName);
        EditableViewController.commaButton = (Button)findViewById(R.id.buttonComma);
        editableView.setChoose(true);
    }
    
    public void onNumberButtonClick(View view)
    {
        EditableViewController.addNumber(((Button)view).getText().toString());
    }
    
    public void onBackspaceButtonClick(View view)
    {
        EditableViewController.removeNumber();
    }
    
    public void onNextButtonClick(View view)
    {
        EditableViewController.nextView();
    }
    
    public void onPreviousButtonClick(View view)
    {
        EditableViewController.previousView();
    }
}
