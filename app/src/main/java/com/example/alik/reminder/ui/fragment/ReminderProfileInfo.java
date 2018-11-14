package com.example.alik.reminder.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alik.reminder.R;
import com.example.alik.reminder.adapter.ReminderProfileInfoAdapter;
import com.example.alik.reminder.model.data_access.RemindDA;
import com.example.alik.reminder.model.data_access.UserDA;
import com.example.alik.reminder.model.table_object.RemindModel;
import com.example.alik.reminder.model.table_object.UserModel;

import java.util.ArrayList;
import java.util.List;

public class ReminderProfileInfo extends Fragment {

    private int userId;
    private RecyclerView rec;
    private ReminderProfileInfoAdapter adapter;
    private List<RemindModel> remindModelList;

    public static ReminderProfileInfo newInstance(){
        return new ReminderProfileInfo();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_info_reminder_layout, container, false);;
        rec = view.findViewById(R.id.rec_reminer_info);
        userId = getArguments().getInt("user_id");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserDA userDA = new UserDA(getActivity());
        userDA.open();
        //get the user id
        UserModel userModel = userDA.getUserById(userId);
        userDA.close();

        remindModelList = new ArrayList<>();

        RemindDA remindDA = new RemindDA(getActivity());
        remindDA.open();
        //get the user reminder info
        remindModelList = remindDA.getInfoByUserId(userModel.getId());
        remindDA.close();

        adapter = new ReminderProfileInfoAdapter(getActivity());

        rec.setLayoutManager(new LinearLayoutManager(getActivity()));
        rec.setAdapter(adapter);

        adapter.updateAdapterData(remindModelList);
        adapter.notifyDataSetChanged();

    }
}
