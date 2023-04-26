package com.example.mello2.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mello2.R;


public class SettingsActivity extends AppCompatActivity {

    ImageView user_image,editbtn,backbtn;
    TextView user_name;
    Switch darkmode,privatesession;
    SeekBar random,gap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        initviews();


    }

    private void initviews() {
        user_image=findViewById(R.id.user_image);
        editbtn=findViewById(R.id.user_image);
        user_name=findViewById(R.id.user_name);
        privatesession=findViewById(R.id.private_session);
        random=findViewById(R.id.seekBar2);
        gap=findViewById(R.id.seekBar3);
        backbtn=findViewById(R.id.backbutton_settings);
        random.setMax(12);
    }


    public void backPressed(View view){
        super.onBackPressed();
    }

}