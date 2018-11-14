package com.example.alik.reminder.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.alik.reminder.R;
import com.example.alik.reminder.adapter.ViewPagerAdapter;
import com.example.alik.reminder.ui.fragment.PersonalProfileInfo;
import com.example.alik.reminder.ui.fragment.ReminderProfileInfo;

public class ProfileInfo extends AppCompatActivity {

    private ViewPagerAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_info_layout);

        int userId = getIntent().getIntExtra("user_id", 0);

        PersonalProfileInfo personalProfileInfo = PersonalProfileInfo.newInstance();
        ReminderProfileInfo reminderProfileInfo = ReminderProfileInfo.newInstance();

        Bundle bundle = new Bundle();
        bundle.putInt("user_id", userId);
        personalProfileInfo.setArguments(bundle);
        reminderProfileInfo.setArguments(bundle);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(personalProfileInfo, "Personal Info");
        adapter.addFragment(reminderProfileInfo, "Reminder Info");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
