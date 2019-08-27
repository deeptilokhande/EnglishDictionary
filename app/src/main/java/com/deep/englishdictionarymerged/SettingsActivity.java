package com.deep.englishdictionarymerged;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

    DatabaseHelper myDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        TextView clearHistory = (TextView) findViewById(R.id.clear_history);
        clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDbHelper = new DatabaseHelper(SettingsActivity.this);
                try {
                    myDbHelper.openDatabase();
                }catch (SQLException e){
                    e.printStackTrace();
                }
                showAlertDialog();
            }
        });

        //change 1 for new word daily
        TextView newWordAlarm = (TextView) findViewById(R.id.new_word_alarm);
        newWordAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    Calendar calendar = Calendar.getInstance();

                    calendar.add(Calendar.SECOND, 20);

                    Intent intent = new Intent("deepti.lok.action.DISPLAY_NOTIFICATION");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
                    Toast.makeText(SettingsActivity.this, "You set your daily word feature! You will see new word everyday", Toast.LENGTH_LONG).show();


            }

        });
        //end of change 1


    }

    private void showAlertDialog(){
        AlertDialog.Builder aldialog= new AlertDialog.Builder(SettingsActivity.this,R.style.myDialogTheme);
        aldialog.setTitle("Are you sure?");
        aldialog.setMessage("All history will be deleted");

        String positiveText = "Yes";
        String negativeText = "No";

        aldialog.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                myDbHelper.deleteHistory();
            }
        });

        aldialog.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = aldialog.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home ){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
