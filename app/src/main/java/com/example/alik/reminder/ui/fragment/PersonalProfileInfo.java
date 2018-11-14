package com.example.alik.reminder.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alik.reminder.R;
import com.example.alik.reminder.model.data_access.UserDA;
import com.example.alik.reminder.model.table_object.UserModel;

public class PersonalProfileInfo extends Fragment {

    private TextView firstName, lastName, phoneNumber;
    private int userId;

    public static PersonalProfileInfo newInstance(){
        return new PersonalProfileInfo();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_info_personal_layout, container, false);;
        firstName = view.findViewById(R.id.first_name_info);
        lastName = view.findViewById(R.id.last_name_info);
        phoneNumber = view.findViewById(R.id.phone_number_info);
        userId = getArguments().getInt("user_id");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserDA userDA = new UserDA(getActivity());
        userDA.open();
        UserModel userModel = userDA.getUserById(userId);
        userDA.close();

        firstName.setText(userModel.getFirstName());
        lastName.setText(userModel.getLastName());
        phoneNumber.setText(userModel.getPhoneNumber());
    }
}
