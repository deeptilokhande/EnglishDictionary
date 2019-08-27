package com.deep.englishdictionarymerged;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

public class OnlineActivity extends AppCompatActivity implements View.OnClickListener {

    private String url1,url2;
    private Button btn1;
    private EditText wordET;
    private TextView defnTV,exampleTV,pronunciationTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);


        btn1 = (Button) findViewById(R.id.btn1);
        wordET = (EditText) findViewById(R.id.enter_word);
        defnTV = (TextView) findViewById(R.id.meaningTV);
        exampleTV = (TextView) findViewById(R.id.example_TV);
        pronunciationTV =(TextView) findViewById(R.id.pronunciation_TV);

        btn1.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        DictionaryRequest dictionaryRequest = new DictionaryRequest(this,defnTV,exampleTV );
        DictionaryRequestPronunciations dictionaryRequestPronunciations = new DictionaryRequestPronunciations(this,pronunciationTV);
        url1 = dictionaryEntries();
        url2=dictionaryEntries2();
        dictionaryRequest.execute(url1);
        dictionaryRequestPronunciations.execute(url2);

    }

    private String dictionaryEntries() {
        final String language = "en-us";
        final String word =wordET.getText().toString();
        final String fields = "definitions,examples";
        // final String fields2 = "examples";
        final String strictMatch = "false";
        final String word_id = word.toLowerCase();
        // return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;
        return  "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id + "?" + "fields="+fields + "&strictMatch=" + strictMatch;
    }


    private String dictionaryEntries2() {
        final String language = "en-gb";
        final String word = wordET.getText().toString();
        final String fields = "pronunciations";
        final String strictMatch = "false";
        final String word_id = word.toLowerCase();
        return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;
    }


}
