package com.deep.englishdictionarymerged;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DictionaryRequestPronunciations extends AsyncTask<String,Integer,String> {
    // String myUrl;
    Context context;
    TextView pronunciationTV;

    DictionaryRequestPronunciations(Context context, TextView pronunciationTV){
        this.context = context;
        this.pronunciationTV = pronunciationTV;

    }


    @Override
    protected String doInBackground(String... strings) {
        //myUrl =  strings[0];
        final String app_id = "d2d2ae46";
        final String app_key = "74ef692c3a6aa2e0ce4b29499794d4d8";
        try {

            URL url2 = new URL(strings[0]);

            HttpsURLConnection urlConnection = (HttpsURLConnection) url2.openConnection();
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("app_id",app_id);
            urlConnection.setRequestProperty("app_key",app_key);


            // read the output from the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }




            return stringBuilder.toString();

        }
        catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    @Override
    protected void onPostExecute(String ss) {
        super.onPostExecute(ss);
        //Toast.makeText(context,s, Toast.LENGTH_LONG).show();
        //synonymTV.setText(ss);
        //System.out.println(s);

        //convert json object returned from api to string to show in textview.
       String pronounciation;

        try {
            JSONObject js = new JSONObject(ss);

            JSONArray results =js.getJSONArray("results");

            JSONObject resultObj = results.getJSONObject(0);
            JSONArray lexicalEntriesArray = resultObj.getJSONArray("lexicalEntries");

            JSONObject lexicalEntriesObj = lexicalEntriesArray.getJSONObject(1);
            JSONArray pronunciationsArray = lexicalEntriesObj.getJSONArray("pronunciations");

            JSONObject pronunciationObj = pronunciationsArray.getJSONObject(0);
            pronounciation = pronunciationObj.getString("phoneticSpelling");





            //pronounciation = definitionArray.getString(0);


            pronunciationTV.setText("Pronunciation :"+pronounciation);





        } catch (JSONException ee) {
            pronunciationTV.setText("No pronunciation info available\n");

            ee.printStackTrace();
        }
}
}
