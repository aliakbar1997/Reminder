package com.example.alik.reminder.utility;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.example.alik.reminder.R;
import com.example.alik.reminder.model.data_access.RemindDA;
import com.example.alik.reminder.ui.MainActivity;
import com.example.alik.reminder.ui.dialog.AlertDialogAlarmResult;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String label = intent.getStringExtra("label");
        String firstname = intent.getStringExtra("firstname");
        String lastname = intent.getStringExtra("lastname");
        String phoneNumber = intent.getStringExtra("phonenumber");
        int reminderKey = intent.getIntExtra("remindkey", 0);

        String alarmType = intent.getStringExtra("alarmType");

        if (alarmType.equals("text")){
            Intent intent1 = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);

            WakeLocker.acquire(context);

            //set done in db
            RemindDA remindDA = new RemindDA(context);
            remindDA.open();
            remindDA.updateStatusById(reminderKey, 1);
            remindDA.close();

            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = new Notification.Builder(context)
                    .setContentTitle("Alarm Notification")
                    .setContentText(label)
                    .setSmallIcon(R.drawable.ic_notifications_active_red_a700_24dp)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();
            notificationManager.notify(1, notification);

            Intent alarmIntent = new Intent("android.intent.action.MAIN");
            alarmIntent.setClass(context, AlertDialogAlarmResult.class);
            alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            alarmIntent.putExtra("label", label);
            alarmIntent.putExtra("firstname", firstname);
            alarmIntent.putExtra("lastname", lastname);
            context.startActivity(alarmIntent);

        }else if (alarmType.equals("sms")){
            try{
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(phoneNumber, null, "Your Job For This Time : " + label, null, null);
                //set done in db
                RemindDA remindDA = new RemindDA(context);
                remindDA.open();
                remindDA.updateStatusById(reminderKey, 1);
                remindDA.close();
                Toast.makeText(context, "SMS Send!", Toast.LENGTH_LONG).show();
            }catch (Exception e){
                Toast.makeText(context, "SMS permission not allowed", Toast.LENGTH_LONG).show();
                Log.e("printStackTrace: ", Log.getStackTraceString(e));
            }
        }


//        String s = intent.getExtras().getString("extra");
//        service.putExtra("extra", s);
//        Intent service = new Intent(context, TestService.class);
//        context.startService(service);
    }
}
