/*
 * 
 * 直播导航
 */

package com.ipmacro.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.ipmacro.iptv.R;
import com.ipmacro.model.ChannelType;
import com.ipmacro.model.MovieType;
import com.scrollabletab.TabAdapter;

import java.util.List;

public class ScrollingTabsMovieAdapter implements TabAdapter {
    List<MovieType> typeList;
    Activity activity;

    public ScrollingTabsMovieAdapter(Activity act, List<MovieType> typeList) {
        this.typeList = typeList;
        this.activity = act;
    }

    @Override
    public View getView(int position) {
        if(activity != null){
            LayoutInflater inflater = activity.getLayoutInflater();
            final Button tab = (Button) inflater.inflate(R.layout.scrollable_tab, null);
            MovieType type = this.typeList.get(position);
            tab.setText(type.getName());
            return tab;
        }else{
            return null;
        }
    }

}
