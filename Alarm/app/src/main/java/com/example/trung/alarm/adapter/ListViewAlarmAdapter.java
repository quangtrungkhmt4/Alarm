package com.example.trung.alarm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.example.trung.alarm.R;
import com.example.trung.alarm.model.ItemAlarm;

import java.util.ArrayList;

public class ListViewAlarmAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<ItemAlarm> arrItem;

    public ListViewAlarmAdapter(Context context, int layout, ArrayList<ItemAlarm> arrItem) {
        this.context = context;
        this.layout = layout;
        this.arrItem = arrItem;
    }

    private class ViewHolder{
        TextView tvTime;
        TextView tvLoop;
        Switch switchEnable;
    }

    @Override
    public int getCount() {
        return arrItem.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewRow = view;
        if(viewRow == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewRow = inflater.inflate(layout,viewGroup,false);

            ViewHolder holder = new ViewHolder();
            holder.tvTime = viewRow.findViewById(R.id.tvTime);
            holder.tvLoop = viewRow.findViewById(R.id.tvItemLoop);
            holder.switchEnable = viewRow.findViewById(R.id.switchEnable);

            viewRow.setTag(holder);
        }

        ItemAlarm item = arrItem.get(i);
        ViewHolder holder = (ViewHolder) viewRow.getTag();
        holder.tvTime.setText(item.getTime());
        holder.tvLoop.setText(item.getLoop());
        holder.switchEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return viewRow;
    }
}
