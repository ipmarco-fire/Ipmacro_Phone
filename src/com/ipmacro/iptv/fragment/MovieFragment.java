
package com.ipmacro.iptv.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ipmacro.adapter.ScrollingTabsMovieAdapter;
import com.ipmacro.app.IptvApplication;
import com.ipmacro.iptv.MainActivity;
import com.ipmacro.iptv.R;
import com.ipmacro.model.MovieType;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.linkin.fragment.FragmentPagerAdapter;
import com.linkin.utils.HttpUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.scrollabletab.ScrollableTabView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieFragment extends Fragment {
    int category;
    
    IptvApplication mApp;
    Context mContext;
    View parent;
    FragmentAdapter mAdapter;
    ScrollingTabsMovieAdapter scrollingTabsAdapter;
    List<Fragment> fragmentList;
    List<MovieType> typeList;
    
    LinearLayout layoutLoading;
    private ScrollableTabView scrollableTabView;
    private ViewPager viewPager;
    private Handler mHandler = new Handler();
    
    public static synchronized MovieFragment getFragment(int category) {
        MovieFragment fragment = new MovieFragment();
        fragment.category = category;
        return fragment;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mApp = (IptvApplication) getActivity().getApplication();
        mContext = getActivity();
        parent = inflater.inflate(R.layout.movie, null);
        
        layoutLoading = (LinearLayout) parent.findViewById(R.id.layout_loading);
        
        scrollableTabView = (ScrollableTabView) parent.findViewById(R.id.scrollabletabview);
        viewPager = (ViewPager) parent.findViewById(R.id.vp_list);
        viewPager.setOnPageChangeListener(onPageChangeListener);
        
        initData();
        return parent;
    }
    
    private void initData(){
        layoutLoading.setVisibility(View.VISIBLE);
        String url = getResources().getString(R.string.weburl) + "/channel/stb/getProgramType2.htm?category=" + category;
        HttpUtil.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(JSONArray response) {
                try {
                    parserType(response);
                    refreshContent();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Throwable error) {
            }
            @Override
            public void onFinish() {
                layoutLoading.setVisibility(View.GONE);
                super.onFinish();
            }
        });
    }
    
    private void parserType(JSONArray response) throws JSONException{
        typeList = new ArrayList<MovieType>();
        
        for(int i = 0;i<response.length();i++){
            JSONObject obj = response.getJSONObject(i);
            int id = obj.getInt("id");
            String name = obj.getString("name");
            
            MovieType type = new MovieType(id, name);
            typeList.add(type);
        }
    }
    
    protected void refreshContent() {
        fragmentList = new ArrayList<Fragment>();
        for (int i = 0; i < typeList.size(); i++) {
            fragmentList.add(MovieTypeFragment.getFragment(typeList.get(i), category));;
        }
        
        if(fragmentList.size() >=3){            //设置页面缓存数量
            viewPager.setOffscreenPageLimit(3);
        }
        
        //这里要使用 getChildFragmentManager ,不然listView,gridView不显示数据
        mAdapter = new FragmentAdapter(getChildFragmentManager(),"movie_"+category);
        viewPager.setAdapter(mAdapter);

        scrollingTabsAdapter = new ScrollingTabsMovieAdapter(getActivity(), typeList);
        scrollableTabView.setAdapter(scrollingTabsAdapter);
        scrollableTabView.setViewPage(viewPager);
    }
    OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            if (scrollableTabView != null) {
                scrollableTabView.selectTab(position);
            }

            switch (position) {
                case 0:
                    ((MainActivity)getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    break;
                default:
                    ((MainActivity)getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
                    break;
            }
        }
    };

    class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm,String name) {
            super(fm , name);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position < fragmentList.size()) {
                fragment = fragmentList.get(position);
            } else {
                fragment = fragmentList.get(0);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return fragmentList != null ? fragmentList.size() : 0;
        }
    }
}
