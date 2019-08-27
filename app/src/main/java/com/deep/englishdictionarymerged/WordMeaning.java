package com.deep.englishdictionarymerged;


import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.deep.englishdictionarymerged.Fragments.FragmentAntonyms;
import com.deep.englishdictionarymerged.Fragments.FragmentDefinition;
import com.deep.englishdictionarymerged.Fragments.FragmentExamples;
import com.deep.englishdictionarymerged.Fragments.FragmentSynonyms;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WordMeaning extends AppCompatActivity {

    private ViewPager vw;

    String en_word;
    DatabaseHelper myDBHelper;
    Cursor c= null;


    public String enDefinition;
    public String synonyms;
    public String antonyms;
    public String example;

    TextToSpeech tts;

    boolean startedFromShare = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_meaning);

        //received values from Main Activity for word
        Bundle b = getIntent().getExtras();
        en_word = b.getString("en_word");

       Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if(intent.ACTION_SEND.equals(action)&& type!=null){
            if("text/plain".equals(type)){
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                startedFromShare=true;

                if(sharedText!=null){
                    Pattern p = Pattern.compile("[A-Za-z ]{1,25}");
                    Matcher m = p.matcher(sharedText);

                    if(m.matches()){
                        en_word = sharedText;
                    }else{
                        en_word="Not Available";
                    }
                }
            }
        }
        myDBHelper = new DatabaseHelper(this);

        try{
            myDBHelper.openDatabase();
        }catch (SQLException e){
            throw  e;
        }

        c=myDBHelper.getMeaning(en_word);

        if(c.moveToFirst()){
            enDefinition = c.getString(c.getColumnIndex("en_definition"));
            example = c.getString(c.getColumnIndex("example"));
            synonyms=c.getString(c.getColumnIndex("synonyms"));
            antonyms=c.getString(c.getColumnIndex("antonyms"));

            myDBHelper.insertHistory(en_word);
        }
        else{
            en_word = "Not Available";
        }



        ImageButton btnSpeak = (ImageButton) findViewById(R.id.btn_speak);

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts = new TextToSpeech(WordMeaning.this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        if(i==TextToSpeech.SUCCESS){
                            int result = tts.setLanguage(Locale.getDefault());
                            if(result==TextToSpeech.LANG_MISSING_DATA||result==TextToSpeech.LANG_NOT_SUPPORTED){
                                Log.e("error","This language is not supported");
                            }else{
                                tts.speak(en_word,TextToSpeech.QUEUE_FLUSH,null);
                            }
                        }
                        else{
                            Log.e("error","Initialization of TTS failed");
                        }
                    }
                });
            }
        });




        Toolbar toolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(en_word);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        vw = (ViewPager) findViewById(R.id.tab_viewpager);
        if(vw!=null){
            setUpViewPager(vw);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(vw);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vw.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private class ViewPagerAdapter extends FragmentPagerAdapter{
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager){
            super(manager);
        }

        void addFrag(Fragment frag,String title){
            mFragmentList.add(frag);
            mFragTitleList.add(title);

        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragTitleList.get(position);
        }
    }


    private void setUpViewPager(ViewPager vw){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FragmentDefinition(),getString(R.string.string_def));
        adapter.addFrag(new FragmentSynonyms(), getString(R.string.string_syno));
        adapter.addFrag(new FragmentAntonyms(), getString(R.string.string_anto));
        adapter.addFrag(new FragmentExamples(), getString(R.string.string_exam));

        vw.setAdapter(adapter);

    }
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==android.R.id.home){

            if(startedFromShare)
            {
                Intent intent = new Intent(this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }else{

            //onBackPressed();
                Intent intent = new Intent(this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }


}
