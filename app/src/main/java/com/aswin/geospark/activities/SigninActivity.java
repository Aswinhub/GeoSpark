package com.aswin.geospark.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;

import com.aswin.geospark.R;
import com.aswin.geospark.base.BaseActivity;
import com.aswin.geospark.common.AppPreference;
import com.aswin.geospark.common.Constants;
import com.aswin.locationtracker.RealmDB;
import com.aswin.locationtracker.UserModel;

import io.realm.RealmList;

public class SigninActivity extends BaseActivity {
    private EditText usernameEt;
    private EditText passwordEt;
    private CardView loginCv;
    private CardView signupCv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        usernameEt = (EditText) findViewById(R.id.username_et);
        passwordEt = (EditText) findViewById(R.id.password_et);
        loginCv = (CardView) findViewById(R.id.login_cv);
        signupCv = (CardView) findViewById(R.id.signup_cv);


        loginCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (usernameEt.getText().toString().isEmpty()) {
                    usernameEt.requestFocus();
                    usernameEt.setError("Enter Username");
                } else if (passwordEt.getText().toString().isEmpty()) {
                    passwordEt.requestFocus();
                    passwordEt.setError("Enter password");
                } else
                    login();
            }
        });

        signupCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SigninActivity.this, SignupActivity.class));
            }
        });

    }

    private void login() {
        RealmDB realmDB = RealmDB.with(this);

        if (!realmDB.isUserExist(usernameEt.getText().toString())){
            if (!realmDB.isPasswordMatch(usernameEt.getText().toString(), passwordEt.getText().toString())){

                AppPreference.getInstance(this).setBoolean(Constants.ISLOGGEDIN, true);
                AppPreference.getInstance(this).setString(Constants.USERNAME, usernameEt.getText().toString());

                realmDB.setCurrentUser(usernameEt.getText().toString());
                startActivity(new Intent(this, MainActivity.class));

                finish();

            }else{
                passwordEt.requestFocus();
                passwordEt.setError("Wrong Password");
            }
        }else
            showToast("User does not exist... Please Signup to proceed");
    }
}
