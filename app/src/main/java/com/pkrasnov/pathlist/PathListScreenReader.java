package com.pkrasnov.pathlist;

import java.util.*;

public class PathListScreenReader extends PathListReader
        {
            private List<EditableView> views;
            
            public PathListScreenReader(List<EditableView> views)
            {
                this.views = views;
            }

            @Override
            public PathListReader readData()
            {
                beginSpeedometer = Math.round(views.get(0).getValue());
                beginFuel = Math.round(views.get(1).getValue());
                addedFuel = Math.round(views.get(2).getValue());
                beginOil = views.get(3).getValue();
                addedOil = views.get(4).getValue();
                fuelRate = views.get(5).getValue();
                oilRate = views.get(6).getValue();
                motohourRate = views.get(7).getValue();
                maxWeight = views.get(8).getValue();
                kilos.clear();
                intercity.clear();
                weights.clear();
                motohours.clear();
                for (int i = 9; i < views.size(); i++)
                {
                    if ((i - 9) % 3 == 0)
                    {
                        kilos.add(Math.round(views.get(i).getValue()));
                        intercity.add(views.get(i).isSpecialSelected());
                    }
                    else if ((i - 9) % 3 == 1)
                    {
                        weights.add(Math.round(views.get(i).getValue() * 10) /10f);
                    }
                    else 
                    {
                        motohours.add(Math.round(views.get(i).getValue()));
                    }
                }
                return this;
            }
        }
