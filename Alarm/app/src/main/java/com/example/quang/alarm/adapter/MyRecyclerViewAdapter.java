package com.example.quang.alarm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.quang.alarm.MainActivity;
import com.example.quang.alarm.R;
import com.example.quang.alarm.model.ItemAlarm;
import com.example.quang.alarm.utils.DatabaseUtil;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<ItemAlarm> arr;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    private Context context;

    // constructor
    public MyRecyclerViewAdapter(Context context, List<ItemAlarm> data) {
        this.mInflater = LayoutInflater.from(context);
        this.arr = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_alarm, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String time = arr.get(position).getTime();
        String title = arr.get(position).getTitle();
        String loop = arr.get(position).getLoop();
        holder.tvTime.setText(time);
        holder.tvTitle.setText(title);
        holder.tvLoop.setText(loop);
        if (arr.get(position).getEnable() == 1){
            holder.aSwitch.setChecked(true);
        }else if (arr.get(position).getEnable() == 0){
            holder.aSwitch.setChecked(false);
        }

        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    ItemAlarm itemAlarm = arr.get(position);
                    itemAlarm.setEnable(1);
                    DatabaseUtil databaseUtil = new DatabaseUtil(context);
                    databaseUtil.update(itemAlarm);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTime;
        TextView tvTitle;
        TextView tvLoop;
        Switch aSwitch;

        ViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTitle = itemView.findViewById(R.id.tvItemTitle);
            tvLoop = itemView.findViewById(R.id.tvItemLoop);
            aSwitch = itemView.findViewById(R.id.switchEnable);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    public ItemAlarm getItem(int id) {
        return arr.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}