package com.deep.englishdictionarymerged;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LauncherActivity extends AppCompatActivity implements View.OnClickListener {
Button onlineButton, offlineButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        onlineButton =   findViewById(R.id.online_dictionary);
        offlineButton =   findViewById(R.id.offline_dictionary);

        offlineButton.setOnClickListener(this);
        onlineButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.online_dictionary :
                Intent onlineintent = new Intent(getApplicationContext(),OnlineActivity.class);
                startActivity(onlineintent);
                break;

            case R.id.offline_dictionary :
                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
                break;

        }

    }
}
