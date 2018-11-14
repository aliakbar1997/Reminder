package com.example.alik.reminder.ui;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import com.example.alik.reminder.R;
import com.example.alik.reminder.adapter.UserAdapter;
import com.example.alik.reminder.model.MyDatabaseHelper;
import com.example.alik.reminder.model.data_access.AlarmCodeDA;
import com.example.alik.reminder.model.data_access.RemindDA;
import com.example.alik.reminder.model.data_access.UserDA;
import com.example.alik.reminder.model.table_object.RemindModel;
import com.example.alik.reminder.model.table_object.UserModel;
import com.example.alik.reminder.utility.AlarmReceiver;
import com.example.alik.reminder.utility.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView userList;
    private FloatingActionButton addUser;
    private UserAdapter adapter;
    private UserDA userDA;
    private Calendar calendar;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addUser = findViewById(R.id.fab_add);
        userList = findViewById(R.id.list_of_user);

        addUser.setOnClickListener(this);

        userDA = new UserDA(this);
        final RemindDA remindDA = new RemindDA(this);

        adapter = new UserAdapter(this, new UserAdapter.UserEventHandler() {
            @Override
            public int deleteUser(int userId) {
                userDA.open();
                remindDA.open();
                remindDA.deleteRemindById(userId);
                int status =  userDA.deleteUserById(userId);
                remindDA.close();
                userDA.close();
                return status;
            }

            @Override
            public void editUser(int userId) {
                Intent editUserIntent = new Intent(MainActivity.this, EditUserInfo.class);
                editUserIntent.putExtra(Constants.EDIT_USER_KEY, userId);
                startActivityForResult(editUserIntent, Constants.EDIT_USER_REQUEST_CODE);
            }

            @Override
            public void setJob(int userId, final String firstName, final String lastName) {

                calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, final int hourOfDay, final int minute) {

                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.SEND_SMS}, 1);
                        }

                        //create dialog for setting label for the alarm
                        final Dialog dialog = new Dialog(MainActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
                        dialog.setContentView(R.layout.label_dialog_layout);
                        dialog.setTitle("Your Label");

                        final EditText editText = dialog.findViewById(R.id.label_alarm);
                        Button buttonYes  = dialog.findViewById(R.id.btn_yes);
                        final RadioGroup radioGroup = dialog.findViewById(R.id.radio_group);

                        buttonYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                int selectedId = radioGroup.getCheckedRadioButtonId();
                                RadioButton radioButton = dialog.findViewById(selectedId);

                                String label = editText.getText().toString();

                                UserDA userDA = new UserDA(MainActivity.this);
                                userDA.open();
                                UserModel tempUser = userDA.getUserByName(firstName, lastName);
                                userDA.close();

                                RemindDA remindDA = new RemindDA(MainActivity.this);

                                //for request code alarm manager
                                AlarmCodeDA alarmCodeDA = new AlarmCodeDA(MainActivity.this);
                                alarmCodeDA.open();
                                int alarmCode = alarmCodeDA.getLastId() + 1;
                                alarmCodeDA.close();

                                calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                calendar.set(Calendar.SECOND, 0);
                                calendar.set(Calendar.MILLISECOND, 0);

                                //save in db
                                SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.hour_minute));
                                String timeString = formatter.format(new Date(calendar.getTimeInMillis()));

                                RemindModel remindModel = new RemindModel();
                                remindModel.setUser_id(tempUser.getId());
                                if (radioButton.getText().toString().equals("TEXT")){
                                    remindModel.setKind(1);
                                }else if (radioButton.getText().toString().equals("SMS")){
                                    remindModel.setKind(2);
                                }
                                remindModel.setStatus(0);
                                remindModel.setLabel(label);
                                remindModel.setTime(timeString);

                                remindDA.open();
                                remindDA.addAlarm(remindModel);
                                int remindPrimary = remindDA.getLastId();
                                remindDA.close();

                                alarmCodeDA.open();
                                alarmCodeDA.addId(remindPrimary);
                                alarmCodeDA.close();


                                //setting alarm
                                AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                                Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);

                                if (radioButton.getText().toString().equals("TEXT")){
                                    intent.putExtra("alarmType", "text");
                                }else if (radioButton.getText().toString().equals("SMS")){
                                    intent.putExtra("alarmType", "sms");
                                }

                                intent.putExtra("label", label);
                                intent.putExtra("firstname", tempUser.getFirstName());
                                intent.putExtra("lastname", tempUser.getLastName());
                                intent.putExtra("phonenumber", tempUser.getPhoneNumber());
                                intent.putExtra("remindkey", remindPrimary);

                                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, alarmCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                                //end of setting alarm

                                dialog.dismiss();


                            }
                        });

                        dialog.show();

                    }
                }, hour, minute, DateFormat.is24HourFormat(MainActivity.this)).show();
            }
        });


        userList.setLayoutManager(new LinearLayoutManager(this));
        userList.setAdapter(adapter);

        userDA.open();
        adapter.updateAdapterData(userDA.getAllUser());
        adapter.notifyDataSetChanged();
        userDA.close();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_add){
            Intent createUserIntent = new Intent(this, CreateUser.class);
            startActivityForResult(createUserIntent, Constants.CREATE__USER_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Constants.CREATE__USER_REQUEST_CODE && resultCode == RESULT_OK) {
            userDA.open();
            adapter.updateAdapterData(userDA.getAllUser());
            adapter.notifyDataSetChanged();
            userDA.close();
        }else if (requestCode == Constants.EDIT_USER_REQUEST_CODE && resultCode == RESULT_OK){
            userDA.open();
            adapter.updateAdapterData(userDA.getAllUser());
            adapter.notifyDataSetChanged();
            userDA.close();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
//        userDA.close();
        super.onPause();
    }

    @Override
    protected void onResume() {
//        userDA.open();
        super.onResume();
    }
}
