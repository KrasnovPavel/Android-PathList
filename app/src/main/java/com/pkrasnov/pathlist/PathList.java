package com.pkrasnov.pathlist;
import java.util.*;
import java.lang.Math;

public class PathList
{
    
    private int beginSpeedometer, endSpeedometer;
    private int fullPath, cityPath, intercityPath;
    private int beginFuel, endFuel, addedFuel;
    private float beginOil, endOil, addedOil, consumedOil, consumedFuel;
    private List<Integer> speedometers = new ArrayList<Integer>();
    private List<Integer> kilos;
    private List<Boolean> intercity;
    private static final float oilRate = 2.4f;
    private float fuelRate, fuelCity, fuelIntercity;;

    public void setData(PathListReader reader)
    {
        beginSpeedometer = reader.getBeginSpeedometer();
        kilos = reader.getKilos();
        intercity = reader.getIntercity();
        beginFuel = reader.getBeginFuel();
        addedFuel = reader.getAddedFuel();
        beginOil = reader.getBeginOil();
        addedOil = reader.getAddedOil();
        fuelRate = reader.getFuelRate();
    }
    
    public void calculate()
    {
        speedometers.clear();
        if (kilos.size() > 0)
        {
            speedometers.add(beginSpeedometer + kilos.get(0));
            if (intercity.get(0))
            {
                intercityPath = kilos.get(0);
                cityPath = 0;
            }
            else
            {
                intercityPath = 0;
                cityPath = kilos.get(0);
            }
            for (int i = 1; i < kilos.size(); i++)
            {
                speedometers.add(speedometers.get(i-1) + kilos.get(i));
                if (intercity.get(i)) intercityPath += kilos.get(i);
                else cityPath += kilos.get(i);
            }

            fuelCity = Math.round(cityPath * fuelRate) / 100f;
            fuelIntercity = Math.round(intercityPath * fuelRate * 0.85f) / 100f;
            consumedFuel = Math.round((fuelCity + fuelIntercity) * 100) / 100f;
            consumedOil  = Math.round((fuelCity + fuelIntercity) * oilRate) / 100f;
            endFuel = beginFuel + addedFuel - Math.round(consumedFuel);
            endOil = Math.round((beginOil + addedOil - consumedOil) * 100) / 100f;
            fullPath = cityPath + intercityPath;
        }
        else
        {
            intercityPath = cityPath = 0;
            fuelCity = fuelIntercity = 0;
            consumedFuel = consumedOil = 0f;
            endFuel = beginFuel + addedFuel;
            endOil = beginOil + addedOil;
        }
    }

    public List<Boolean> getIntercity()
    {
        return intercity;
    }

    public List<Integer> getKilos()
    {
        return kilos;
    }
    
    public List<Integer> getSpeedometers()
    {
        return speedometers;
    }
    
    public int getFullPath()
    {
        return fullPath;
    }

    public float getBeginOil()
    {
        return beginOil;
    }

    public float getEndOil()
    {
        return endOil;
    }

    public float getAddedOil()
    {
        return addedOil;
    }
    
    public float getConsumedOil()
    {
        return consumedOil;
    }

    public int getBeginFuel()
    {
        return beginFuel;
    }

    public int getEndFuel()
    {
        return endFuel;
    }

    public int getAddedFuel()
    {
        return addedFuel;
    }
    
    public float getConsumedFuel()
    {
        return consumedFuel;
    }
    
    public int getBeginSpeedometer()
    {
        return beginSpeedometer;
    }
    
    public int getEndSpeedometer()
    {
        return endSpeedometer;
    }
    
    public float getFuelRate()
    {
        return fuelRate;
    }
    
    public float getFuelCity()
    {
        return fuelCity;
    }
    
    public float getFuelIntercity()
    {
        return fuelIntercity;
    }
    
    public float getCityPath()
    {
        return cityPath;
    }

    public float getIntercityPath()
    {
        return intercityPath;
    }
    
    public boolean hasIntercity()
    {
        Iterator<Boolean> i = intercity.iterator();
        while (i.hasNext())
        {
            if (i.next()) return true;
        }
        return false;
    }
}

