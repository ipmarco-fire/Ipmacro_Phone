package com.ipmacro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipmacro.adapter.LiveGridAdapter.ViewHolder;
import com.ipmacro.iptv.R;
import com.ipmacro.model.Vod;
import com.linkin.utils.SyncImageLoader;

import java.util.ArrayList;
import java.util.List;

public class MovieGridAdapter extends BaseAdapter{
    Context context;
    SyncImageLoader imageLoader;
    String webUrl ;
    List<Vod> dataList;
    
    public MovieGridAdapter(Context context) {
        this.context = context;
        imageLoader = new SyncImageLoader(context);
        webUrl = context.getResources().getString(R.string.weburl);
        dataList = new ArrayList<Vod>();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
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
            view=LayoutInflater.from(context).inflate(R.layout.movie_grid_item,null); 
            holder.imgLogo = (ImageView) view.findViewById(R.id.img_logo);
            holder.txtName = (TextView) view.findViewById(R.id.txt_name);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        
        Vod vod = dataList.get(position);
        imageLoader.displayImage(holder.imgLogo, webUrl + vod.getLogo());
        holder.txtName.setText(vod.getName());
        return view;
    }
    
    static class ViewHolder {
        ImageView imgLogo;
        TextView txtName;
    }

    public void update( List<Vod> list) {
        for(Vod vod:list){
            dataList.add(vod);
        }
        notifyDataSetChanged();
    }
}
