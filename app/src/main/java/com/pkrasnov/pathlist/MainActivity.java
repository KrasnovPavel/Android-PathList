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
    private List<TextView> factJobViews = new ArrayList<TextView>();
    private List<TextView> possibleJobViews = new ArrayList<TextView>();
    private List<TextView> loadedPathViews = new ArrayList<TextView>();
    private TableLayout table;
    private TextView nameView, fuelCityView, fuelIntercityView, fullPathView;
    private TextView fuelSumView, oilSumView, endFuelView, endOilView;
    private TextView fullWeightView, fullPossibleJobView, fullFactJobView, fullLoadedPathView, percentageView;
    private Button dotButton, previousButton;
    private ToggleButton selectButton;
    private PathList pathList;
    private PathListScreenReader reader;
    private List<EditableView> views = new ArrayList<EditableView>();
    private static final int STATIC_FIELDS_COUNT = 8;
    private SharedPreferences sPref;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EditableView.setAllViews(views);
        setContentView(R.layout.main);
        
        nameView            = (TextView)findViewById(R.id.viewName);
        fuelCityView        = (TextView)findViewById(R.id.viewFuelCity);
        fuelIntercityView   = (TextView)findViewById(R.id.viewFuelIntercity);
        fullPathView        = (TextView)findViewById(R.id.viewFullPath);
        fuelSumView         = (TextView)findViewById(R.id.viewFuelSum);
        endFuelView         = (TextView)findViewById(R.id.viewEndFuel);
        oilSumView          = (TextView)findViewById(R.id.viewOilSum);
        endOilView          = (TextView)findViewById(R.id.viewEndOil);
        fullWeightView      = (TextView)findViewById(R.id.viewFullWeight);
        fullFactJobView     = (TextView)findViewById(R.id.viewFactJob);
        fullPossibleJobView = (TextView)findViewById(R.id.viewPossibleJob);
        fullLoadedPathView  = (TextView)findViewById(R.id.viewLoadedPath);
        percentageView      = (TextView)findViewById(R.id.viewPercentage);
        dotButton           = (Button)  findViewById(R.id.buttonDot);
        previousButton      = (Button)  findViewById(R.id.buttonPrevious);
        selectButton        = (ToggleButton)findViewById(R.id.buttonSelect);
        table               = (TableLayout)findViewById(R.id.table);
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
        TableRow row = new TableRow(this);
        
        String name1 = "Рейс #" + String.valueOf(numberOfRows()) 
                     + ": пройдено километров";
                     
        String name2 = "Рейс #" + String.valueOf(numberOfRows()) 
                     + ": перевезённый груз";
                     
        EditableView ed = new EditableView(this, name1, 3, 0);
        row.addView(ed);
        
        TextView view = new TextView(this, null, 0, R.style.EditableViewStyle);
        loadedPathViews.add(view);
        row.addView(view);
        
        ed = new EditableView(this, name2, 2, 1);
        row.addView(ed);
        
        view = new TextView(this, null, 0, R.style.EditableViewStyle);
        factJobViews.add(view);
        row.addView(view);
        
        view = new TextView(this, null, 0, R.style.EditableViewStyle);
        possibleJobViews.add(view);
        row.addView(view);
        
        view = new TextView(this, null, 0, R.style.EditableViewStyle);
        speedometerViews.add(view);
        row.addView(view);
        
        table.addView(row, table.getChildCount()-1);
        
        ScrollView sv = (ScrollView)findViewById(R.id.scroll);
        sv.scrollTo(0, table.getBottom());
    }
    
    public int numberOfRows()
    {
        return table.getChildCount() - 1;
    }
    
    public void refresh()
    {
        pathList.setData(reader.readData());
        pathList.calculate();
        
        Iterator<TextView> s = speedometerViews.iterator();
        Iterator<Integer> ps = pathList.getSpeedometers().iterator();
        Iterator<TextView> f = factJobViews.iterator();
        Iterator<Float>   pf = pathList.getFactJobs().iterator();
        Iterator<TextView> p = possibleJobViews.iterator();
        Iterator<Float>   pp = pathList.getPossibleJobs().iterator();
        Iterator<TextView> l = loadedPathViews.iterator();
        Iterator<Integer> pl = pathList.getLoadedPaths().iterator();
        while (s.hasNext())
        {
            s.next().setText(ps.next().toString());
            f.next().setText(pf.next().toString());
            p.next().setText(pp.next().toString());
            l.next().setText(pl.next().toString());
        }
        
        
        fullPathView.setText(pathList.getFullPath() + "");
        fullWeightView.setText(pathList.getFullWeight() + "");
        fullFactJobView.setText(pathList.getFullFactJob() + "");
        fullPossibleJobView.setText(pathList.getFullPossibleJob() + "");
        fullLoadedPathView.setText(pathList.getFullLoadedPath() + "");
        
        fuelCityView.setText("Бензин(город):" 
                             + pathList.getCityPath()
                             + "x"
                             + pathList.getFuelRate()
                             + "/100="
                             + pathList.getFuelCity()
                             + (!pathList.hasIntercity()?("~"+Math.round(pathList.getFuelCity()))
                                                       :""));
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
        percentageView.setText("Процент использования:" + pathList.getPercentage() * 100 + "%");
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
        ed.putFloat("MaxWeight", pathList.getMaxWeight());
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
        views.get(7).setValue(sPref.getFloat("MaxWeight", 0));
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
        
        Iterator<TextView> s = speedometerViews.iterator();
        while(s.hasNext())
        {
            TextView view = s.next();
            ((ViewGroup)view.getParent()).removeView(view);
        }
        speedometerViews.clear();
        
        Iterator<TextView> l = loadedPathViews.iterator();
        while(l.hasNext())
        {
            TextView view = l.next();
            ((ViewGroup)view.getParent()).removeView(view);
        }
        loadedPathViews.clear();
        
        Iterator<TextView> f = factJobViews.iterator();
        while(f.hasNext())
        {
            TextView view = f.next();
            ((ViewGroup)view.getParent()).removeView(view);
        }
        factJobViews.clear();
        
        Iterator<TextView> p = possibleJobViews.iterator();
        while(p.hasNext())
        {
            TextView view = p.next();
            ((ViewGroup)view.getParent()).removeView(view);
        }
        possibleJobViews.clear();
        
        table.removeViews(1, table.getChildCount()-2);
        
        fullPathView.setText("");
        fuelCityView.setText("");
        fuelIntercityView.setText("");
        fuelSumView.setText("");
        endFuelView.setText("");
        oilSumView.setText("");
        endOilView.setText("");
        fullLoadedPathView.setText("");
        fullWeightView.setText("");
        fullFactJobView.setText("");
        fullPossibleJobView.setText("");
        pathList = new PathList();
    }

    @Override
    protected void onDestroy()
    {
        views.clear();
        speedometerViews.clear();
        loadedPathViews.clear();
        factJobViews.clear();
        possibleJobViews.clear();
        EditableView.clearStatic();
        nameView = null;
        dotButton = previousButton = selectButton = null;
        fuelCityView = fuelIntercityView = fullPathView = null;
        fuelSumView = oilSumView = endOilView = endFuelView = null;
        fullLoadedPathView = fullWeightView = fullPossibleJobView = fullFactJobView = null;
        pathList = null;
        reader = null;
        table = null;
        super.onDestroy();
    }
}

