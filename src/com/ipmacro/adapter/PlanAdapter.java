package com.ipmacro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipmacro.iptv.R;
import com.ipmacro.model.Plan;

import java.util.List;

public class PlanAdapter extends BaseAdapter{
    Context context;
    List<Plan> list ;
    
    public PlanAdapter(Context context,List<Plan> list){
        this.context = context;
        this.list = list;
    }
    
    @Override
    public int getCount() {
        return list!=null?list.size():0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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
            view=LayoutInflater.from(context).inflate(R.layout.info_plan_item,null); 
            holder.txtExpiry = (TextView) view.findViewById(R.id.txt_expiry);
            holder.txtName = (TextView) view.findViewById(R.id.txt_name);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        Plan plan = list.get(position);
        holder.txtName.setText(plan.getName());
        holder.txtExpiry.setText(plan.getDateOfExpiry().substring(0, 10));
        return view;
    }
    
    static class ViewHolder {
        TextView txtName;
        TextView txtExpiry;
    }
}
