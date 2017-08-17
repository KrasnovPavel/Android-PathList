package com.pkrasnov.pathlist;

import android.app.Activity;
import android.widget.AdapterView;
import android.view.View;
import android.widget.ListView;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;

public class DrawerHelper
{
    public static void initDrawer(Activity activity)
    {
        final Activity act = activity;

        mDrawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(
        activity,                  /* host Activity */
        mDrawerLayout,         /* DrawerLayout object */
        R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
        R.string.drawer_open, R.string.drawer_close){
            public void onDrawerClosed(View view) {
                act.invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView){
                act.invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private static class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    }

    public static ActionBarDrawerToggle getDrawerToggle()
    {
        return mDrawerToggle;
    }

    private static DrawerItemClickListener listener = new DrawerItemClickListener();
    private static ActionBarDrawerToggle mDrawerToggle;
    private static DrawerLayout mDrawerLayout;
} 
