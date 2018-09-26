package com.aswin.geospark.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aswin.geospark.R;
import com.aswin.geospark.base.BaseActivity;
import com.aswin.geospark.common.AppPreference;
import com.aswin.locationtracker.LocationTracker;
import com.aswin.locationtracker.RealmDB;

public class MainActivity extends BaseActivity implements View.OnClickListener {


    private ImageView logoutIv;
    private TextView logoutTv;
    private TextView textView4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService();
        initView();
    }

    private void initView() {
        logoutIv = (ImageView) findViewById(R.id.logout_iv);
        logoutTv = (TextView) findViewById(R.id.logout_tv);
        textView4 = (TextView) findViewById(R.id.textView4);

        logoutIv.setOnClickListener(this);
        logoutTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        AppPreference.getInstance(this).deletePreference();
        RealmDB.with(this).deleteCurrentUser();
        stopService();
        startActivity(new Intent(this, SigninActivity.class));
        finish();
    }
}
