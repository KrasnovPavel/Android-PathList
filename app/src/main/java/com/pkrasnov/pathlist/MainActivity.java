package com.pkrasnov.pathlist;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;

public class MainActivity extends Activity 
{
    private List<TextView> speedometerViews = new ArrayList<TextView>();
    private List<TextView> factJobViews = new ArrayList<TextView>();
    private List<TextView> possibleJobViews = new ArrayList<TextView>();
    private List<TextView> loadedPathViews = new ArrayList<TextView>();
    private TableLayout table;
    private TextView nameView, fuelCityView, fuelIntercityView, fullPathView;
    private TextView fuelSumView, oilSumView, endFuelView, endOilView;
    private TextView fullMotohoursView, fuelMotohoursView;
    private TextView fullWeightView, fullPossibleJobView, fullFactJobView, fullLoadedPathView, percentageView;
    private TextView view6, view12, view14, view15, view16;
    private Button dotButton, previousButton;
    private ToggleButton selectButton;
    private PathList pathList;
    private PathListScreenReader reader;
    private List<EditableView> views = new ArrayList<EditableView>();
    private static final int STATIC_FIELDS_COUNT = 9;
    private SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EditableView.setAllViews(views);
        setContentView(R.layout.main);
        DrawerHelper.initDrawer(this);
        setTitle(R.string.main_title);

        getActionBar().setDisplayUseLogoEnabled(true);

        findResourses();
        pathList = new PathList();
        reader = new PathListScreenReader(views);

        readData();

        EditableView.setNameView(nameView);
        views.get(0).setChoosed(true);
        checkButtons();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        DrawerHelper.getDrawerToggle().syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        DrawerHelper.getDrawerToggle().onConfigurationChanged(newConfig);
    }

    private void findResourses()
    {
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
        fullMotohoursView   = (TextView)findViewById(R.id.viewFullMotohours);
        fuelMotohoursView   = (TextView)findViewById(R.id.viewFuelMotohours);
        view6               = (TextView)findViewById(R.id.view6);
        view12              = (TextView)findViewById(R.id.view12);
        view14              = (TextView)findViewById(R.id.view14);
        view15              = (TextView)findViewById(R.id.view15);
        view16              = (TextView)findViewById(R.id.view16);
        dotButton           = (Button)  findViewById(R.id.buttonDot);
        previousButton      = (Button)  findViewById(R.id.buttonPrevious);
        selectButton        = (ToggleButton)findViewById(R.id.buttonSelect);
        table               = (TableLayout)findViewById(R.id.table);
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
        int delta = 1;
        int dcvn = cvn - (STATIC_FIELDS_COUNT - 1);
        if (dcvn % 3 == 2 && !pathList.hasMotohours()) delta = 2;
        if (dcvn % 3 == 1 && !pathList.hasWeight())
        {
            delta += (pathList.hasMotohours()) ?1: 2;
        }
        if (cvn < STATIC_FIELDS_COUNT) delta = 1;
        if ((cvn + delta) == views.size()) createRow();
        views.get(cvn + delta).setChoosed(true);
        checkButtons();
    }

    public void onPreviousButtonClick(View view)
    {
        int cvn = choosedViewNumber();
        int delta = 1;
        int dcvn = cvn - (STATIC_FIELDS_COUNT - 1);
        if (dcvn % 3 == 0 && !pathList.hasWeight()) delta = 2;
        if (dcvn % 3 == 1 && !pathList.hasMotohours())
        {
            delta += (pathList.hasWeight()) ?1: 2;
        }
        if (cvn <= STATIC_FIELDS_COUNT) delta = 1;
        if (cvn - delta >= 0)
        { 
            views.get(cvn - delta).setChoosed(true);
        }
        checkButtons();
    }

    public void onDoneButtonClick(View view)
    {
        donePressed();
    }

    protected void donePressed()
    {
        saveData();
        clearScreen();
        readData();
    }

    public static String format(float d)
    {
        if (d == (int) d)
            return String.format("%d", (long)d);
        else
            return String.format("%s", d);
    }

    protected EditableView getChoosedView()
    {
        Iterator<EditableView> i = EditableView.getAllViews().iterator();
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
        selectButton.setEnabled(getChoosedView().needSpecialSelect());
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

        String name3 = "Рейс #" + String.valueOf(numberOfRows()) 
        + ": отработано моточасов";

        EditableView ed = new EditableView(this, name1, true);
        row.addView(ed);

        TextView view = new TextView(this, null, 0, R.style.EditableViewStyle);
        loadedPathViews.add(view);
        row.addView(view);

        ed = new EditableView(this, name2, false);
        row.addView(ed);

        view = new TextView(this, null, 0, R.style.EditableViewStyle);
        factJobViews.add(view);
        row.addView(view);

        view = new TextView(this, null, 0, R.style.EditableViewStyle);
        possibleJobViews.add(view);
        row.addView(view);

        ed = new EditableView(this, name3, false);
        row.addView(ed);

        view = new TextView(this, null, 0, R.style.EditableViewStyle);
        speedometerViews.add(view);
        row.addView(view);

        table.addView(row, table.getChildCount() - 1);

        ScrollView sv = (ScrollView)findViewById(R.id.scroll);
        sv.scrollTo(0, table.getBottom());
        
        refreshVisibility();
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
            s.next().setText(format(ps.next()));
            f.next().setText(format(pf.next()));
            p.next().setText(format(pp.next()));
            l.next().setText(format(pl.next()));
        }

        fullPathView.setText(pathList.getFullPath() + "");
        fullWeightView.setText(format(pathList.getFullWeight()));
        fullFactJobView.setText(format(pathList.getFullFactJob()));
        fullPossibleJobView.setText(format(pathList.getFullPossibleJob()));
        fullLoadedPathView.setText(pathList.getFullLoadedPath() + "");
        fullMotohoursView.setText(pathList.getFullMotohours() + "");

        fuelCityView.setText("Топливо(город):" 
                             + pathList.getCityPath()
                             + "x"
                             + pathList.getFuelRate()
                             + "/100="
                             + pathList.getFuelCity()
                             + (!pathList.hasIntercity() ?("~" + Math.round(pathList.getFuelCity()))
                             : ""));
        fuelIntercityView.setText("Топливо(межгород):" 
                                  + pathList.getIntercityPath()
                                  + "x"
                                  + pathList.getFuelRate()
                                  + "/100-15%="
                                  + pathList.getFuelIntercity());
        fuelMotohoursView.setText("Топливо(моточасы):" 
                                  + pathList.getFullMotohours()
                                  + "x"
                                  + pathList.getMotohourRate()
                                  + "="
                                  + pathList.getFuelMotohours());
        fuelSumView.setText("Топливо(сумма):" 
                            + pathList.getFuelCity()
                            + (pathList.hasIntercity() ? "+"
                            + pathList.getFuelIntercity() : "")
                            + (pathList.hasMotohours() ? "+"
                            + pathList.getFuelMotohours() : "")
                            + "="
                            + pathList.getConsumedFuel()
                            + "~"
                            + Math.round(pathList.getConsumedFuel()));
        oilSumView.setText("Масло:" 
                           + pathList.getConsumedFuel()
                           + "x"
                           + pathList.getOilRate()
                           + "/100=" 
                           + pathList.getConsumedOil());
        endFuelView.setText("Осталось топлива:" + pathList.getEndFuel());
        endOilView.setText("Осталось масла:" + pathList.getEndOil());
        percentageView.setText("Процент использования:" + pathList.getPercentage() * 100 + "%");

        refreshVisibility();
    }

    protected void refreshVisibility()
    {
        fuelIntercityView.setVisibility(View.GONE);
        fuelMotohoursView.setVisibility(View.GONE);
        fuelSumView.setVisibility(View.GONE);
        if (pathList.hasMotohours())
        {
            for (int i = STATIC_FIELDS_COUNT; i < views.size(); i++)
            {
                if ((i - (STATIC_FIELDS_COUNT - 1)) % 3 == 0)
                {
                    views.get(i).setVisibility(View.VISIBLE);
                }
            }
            view16.setVisibility(View.VISIBLE);
            fullMotohoursView.setVisibility(View.VISIBLE);
            fuelMotohoursView.setVisibility(View.VISIBLE);
            fuelSumView.setVisibility(View.VISIBLE);
        }
        else
        {
            for (int i = STATIC_FIELDS_COUNT; i < views.size(); i++)
            {
                if ((i - (STATIC_FIELDS_COUNT - 1)) % 3 == 0)
                {
                    views.get(i).setVisibility(View.GONE);
                }
            }
            view16.setVisibility(View.GONE);
            fullMotohoursView.setVisibility(View.GONE);
        }
        if (pathList.hasWeight())
        {
            for (int i = STATIC_FIELDS_COUNT; i < views.size(); i++)
            {
                if ((i - (STATIC_FIELDS_COUNT - 1)) % 3 == 2)
                {
                    views.get(i).setVisibility(View.VISIBLE);
                }
            }
            for (int i = 0; i < loadedPathViews.size(); i++)
            {
                loadedPathViews.get(i).setVisibility(View.VISIBLE);
                factJobViews.get(i).setVisibility(View.VISIBLE);
                possibleJobViews.get(i).setVisibility(View.VISIBLE);
            }
            view6.setVisibility(View.VISIBLE);
            view12.setVisibility(View.VISIBLE);
            view14.setVisibility(View.VISIBLE);
            view15.setVisibility(View.VISIBLE);
            fullLoadedPathView.setVisibility(View.VISIBLE);
            fullWeightView.setVisibility(View.VISIBLE);
            fullPossibleJobView.setVisibility(View.VISIBLE);
            fullFactJobView.setVisibility(View.VISIBLE);
            percentageView.setVisibility(View.VISIBLE);
        }
        else
        {
            for (int i = STATIC_FIELDS_COUNT; i < views.size(); i++)
            {
                if ((i - (STATIC_FIELDS_COUNT - 1)) % 3 == 2)
                {
                    views.get(i).setVisibility(View.GONE);
                }
            }
            for (int i = 0; i < loadedPathViews.size(); i++)
            {
                loadedPathViews.get(i).setVisibility(View.GONE);
                factJobViews.get(i).setVisibility(View.GONE);
                possibleJobViews.get(i).setVisibility(View.GONE);
            }
            view6.setVisibility(View.GONE);
            view12.setVisibility(View.GONE);
            view14.setVisibility(View.GONE);
            view15.setVisibility(View.GONE);
            fullLoadedPathView.setVisibility(View.GONE);
            fullWeightView.setVisibility(View.GONE);
            fullPossibleJobView.setVisibility(View.GONE);
            fullFactJobView.setVisibility(View.GONE);
            percentageView.setVisibility(View.GONE);
        }
        if (pathList.hasIntercity())
        {
            fuelIntercityView.setVisibility(View.VISIBLE);
            fuelSumView.setVisibility(View.VISIBLE);
        }
        if (pathList.hasOil())
        {
            oilSumView.setVisibility(View.VISIBLE);
            endOilView.setVisibility(View.VISIBLE);
        }
        else
        {
            oilSumView.setVisibility(View.GONE);
            endOilView.setVisibility(View.GONE);
        }
    }

    protected void saveData()
    {
        sPref = getPreferences(MODE_PRIVATE);
        Editor ed = sPref.edit();
        ed.putInt("Speedometer", pathList.getEndSpeedometer());
        ed.putInt("Fuel", pathList.getEndFuel());
        ed.putFloat("MotohoursRate", pathList.getMotohourRate());
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
        views.get(7).setValue(sPref.getFloat("MotohoursRate", 0));
        views.get(8).setValue(sPref.getFloat("MaxWeight", 0));
        refresh();
    }

    protected void clearScreen()
    {
        views.get(0).setChoosed(true);
        for (int i = views.size() - 1; i >= 0 ; i--)
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
        while (s.hasNext())
        {
            TextView view = s.next();
            ((ViewGroup)view.getParent()).removeView(view);
        }
        speedometerViews.clear();

        Iterator<TextView> l = loadedPathViews.iterator();
        while (l.hasNext())
        {
            TextView view = l.next();
            ((ViewGroup)view.getParent()).removeView(view);
        }
        loadedPathViews.clear();

        Iterator<TextView> f = factJobViews.iterator();
        while (f.hasNext())
        {
            TextView view = f.next();
            ((ViewGroup)view.getParent()).removeView(view);
        }
        factJobViews.clear();

        Iterator<TextView> p = possibleJobViews.iterator();
        while (p.hasNext())
        {
            TextView view = p.next();
            ((ViewGroup)view.getParent()).removeView(view);
        }
        possibleJobViews.clear();

        table.removeViews(1, table.getChildCount() - 2);

        fullPathView.setText("");
        fuelCityView.setText("");
        fuelIntercityView.setText("");
        fuelMotohoursView.setText("");
        fuelSumView.setText("");
        endFuelView.setText("");
        oilSumView.setText("");
        endOilView.setText("");
        fullLoadedPathView.setText("");
        fullWeightView.setText("");
        fullFactJobView.setText("");
        fullPossibleJobView.setText("");
        fullMotohoursView.setText("");
        percentageView.setText("");
        pathList = new PathList();
    }

    protected void showAboutDialog()
    {
        DialogFragment newFragment = new AboutDialogFragment();
        newFragment.show(getFragmentManager(), "about");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle presses on the action bar items
        switch (item.getItemId())
        {
            case R.id.reset:
                clearScreen();
                return true;
            case R.id.done:
                donePressed();
                return true;
            case R.id.about:
                showAboutDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

