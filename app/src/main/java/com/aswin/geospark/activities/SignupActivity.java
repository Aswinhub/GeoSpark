package com.aswin.geospark.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;

import com.aswin.geospark.R;
import com.aswin.geospark.base.BaseActivity;
import com.aswin.locationtracker.RealmDB;
import com.aswin.locationtracker.UserModel;

import io.realm.RealmList;

public class SignupActivity extends BaseActivity implements View.OnClickListener {
    private EditText usernameEt;
    private EditText passwordEt;
    private CardView signupCv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initView();
    }

    private void initView() {
        usernameEt = (EditText) findViewById(R.id.username_et);
        passwordEt = (EditText) findViewById(R.id.password_et);
        signupCv = (CardView) findViewById(R.id.signup_cv);

        signupCv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (usernameEt.getText().toString().isEmpty()) {
            usernameEt.requestFocus();
            usernameEt.setError("Enter Username");
        } else if (passwordEt.getText().toString().isEmpty()) {
            passwordEt.requestFocus();
            passwordEt.setError("Enter password");
        } else
            register();
    }

    private void register() {
        if (RealmDB.with(this).isUserExist(usernameEt.getText().toString())){
            UserModel userModel = new UserModel(usernameEt.getText().toString(), passwordEt.getText().toString(), new RealmList<String>(), new RealmList<String>());
            if (RealmDB.with(this).updateUser(userModel)){
                showToast("Signup Successfull");
                finish();
            }
        }else
            showToast("User Already Exist. Please Login to proceed");
    }
}
