package com.mxth.timeselector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cxp.timeselector.PopuTextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private PopuTextView tvLiveTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvLiveTime = (PopuTextView) findViewById(R.id.tv_live_time);
        tvLiveTime.setOnDateSelectedListener(new PopuTextView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Calendar calendar) {
                tvLiveTime.setRightTimeTrue(true);
            }
        });
    }
    public void goSelectLiveTime(View view){
        tvLiveTime.onClick();
    }
}
