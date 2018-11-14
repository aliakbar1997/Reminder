package com.example.alik.reminder.ui.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.alik.reminder.R;

public class AlertDialogAlarmResult extends Activity {

    AlertDialog.Builder mAlertDlgBuilder;
    AlertDialog mAlertDialog;
    View mDialogView = null;
    Button mOKBtn;
    TextView labelTX, fullnameTX;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Build the dialog
        mAlertDlgBuilder = new AlertDialog.Builder(this);
        mDialogView = getLayoutInflater().inflate(R.layout.notif_dialog_layout, null);
        mOKBtn = mDialogView.findViewById(R.id.button_ok);
        labelTX = mDialogView.findViewById(R.id.tx_title);
        fullnameTX = mDialogView.findViewById(R.id.tx_user_fullname);

        String label = getIntent().getExtras().getString("label");
        String firstname = getIntent().getExtras().getString("firstname");
        String lastname = getIntent().getExtras().getString("lastname");

        labelTX.setText(label);
        fullnameTX.setText(firstname + " " + lastname);

        mAlertDlgBuilder.setCancelable(false);
        mAlertDlgBuilder.setView(mDialogView);
        mOKBtn.setOnClickListener(mDialogbuttonClickListener);
        mAlertDialog = mAlertDlgBuilder.create();
        mAlertDialog.show();
//        mAlertDialog.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);

    }
    View.OnClickListener mDialogbuttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.button_ok)
            {
                mAlertDialog.dismiss();
                finish();
            }
        }
    };

}
