package com.example.alik.reminder.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alik.reminder.R;
import com.example.alik.reminder.model.data_access.UserDA;
import com.example.alik.reminder.model.table_object.UserModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateUser extends AppCompatActivity implements View.OnClickListener {

    private ImageView userImageBlur;
    private CircleImageView userProfileImage;
    private EditText firstName, lastName, phoneNumber;
    private TextInputLayout TILFirstName, TILLastName, TILPhoneNumber;
    private Button addUser;
    private UserDA userDA;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user);

        userImageBlur = findViewById(R.id.user_image_blur);
        userProfileImage = findViewById(R.id.profile_image);

        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        phoneNumber = findViewById(R.id.phone_number);
        TILFirstName = findViewById(R.id.input_layout_first_name);
        TILLastName = findViewById(R.id.input_layout_last_name);
        TILPhoneNumber = findViewById(R.id.input_layout_phone_number);

        addUser = findViewById(R.id.add_user_btn);

        addUser.setOnClickListener(this);
        userDA = new UserDA(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_user_btn){
            if (submitForm()) {
                UserModel userModel = new UserModel();
                userModel.setFirstName(firstName.getText().toString());
                userModel.setLastName(lastName.getText().toString());
                userModel.setPhoneNumber(phoneNumber.getText().toString());
                userDA.addUser(userModel);
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    /**
     * Validating form
     */
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

        Toast.makeText(getApplicationContext(), firstName.getText().toString() + lastName.getText().toString() + " added!!!",
                Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        userDA.open();
        super.onResume();
    }
}
