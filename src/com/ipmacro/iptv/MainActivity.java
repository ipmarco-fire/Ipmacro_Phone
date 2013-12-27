
package com.ipmacro.iptv;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.ipmacro.iptv.fragment.LiveFragment;
import com.ipmacro.iptv.fragment.MenuFragment;
import com.ipmacro.iptv.fragment.MovieFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {
    protected Fragment mFragment;
    //TextView txtNav;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            mFragment = LiveFragment.getInstance();
        } else {
            mFragment = (Fragment) this.getSupportFragmentManager().getFragment(savedInstanceState, "mFragment");
        }

        setBehindContentView(R.layout.menu_frame);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_frame, new MenuFragment())
                .commit();

        setContentView(R.layout.content_frame);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, mFragment)
                .commit();

        float percent = 0.6f;
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();

        // SlidingMenu控件的初始化
        SlidingMenu sm = getSlidingMenu();
        sm.setShadowWidthRes(R.dimen.shadow_width);// 阴影宽度
        sm.setShadowDrawable(R.drawable.shadow); // 阴影Drawable
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset); // 拉开后离边框距离
        sm.setFadeDegree(0.35f); // 颜色渐变比例
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN); // 拉动事件区域 --全屏
        sm.setBehindWidth((int) (percent * screenWidth)); // 菜单宽度
        getSlidingMenu().requestLayout();

        // 自定义TextView,实现标题居中 actionbar初始化
//        txtNav = new TextView(this);
//        txtNav.setGravity(Gravity.CENTER);
//        txtNav.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//        txtNav.setTextSize(20);
//        txtNav.setTextColor(getResources().getColor(R.color.white));
//        getSupportActionBar().setCustomView(txtNav);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setIcon(R.drawable.btn_menu);
        setTitle(R.string.menu_index);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                toggle();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mFragment", mFragment);
    }

    public void switchContent(Fragment fragment) {
        if (fragment != mFragment) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if(!fragment.isAdded()){// 先判断是否被add过
                transaction.hide(mFragment).add(R.id.content_frame, fragment).commit(); // 隐藏当前的fragment，add下一个到Activity中
            }else{
                transaction.hide(mFragment).show(fragment).commit(); // 隐藏当前的fragment，显示下一个
            }
            mFragment = fragment;
        }
        getSlidingMenu().showContent();

    }
}
