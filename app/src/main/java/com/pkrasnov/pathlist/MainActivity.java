package com.pkrasnov.pathlist;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import android.text.*;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class MainActivity extends Activity 
{
    private List<TextView> speedometerViews = new ArrayList<TextView>();
    private TextView nameView, fuelCityView, fuelIntercityView, fullPathView;
    private TextView fuelSumView, oilSumView, endFuelView, endOilView;
    private Button dotButton, previousButton;
    private ToggleButton selectButton;
    private PathList pathList;
    private PathListScreenReader reader;
    private List<EditableView> views = new ArrayList<EditableView>();
    private static final int STATIC_FIELDS_COUNT = 7;
    private SharedPreferences sPref;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EditableView.setAllViews(views);
        setContentView(R.layout.main);
        
        nameView          = (TextView)findViewById(R.id.viewName);
        fuelCityView      = (TextView)findViewById(R.id.viewFuelCity);
        fuelIntercityView = (TextView)findViewById(R.id.viewFuelIntercity);
        fullPathView      = (TextView)findViewById(R.id.viewFullPath);
        fuelSumView       = (TextView)findViewById(R.id.viewFuelSum);
        endFuelView       = (TextView)findViewById(R.id.viewEndFuel);
        oilSumView        = (TextView)findViewById(R.id.viewOilSum);
        endOilView        = (TextView)findViewById(R.id.viewEndOil);
        dotButton         = (Button)  findViewById(R.id.buttonDot);
        previousButton    = (Button)  findViewById(R.id.buttonPrevious);
        selectButton      = (ToggleButton)findViewById(R.id.buttonSelect);
        pathList = new PathList();
        reader = new PathListScreenReader(views);
        
        readData();
        
        EditableView.setNameView(nameView);
        views.get(0).setChoosed(true);
        checkButtons();
    }
    
    public void onNumberButtonClick(View view)
    {
        getChoosedView().addChar(((Button)view).getText().toString());
        checkDot();
        refresh();
    }
    
    public void onBackspaceButtonClick(View view)
    {
        getChoosedView().removeChar();
        checkDot();
        refresh();
    }
    
    public void onSelectButtonClick(View view)
    {
        getChoosedView().setSpecialSelect(selectButton.isChecked());
        refresh();
    }
    
    public void onNextButtonClick(View view)
    {
        int cvn = choosedViewNumber();
        if (cvn == views.size()-1) createRow();
        views.get(choosedViewNumber()+1).setChoosed(true);
        checkButtons();
    }
    
    public void onPreviousButtonClick(View view)
    {
        if (choosedViewNumber() > 0)
        { 
            views.get(choosedViewNumber()-1).setChoosed(true);
        }
        checkButtons();
    }
    
    public void onDoneButtonClick(View view)
    {
        saveData();
        clearScreen();
        readData();
    }
    
    protected EditableView getChoosedView()
    {
        Iterator<EditableView> i = EditableView.allViews.iterator();
        while (i.hasNext())
        {
            EditableView view = i.next();
            if (view.isChoosed()) return view;
        }
        return null;
    }
    
    protected int choosedViewNumber()
    {
        return views.indexOf(getChoosedView());
    }
    
    public void checkButtons()
    {
        int cvn = choosedViewNumber();
        checkDot();
        previousButton.setEnabled(cvn != 0);
        selectButton.setEnabled(cvn >= STATIC_FIELDS_COUNT);
        selectButton.setChecked(getChoosedView().isSpecialSelected());
    }
    
    protected void checkDot()
    {
        dotButton.setEnabled(getChoosedView().needDot());
    }

    protected void createRow()
    {
        String name = "Рейс #" + String.valueOf(numberOfRows()) 
                     + ": пройдено километров";
        EditableView ed = new EditableView(this, name, 3, 0);
        GridLayout gl = (GridLayout)findViewById(R.id.table);
        gl.addView(ed, gl.getChildCount() - 1);
        
        TextView view = new TextView(this);
        speedometerViews.add(view);
        gl.addView(view, gl.getChildCount() - 1);
        
        ScrollView sv = (ScrollView)findViewById(R.id.scroll);
        sv.scrollTo(0, gl.getBottom());
    }
    
    public int numberOfRows()
    {
        return views.size() - STATIC_FIELDS_COUNT + 1;
    }
    
    public void refresh()
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
                                + pathList.getConsumedFuel()
                                + "~"
                                + Math.round(pathList.getConsumedFuel()));
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
                           + "x"
                           + pathList.getOilRate()
                           + "/100=" 
                           + pathList.getConsumedOil());
        endFuelView.setText("Осталось топлива:" + pathList.getEndFuel());
        endOilView.setText("Осталось масла:" + pathList.getEndOil());
    }
    
    protected void saveData()
    {
        sPref = getPreferences(MODE_PRIVATE);
        Editor ed = sPref.edit();
        ed.putInt("Speedometer", pathList.getEndSpeedometer());
        ed.putInt("Fuel", pathList.getEndFuel());
        ed.putFloat("Oil", pathList.getEndOil());
        ed.putFloat("FuelRate", pathList.getFuelRate());
        ed.putFloat("OilRate", pathList.getOilRate());
        ed.commit();
    }
    
    protected void readData()
    {
        sPref = getPreferences(MODE_PRIVATE);
        views.get(0).setValue(sPref.getInt("Speedometer", 0));
        views.get(1).setValue(sPref.getInt("Fuel", 0));
        views.get(3).setValue(sPref.getFloat("Oil", 0));
        views.get(5).setValue(sPref.getFloat("FuelRate", 0));
        views.get(6).setValue(sPref.getFloat("OilRate", 0));
        refresh();
    }
    
    protected void clearScreen()
    {
        views.get(0).setChoosed(true);
        for(int i = views.size() - 1; i >= 0 ; i--)
        {
            if (i < STATIC_FIELDS_COUNT)
            {
                views.get(i).clear();
            }
            else
            {
                ((ViewGroup)views.get(i).getParent()).removeView(views.get(i));
                views.remove(i);
            }
        }
        
        Iterator<TextView> i = speedometerViews.iterator();
        while(i.hasNext())
        {
            TextView view = i.next();
            ((ViewGroup)view.getParent()).removeView(view);
        }
        speedometerViews.clear();
        fullPathView.setText("");
        fuelCityView.setText("");
        fuelIntercityView.setText("");
        fuelSumView.setText("");
        endFuelView.setText("");
        oilSumView.setText("");
        endOilView.setText("");
        pathList = new PathList();
    }

    @Override
    protected void onDestroy()
    {
        views.clear();
        speedometerViews.clear();
        EditableView.clearStatic();
        nameView = null;
        dotButton = previousButton = selectButton = null;
        fuelCityView = fuelIntercityView = fullPathView = null;
        fuelSumView = oilSumView = endOilView = endFuelView = null;
        pathList = null;
        reader = null;
        super.onDestroy();
    }
}

