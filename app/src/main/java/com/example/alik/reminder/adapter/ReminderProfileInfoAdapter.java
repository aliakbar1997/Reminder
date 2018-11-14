package com.example.alik.reminder.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alik.reminder.R;
import com.example.alik.reminder.model.table_object.RemindModel;

import java.util.Collections;
import java.util.List;

public class ReminderProfileInfoAdapter extends RecyclerView.Adapter<ReminderProfileInfoAdapter.ReminderProfileInfoviewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<RemindModel> mData = Collections.emptyList();

    public ReminderProfileInfoAdapter(Context mContext) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public class ReminderProfileInfoviewHolder extends RecyclerView.ViewHolder {

        private TextView reminderLabel, reminderTime, reminderKind, reminderStatus;

        public ReminderProfileInfoviewHolder(View itemView) {
            super(itemView);

            reminderLabel = itemView.findViewById(R.id.label);
            reminderTime = itemView.findViewById(R.id.time);
            reminderKind = itemView.findViewById(R.id.kind);
            reminderStatus = itemView.findViewById(R.id.status);
        }

    }

    public void updateAdapterData(List<RemindModel> data) {
        this.mData = data;
    }


    @NonNull
    @Override
    public ReminderProfileInfoviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mLayoutInflater.inflate(R.layout.reminer_info_row, viewGroup, false);
        return new ReminderProfileInfoviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderProfileInfoviewHolder holder, int position) {
        RemindModel remindModel =  mData.get(position);

        holder.reminderLabel.setText(remindModel.getLabel());
        holder.reminderTime.setText(remindModel.getTime());
        if (remindModel.getKind() == 1) {
            holder.reminderKind.setText("By Notification");
        }else if (remindModel.getKind() == 2){
            holder.reminderKind.setText("By SMS");
        }
        if (remindModel.getStatus() == 0) {
            holder.reminderStatus.setText("UnDone");
        }else if (remindModel.getStatus() == 1){
            holder.reminderStatus.setText("Done");
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
