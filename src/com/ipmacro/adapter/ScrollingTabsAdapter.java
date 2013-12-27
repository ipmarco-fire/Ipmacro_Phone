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
import com.scrollabletab.TabAdapter;

import java.util.List;

public class ScrollingTabsAdapter implements TabAdapter {
    List<ChannelType> typeList;
    Activity activity;

    public ScrollingTabsAdapter(Activity act, List<ChannelType> typeList) {
        this.typeList = typeList;
        this.activity = act;
    }

    @Override
    public View getView(int position) {
        LayoutInflater inflater = activity.getLayoutInflater();
        final Button tab = (Button) inflater.inflate(R.layout.scrollable_tab, null);
        ChannelType type = this.typeList.get(position);
        tab.setText(type.getName());
        return tab;
    }

}
