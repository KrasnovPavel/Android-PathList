package com.pkrasnov.pathlist;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import android.text.*;
import android.content.*;


public class MainActivity extends Activity 
{
    static class ViewsController
    {      
        private static List<EditableView> views = new ArrayList<EditableView>();
        private static List<TextView> speedometerViews = new ArrayList<TextView>();
        private static TextView nameView, fuelCityView, fuelIntercityView, fullPathView;
        private static TextView fuelSumView, oilSumView, endFuelView, endOilView;
        private static Button dotButton, previousButton;
        private static ToggleButton selectButton;
        private static EditableView choosedView;
        private static final int STATIC_FIELDS_COUNT = 5;
        private static PathList pathList = null;
        private static PathListScreenReader reader = new PathListScreenReader(views);

        public static void init(TextView newNameView, 
                                TextView newFuelCityView,
                                TextView newFuelIntercityView,
                                TextView newFuelSumView,
                                TextView newOilSumView,
                                TextView newEndFuelView,
                                TextView newEndOilView,
                                TextView newFullPathView,
                                Button newDotButton, 
                                Button newPreviousButton,
                                ToggleButton newSelectButton,
                                PathList newPathList)
        {
            dotButton = newDotButton;
            previousButton = newPreviousButton;
            nameView = newNameView;
            selectButton = newSelectButton;
            pathList = newPathList;
            fuelCityView = newFuelCityView;
            fuelIntercityView = newFuelIntercityView;
            fuelSumView = newFuelSumView;
            oilSumView = newOilSumView;
            endOilView = newEndOilView;
            endFuelView = newEndFuelView;
            fullPathView = newFullPathView;
            
            views.get(0).setChoosed(true);
            checkButtons(0);
        }
        
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
                        checkButtons(views.indexOf(view));
                    }
                    else
                    {
                        view.setChoosed(false);
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
        
        public static void checkDot()
        {
            dotButton.setEnabled(choosedView.needDot());
        }
        
        public static void addNumber(String str)
        {
            choosedView.addChar(str);
            checkDot();
        }
           
        public static void removeNumber()
        {
            choosedView.removeChar();
            checkDot();
        }
        
        public static void nextView()
        {
            int cvn = choosedViewNumber();
            checkButtons(cvn);
            if (cvn < views.size()-1)
            {
                views.get(choosedViewNumber()+1).setChoosed(true);
            }
        }
        
        public static void previousView()
        {
            int cvn = choosedViewNumber();
            checkButtons(cvn);
            if (cvn > 0)
            { 
                views.get(choosedViewNumber()-1).setChoosed(true);
            }
        }
        
        public static void selectClick()
        {
            choosedView.setSpecialSelect(selectButton.isChecked());
        }
        
        protected static void checkButtons(int cvn)
        {
            checkDot();
            previousButton.setEnabled(cvn != 0);
            selectButton.setEnabled(cvn > STATIC_FIELDS_COUNT);
            selectButton.setChecked(choosedView.isSpecialSelected());
        }
        
        protected static int choosedViewNumber()
        {
            return views.indexOf(choosedView);
        }
        
        protected static boolean isLastViewChoosed()
        {
            return choosedViewNumber() == views.size() - 1;
        }
        
        protected static int numberOfRows()
        {
            return views.size() - STATIC_FIELDS_COUNT;
        }
        
        public static void reset()
        {
            views.clear();
            nameView = choosedView = null;
            dotButton = previousButton = selectButton = null;
            fuelCityView = fuelIntercityView = fullPathView = null;
            fuelSumView = oilSumView = endOilView = endFuelView = null;
            pathList = null;
        }
        
        public static void addSpeedometerView(Context context, GridLayout layout)
        {
            TextView view = new TextView(context);
            speedometerViews.add(view);
            layout.addView(view, layout.getChildCount() - 1);
        }
        
        public static void refresh()
        {
            pathList.setData(reader.readData());
            pathList.calculate();
            Iterator<TextView> s = speedometerViews.iterator();
            Iterator<Integer> ps = pathList.getSpeedometers().iterator();
            while (s.hasNext() && ps.hasNext())
            {
                s.next().setText(ps.next().toString());
            }
            fullPathView.setText(pathList.getFullPath() + "");
            fuelCityView.setText("Бензин(город):" 
                                 + pathList.getCityPath()
                                 + "x"
                                 + pathList.getFuelRate()
                                 + "/100="
                                 + pathList.getFuelCity());
            if (pathList.hasIntercity())
            {
                fuelIntercityView.setText("Бензин(межгород):" 
                                     + pathList.getIntercityPath()
                                     + "x"
                                     + pathList.getFuelRate()
                                     + "/100-15%="
                                     + pathList.getFuelIntercity());
                fuelSumView.setText("Бензин(сумма):" 
                                     + pathList.getFuelCity()
                                     + "+"
                                     + pathList.getFuelIntercity()
                                     + "="
                                     + pathList.getConsumedFuel());
                fuelIntercityView.setVisibility(View.VISIBLE);
                fuelSumView.setVisibility(View.VISIBLE);
            }
            else
            {
                fuelIntercityView.setVisibility(View.GONE);
                fuelSumView.setVisibility(View.GONE);
            }
            oilSumView.setText("Масло:" 
                                + pathList.getConsumedFuel()
                                + "x2.4/100="
                                + pathList.getConsumedOil());
            endFuelView.setText("Осталось топлива:" + pathList.getEndFuel());
            endOilView.setText("Осталось масла:" + pathList.getEndOil());
        }
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ViewsController.init((TextView)findViewById(R.id.viewName),
                             (TextView)findViewById(R.id.viewFuelCity),
                             (TextView)findViewById(R.id.viewFuelIntercity),
                             (TextView)findViewById(R.id.viewFuelSum),
                             (TextView)findViewById(R.id.viewOilSum),
                             (TextView)findViewById(R.id.viewEndFuel),
                             (TextView)findViewById(R.id.viewEndOil),
                             (TextView)findViewById(R.id.viewFullPath),
                             (Button)findViewById(R.id.buttonDot),
                             (Button)findViewById(R.id.buttonPrevious),
                             (ToggleButton)findViewById(R.id.buttonSelect),
                             new PathList());
        
    }
    
    public void onNumberButtonClick(View view)
    {
        ViewsController.addNumber(((Button)view).getText().toString());
        ViewsController.refresh();
    }
    
    public void onBackspaceButtonClick(View view)
    {
        ViewsController.removeNumber();
        ViewsController.refresh();
    }
    
    public void onNextButtonClick(View view)
    {
        if (ViewsController.isLastViewChoosed()) createRow();
        ViewsController.nextView();
        ViewsController.refresh();
    }
    
    public void onPreviousButtonClick(View view)
    {
        ViewsController.previousView();
        ViewsController.refresh();
    }
    
    public void onSelectButtonClick(View view)
    {
        ViewsController.selectClick();
        ViewsController.refresh();
    }
    
    private void createRow()
    {
        String name = "Рейс #" + String.valueOf(ViewsController.numberOfRows()) 
                     + ": пройдено километров";
        EditableView ed = new EditableView(this, name, 3, 0);
        GridLayout gl = (GridLayout)findViewById(R.id.table);
        gl.addView(ed, gl.getChildCount() - 1);
        
        ViewsController.addSpeedometerView(this, gl);
        
        ScrollView sv = (ScrollView)findViewById(R.id.scroll);
        sv.scrollTo(0, gl.getBottom());
    }

    @Override
    protected void onDestroy()
    {
        ViewsController.reset();
        super.onDestroy();
    }
}

