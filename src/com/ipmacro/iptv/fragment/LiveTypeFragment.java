package com.ipmacro.iptv.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.ipmacro.adapter.LiveGridAdapter;
import com.ipmacro.app.Config;
import com.ipmacro.app.IptvApplication;
import com.ipmacro.iptv.R;
import com.ipmacro.model.ChannelType;


public class LiveTypeFragment extends Fragment{
    IptvApplication mApp;
    Context mContext;
    int typeId;
    
    View parent;
    GridView gridView;
    LiveGridAdapter adapter;
    public static synchronized LiveTypeFragment getFragment(int typeId) {
        LiveTypeFragment mFragment = new LiveTypeFragment();
        mFragment.typeId = typeId;
        return mFragment;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mApp = (IptvApplication) getActivity().getApplication();
        mContext = getActivity();
        
        parent = inflater.inflate(R.layout.live_type, null);
        gridView = (GridView) parent.findViewById(R.id.gridview);
        adapter = new LiveGridAdapter(mContext);
        gridView.setAdapter(adapter);
        ChannelType type = mApp.typeList.get(typeId);
        adapter.update(type.getChannelList()); Log.e(Config.TAG,"live type fragment:"+type.getName());
        return parent;
    }
}
