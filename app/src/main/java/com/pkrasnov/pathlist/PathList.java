package com.pkrasnov.pathlist;
import java.util.*;
import java.lang.Math;

public class PathList
{
    
    private int beginSpeedometer;
    private int fullPath, cityPath, intercityPath, fullMutohours;
    private int beginFuel, endFuel, addedFuel;
    private int fullLoadedPath;
    private float beginOil, endOil, addedOil, consumedOil, consumedFuel;
    private List<Integer> speedometers = new ArrayList<Integer>();
    private List<Integer> kilos;
    private List<Float> weights;
    private List<Float> factJobs = new ArrayList<Float>();
    private List<Float> possibleJobs = new ArrayList<Float>();
    private List<Integer> loadedPaths = new ArrayList<Integer>();
    private List<Integer> motohours = new ArrayList<Integer>();
    private float maxWeight, fullWeight, fullFactJob, fullPossibleJob, percentage;
    private List<Boolean> intercity;
    private float fuelRate, fuelCity, fuelIntercity, fuelMotohours, motohourRate, oilRate;

    
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
        oilRate = reader.getOilRate();
        weights = reader.getWeights();
        maxWeight = reader.getMaxWeight();
        motohours = reader.getMotohours();
        motohourRate = reader.getMotohourRate();
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
            fullPath = cityPath + intercityPath;
            
            factJobs.clear();
            possibleJobs.clear();
            loadedPaths.clear();
            fullWeight = fullFactJob = fullPossibleJob = 0f;
            fullLoadedPath = fullMutohours = 0;
            for (int i = 0; i < weights.size(); i++)
            {
                float cfj = Math.round(weights.get(i) * kilos.get(i) * 10f) / 10f;
                float cpj = Math.round(maxWeight * kilos.get(i) * 10f) / 10f;
                int   clp = (weights.get(i)>0)?kilos.get(i):0;
                factJobs.add(cfj);
                possibleJobs.add(cpj);
                loadedPaths.add(clp);
                fullFactJob += cfj;
                fullPossibleJob += cpj;
                fullWeight += Math.round(weights.get(i) * 10f) / 10f;
                fullLoadedPath += clp;
                fullMutohours += Math.round(motohours.get(i));
            }
            fuelMotohours = Math.round(fullMutohours * motohourRate * 10f) / 10f;
            consumedFuel = Math.round((fuelCity + fuelIntercity + fuelMotohours) * 100) / 100f;
            consumedOil  = Math.round(consumedFuel * oilRate) / 100f;
            endFuel = beginFuel + addedFuel - Math.round(consumedFuel);
            endOil = Math.round((beginOil + addedOil - consumedOil) * 100) / 100f;
            percentage = Math.round(fullFactJob / fullPossibleJob * 100f) / 100f;
            fullFactJob = Math.round(fullFactJob * 10) / 10f;
            fullPossibleJob = Math.round(fullPossibleJob * 10) / 10;
            fullWeight = Math.round(fullWeight * 10) / 10f;
        }
        else
        {
            intercityPath = cityPath = fullLoadedPath = 0;
            fuelCity = fuelIntercity = 0;
            consumedFuel = consumedOil = 0f;
            fullWeight = fullFactJob = fullPossibleJob = percentage = 0f;
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
    
    public List<Float> getPossibleJobs()
    {
        return possibleJobs;
    }

    public List<Float> getFactJobs()
    {
        return factJobs;
    }

    public List<Float> getWeights()
    {
        return weights;
    }
    
    public List<Integer> getLoadedPaths()
    {
        return loadedPaths;
    }
    
    public List<Integer> getMotohours()
    {
        return motohours;
    }
    
    public int getFullMotohours()
    {
        return fullMutohours;
    }
    
    public float getFuelMotohours()
    {
        return fuelMotohours;
    }
    
    public float getMotohourRate()
    {
        return motohourRate;
    }
    
    public int getFullLoadedPath()
    {
        return fullLoadedPath;
    }
    
    public float getPercentage()
    {
        return percentage;
    }

    public float getMaxWeight()
    {
        return maxWeight;
    }

    public float getFullWeight()
    {
        return fullWeight;
    }

    public float getFullFactJob()
    {
        return fullFactJob;
    }

    public float getFullPossibleJob()
    {
        return fullPossibleJob;
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
    
    public float getOilRate()
    {
        return oilRate;
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
        if (speedometers.isEmpty()) return beginSpeedometer;
        else return speedometers.get(speedometers.size()-1);
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
    
    public boolean hasWeight()
    {
        return !(maxWeight == 0);
    }
    
    public boolean hasOil()
    {
        return !(oilRate == 0);
    }
    
    public boolean hasMotohours()
    {
        return !(motohourRate == 0);
    }
}

