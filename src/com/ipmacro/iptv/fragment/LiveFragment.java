/*
 * 直播列表
 * 
 */

package com.ipmacro.iptv.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ipmacro.adapter.ScrollingTabsAdapter;
import com.ipmacro.app.Config;
import com.ipmacro.app.IptvApplication;
import com.ipmacro.iptv.MainActivity;
import com.ipmacro.iptv.R;
import com.ipmacro.parser.LiveParser;
import com.linkin.fragment.FragmentPagerAdapter;
import com.linkin.utils.BackgroundExecutor;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.scrollabletab.ScrollableTabView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class LiveFragment extends Fragment {
    static LiveFragment mFragment;

    IptvApplication mApp;
    Context mContext;
    View parent;

    FragmentAdapter mAdapter;
    ScrollingTabsAdapter scrollingTabsAdapter;
    List<Fragment> fragmentList;

    private ScrollableTabView scrollableTabView;
    private ViewPager viewPager;
    private Handler mHandler = new Handler();

    public static synchronized LiveFragment getInstance() {
        if (mFragment == null) {
            mFragment = new LiveFragment();
        }
        return mFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        Log.i(Config.TAG, "onAttach");
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(Config.TAG, "onCreateView");

        mApp = (IptvApplication) getActivity().getApplication();
        mContext = getActivity();
        parent = inflater.inflate(R.layout.live, null);

        scrollableTabView = (ScrollableTabView) parent.findViewById(R.id.scrollabletabview);
        viewPager = (ViewPager) parent.findViewById(R.id.vp_list);
        viewPager.setOnPageChangeListener(onPageChangeListener);

        initData();
        return parent;
    }

    private void initData() {
        Log.w(Config.TAG, "initData:" + (mApp.typeList == null));
        if (mApp.typeList == null) {
            BackgroundExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    parserData();
                }
            });
        } else {
            refreshContent();
        }
    }

    private void parserData() {
        SharedPreferences settings = mContext.getSharedPreferences(Config.SHARED_NAME, 0);
        String content = settings.getString(Config.LIVE_JSON_DATA, null);
        try {
            String favIds = settings.getString(Config.LIVE_FAV_IDS, null);
            mApp.typeList = LiveParser.parser(mContext, content, mApp.loginInfo.liveChannelIds, mApp.loginInfo.planChannelIds, favIds);
            if (mApp.typeList != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        refreshContent();
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void refreshContent() {
        fragmentList = new ArrayList<Fragment>();
        for (int i = 0; i < mApp.typeList.size(); i++) {
            fragmentList.add(LiveTypeFragment.getFragment(i));
        }
        
        if(fragmentList.size() >=3){
            viewPager.setOffscreenPageLimit(3);
        }
        
        // 这里要使用 getChildFragmentManager ,不然listView,gridView不显示数据
        mAdapter = new FragmentAdapter(getChildFragmentManager(), "live");
        viewPager.setAdapter(mAdapter);

        scrollingTabsAdapter = new ScrollingTabsAdapter(getActivity(), mApp.typeList);
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
                    ((MainActivity) getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    break;
                default:
                    ((MainActivity) getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
                    break;
            }
        }
    };

    class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm, String name) {
            super(fm, name);
            fm.beginTransaction();

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
