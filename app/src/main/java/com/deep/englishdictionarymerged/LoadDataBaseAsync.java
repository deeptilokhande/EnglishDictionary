package com.deep.englishdictionarymerged;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

public class LoadDataBaseAsync extends AsyncTask<Void,Void,Boolean> {
    private Context context;
    private AlertDialog alertDialog;
    private DatabaseHelper myDataBaseHelper;

    public LoadDataBaseAsync(Context context){
        this.context=context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        AlertDialog.Builder d = new AlertDialog.Builder(context,R.style.myDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.alert_dialog_database_copying,null);
        d.setTitle("Loading database");
        d.setView(dialogView);
        alertDialog = d.create();

        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        myDataBaseHelper = new DatabaseHelper(context);
        try {
            myDataBaseHelper.createDataBase();

        }catch(Exception e){
            throw new Error("Database was not created");
        }
        myDataBaseHelper.close();
        return null;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        alertDialog.dismiss();
        MainActivity.openDatabase();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
