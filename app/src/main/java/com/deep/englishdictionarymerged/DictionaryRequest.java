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

public class DictionaryRequest extends AsyncTask<String,Integer,String> {


   // String myUrl;
    Context context;
    TextView defnTV,exampleTV;

    DictionaryRequest(Context context, TextView defnTV,TextView exampleTV){
          this.context = context;
          this.defnTV = defnTV;
          this.exampleTV = exampleTV;
    }


    @Override
    protected String doInBackground(String... strings) {
        //myUrl =  strings[0];
        final String app_id = "d2d2ae46";
        final String app_key = "74ef692c3a6aa2e0ce4b29499794d4d8";
        try {

            URL url1 = new URL(strings[0]);

            HttpsURLConnection urlConnection = (HttpsURLConnection) url1.openConnection();
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
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
       //Toast.makeText(context,s,Toast.LENGTH_LONG).show();
        //System.out.println(s);

        //convert json object returned from api to string to show in textview.
        String definition;
        String example;
        try {
            JSONObject js = new JSONObject(s);

            JSONArray results =js.getJSONArray("results");

            JSONObject lexicalEntries = results.getJSONObject(0);
            JSONArray lexicalArray = lexicalEntries.getJSONArray("lexicalEntries");

            JSONObject entries = lexicalArray.getJSONObject(0);
            JSONArray entryArray = entries.getJSONArray("entries");

            JSONObject senses = entryArray.getJSONObject(0);
            JSONArray sensesArray = senses.getJSONArray("senses");

            JSONObject def  = sensesArray.getJSONObject(0);
            JSONArray definitionArray = def.getJSONArray("definitions");


            definition = definitionArray.getString(0);


            defnTV.setText("Meaning :"+definition);





        } catch (JSONException ee) {
            defnTV.setText("Invalid word!\n");

            ee.printStackTrace();
        }

        try {
            JSONObject js = new JSONObject(s);

            JSONArray results =js.getJSONArray("results");

            JSONObject lexicalEntries = results.getJSONObject(0);
            JSONArray lexicalArray = lexicalEntries.getJSONArray("lexicalEntries");

            JSONObject entries = lexicalArray.getJSONObject(0);
            JSONArray entryArray = entries.getJSONArray("entries");

            JSONObject senses = entryArray.getJSONObject(0);
            JSONArray sensesArray = senses.getJSONArray("senses");

            JSONObject sensesObj  = sensesArray.getJSONObject(0);
            JSONArray exampleArray = sensesObj.getJSONArray("examples");

            JSONObject exampleText = exampleArray.getJSONObject(0);

            example = exampleText.getString("text");

            exampleTV.setText("Example :"+example);




        } catch (JSONException e) {
            exampleTV.setText("Did not find relevant examples\n");

            e.printStackTrace();
        }

    }
}
