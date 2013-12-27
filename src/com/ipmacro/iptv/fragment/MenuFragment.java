
package com.ipmacro.iptv.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ipmacro.iptv.MainActivity;
import com.ipmacro.iptv.R;

public class MenuFragment extends ListFragment {
    MenuAdapter adapter;
    Fragment movieFragment,dramaFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menu_list, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new MenuAdapter(getActivity());
        adapter.add(new Menu(R.string.menu_index, R.drawable.ic_launcher));
        adapter.add(new Menu(R.string.menu_movie, R.drawable.ic_launcher));
        adapter.add(new Menu(R.string.menu_drama, R.drawable.ic_launcher));
        adapter.add(new Menu(R.string.menu_info, R.drawable.ic_launcher));
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = LiveFragment.getInstance();
                break;
            case 1:
                if(movieFragment == null){
                    movieFragment = MovieFragment.getFragment(1);
                }
                fragment = movieFragment;
                break;
            case 2:
                if(dramaFragment == null){
                    dramaFragment = MovieFragment.getFragment(2);
                }
                fragment = dramaFragment;
                break;
            case  3:
                fragment = InfoFragment.getInstance();
                break;
            default:
                break;
        }
        if (fragment != null) {
            switchFragment(fragment,adapter.getItem(position) );
        }
    }

    // the meat of switching the above fragment
    private void switchFragment(Fragment fragment,Menu menu) {
        if (getActivity() == null)
            return;

        MainActivity fca = (MainActivity) getActivity();
        fca.switchContent(fragment);
        fca.setTitle(menu.titleId);
    }

    private class Menu {
        public Menu(int titleId, int iconId) {
            this.titleId = titleId;
            this.iconId = iconId;
        }

        public int titleId;
        public int iconId;
    }

    public class MenuAdapter extends ArrayAdapter<Menu> {

        public MenuAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_item, null);
            }
            ImageView icon = (ImageView) convertView.findViewById(R.id.menu_icon);
            icon.setImageResource(getItem(position).iconId);
            TextView title = (TextView) convertView.findViewById(R.id.menu_title);
            title.setText(getItem(position).titleId);
            return convertView;
        }
    }
}
