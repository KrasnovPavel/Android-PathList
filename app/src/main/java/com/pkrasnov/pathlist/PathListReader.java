package com.pkrasnov.pathlist;

import java.util.*;

public abstract class PathListReader
{
    protected int beginSpeedometer;
    protected List<Integer> kilos = new ArrayList<Integer>();
    protected List<Boolean> intercity = new ArrayList<Boolean>();
    protected int beginFuel, addedFuel;
    protected float beginOil, addedOil;
    protected float fuelRate, oilRate;
    
    public abstract PathListReader readData();

    public float getFuelRate()
    {
        return fuelRate;
    }
    
    public float getOilRate()
    {
        return oilRate;
    }

    public float getBeginOil()
    {
        return beginOil;
    }

    public float getAddedOil()
    {
        return addedOil;
    }

    public int getBeginFuel()
    {
        return beginFuel;
    }

    public int getAddedFuel()
    {
        return addedFuel;
    }

    public List<Boolean> getIntercity()
    {
        return intercity;
    }

    public List<Integer> getKilos()
    {
        return kilos;
    }

    public int getBeginSpeedometer()
    {
        return beginSpeedometer;
    }
}
