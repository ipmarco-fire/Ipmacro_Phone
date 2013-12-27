
package com.ipmacro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipmacro.iptv.R;
import com.ipmacro.model.Channel;
import com.linkin.utils.SyncImageLoader;

import java.util.List;

public class LiveGridAdapter extends BaseAdapter {
    List<Channel> channelList;
    Context context;
    SyncImageLoader imageLoader;
    String webUrl ;
    public LiveGridAdapter(Context context) {
        this.context = context;
        imageLoader = new SyncImageLoader(context);
        webUrl = context.getResources().getString(R.string.weburl);
    }

    @Override
    public int getCount() {
        return channelList != null ? channelList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return channelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            view=LayoutInflater.from(context).inflate(R.layout.live_grid_item,null); 
            holder.imgIcon = (ImageView) view.findViewById(R.id.image);
            holder.txtTitle = (TextView) view.findViewById(R.id.text);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        
        Channel ch = (Channel) getItem(position);
        holder.imgIcon.setImageDrawable(null);
        imageLoader.displayImage(holder.imgIcon, webUrl + ch.getLogo());
        holder.txtTitle.setText(ch.getName());
        return view;
    }

    public void update(List<Channel> channelList) {
        this.channelList = channelList;
        notifyDataSetChanged();
    }
    
    static class ViewHolder {
        ImageView imgIcon;
        TextView txtTitle;
    }
}
