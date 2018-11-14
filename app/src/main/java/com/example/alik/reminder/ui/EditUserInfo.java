package com.example.alik.reminder.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alik.reminder.R;
import com.example.alik.reminder.model.data_access.UserDA;
import com.example.alik.reminder.model.table_object.UserModel;
import com.example.alik.reminder.utility.Constants;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditUserInfo extends AppCompatActivity implements View.OnClickListener {

    private ImageView userImageBlur;
    private CircleImageView userProfileImage;
    private EditText firstName, lastName, phoneNumber;
    private TextInputLayout TILFirstName, TILLastName, TILPhoneNumber;
    private Button editUser;
    private UserDA userDA;
    private int userIdFromIntent;
    private UserModel userModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            userIdFromIntent = bundle.getInt(Constants.EDIT_USER_KEY);
        }

        userImageBlur = findViewById(R.id.user_image_blur_edit);
        userProfileImage = findViewById(R.id.profile_image_edit);

        firstName = findViewById(R.id.first_name_edit);
        lastName = findViewById(R.id.last_name_edit);
        phoneNumber = findViewById(R.id.phone_number_edit);
        TILFirstName = findViewById(R.id.input_layout_first_name_edit);
        TILLastName = findViewById(R.id.input_layout_last_name_edit);
        TILPhoneNumber = findViewById(R.id.input_layout_phone_number_edit);

        editUser = findViewById(R.id.confirm_edit_user_btn);

        userDA = new UserDA(this);
        getUserInfoFromDB(userIdFromIntent);


        firstName.setText(userModel.getFirstName());
        lastName.setText(userModel.getLastName());
        phoneNumber.setText(userModel.getPhoneNumber());

        editUser.setOnClickListener(this);

    }

    private void getUserInfoFromDB(int userId) {
        userDA.open();
        userModel = userDA.getUserById(userId);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.confirm_edit_user_btn){
            if (submitForm()) {
                UserModel userModel = new UserModel();
                userModel.setId(userIdFromIntent);
                userModel.setFirstName(firstName.getText().toString());
                userModel.setLastName(lastName.getText().toString());
                userModel.setPhoneNumber(phoneNumber.getText().toString());
                if (userDA.updateUserById(userModel) > 0) {
                    Toast.makeText(getApplicationContext(), firstName.getText().toString() + lastName.getText().toString() + " edited!!!",
                            Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }
            }
        }
    }

    private boolean submitForm() {
        if (!validateFirstName()) {
            return false;
        }

        if (!validateLastName()) {
            return false;
        }

        if (!validatePhoneNumber()) {
            return false;
        }

        return true;
    }

    private boolean validateFirstName() {
        if (firstName.getText().toString().trim().isEmpty()) {
            TILFirstName.setError("enter your first name");
            requestFocus(firstName);
            return false;
        }else {
            TILFirstName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateLastName() {
        if (lastName.getText().toString().trim().isEmpty()) {
            TILFirstName.setError("enter your last name");
            requestFocus(lastName);
            return false;
        }else {
            TILLastName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePhoneNumber() {
        if (phoneNumber.getText().toString().trim().isEmpty()) {
            TILPhoneNumber.setError("enter your phone number");
            requestFocus(phoneNumber);
            return false;
        }else {
            TILPhoneNumber.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        userDA.close();
        super.onPause();
    }
//
//    @Override
//    protected void onResume() {
//        userDA.open();
//        super.onResume();
//    }
}
