package com.ipmacro.iptv.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.ipmacro.adapter.MovieGridAdapter;
import com.ipmacro.app.Config;
import com.ipmacro.app.IptvApplication;
import com.ipmacro.iptv.R;
import com.ipmacro.iptv.VodActivity;
import com.ipmacro.model.MovieType;
import com.ipmacro.model.Vod;
import com.ipmacro.parser.MovieParser;
import com.linkin.utils.HttpUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MovieTypeFragment extends Fragment{
    public static final int PAGE_SIZE = 18;
    MovieType type;
    int category;
    
    IptvApplication mApp;
    Context mContext;
    View parent;
    PullToRefreshGridView refreshGridView;
    
    MovieGridAdapter adapter;
    String webUrl;
    int curPage = 0;
    
    public static synchronized MovieTypeFragment getFragment(MovieType type,int category) {
        MovieTypeFragment mFragment = new MovieTypeFragment();
        mFragment.type = type;
        mFragment.category = category;
        return mFragment;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(Config.TAG,"onCreateView..." + type.getName());
        mApp = (IptvApplication) getActivity().getApplication();
        mContext = getActivity();
        parent = inflater.inflate(R.layout.movie_type, null);
        
        webUrl = getResources().getString(R.string.weburl);
        
        refreshGridView = (PullToRefreshGridView) parent.findViewById(R.id.pull_refresh_grid);
        refreshGridView.setPullToRefreshEnabled(false);
        GridView gridview = refreshGridView.getRefreshableView();
        adapter = new MovieGridAdapter(mContext);
        gridview.setAdapter(adapter);
        refreshGridView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                int page = curPage + 1;
                downoloadData(page);
            }
        });
        gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Vod vod = (Vod) adapter.getItem(position);
                mApp.setParam(VodActivity.PARM_VOD, vod);
                Intent intent = new Intent(mContext,VodActivity.class);
                startActivity(intent);
            }
        });
        initData();
        return parent;
    }
    
    private void initData(){
        downoloadData(1);
    }
    
    private void downoloadData(final int page){
        String url = webUrl + "/channel/stb/getProgramListByType.htm?page="+page+"&rp="+PAGE_SIZE+"&category="+category+"&categoryType="+URLEncoder.encode(type.getName());
        HttpUtil.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    List<Vod> list = MovieParser.parserDataList(response);
                    adapter.update(list);
                    if(page == 1 && list.size() == PAGE_SIZE){
                        refreshGridView.setPullToRefreshEnabled(true);
                    }else if(list.size() < PAGE_SIZE){
                        refreshGridView.setPullToRefreshEnabled(false);
                    }
                    curPage = page;
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Throwable error) {
                Log.e(Config.TAG,"error");
            }
            @Override
            public void onFinish() {
                refreshGridView.onRefreshComplete();
            }
        });
    }
    
    
}
