package com.deep.englishdictionarymerged;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    static DatabaseHelper myDataBaseHelper;
    static boolean dataBaseOpened = false;

    SearchView search;

    SimpleCursorAdapter suggestionAdapter;

    ArrayList<History> historyArrayList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter historyAdapter;

    RelativeLayout emptyHistory;
    Cursor cursorHistory;

    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        search = (SearchView) findViewById(R.id.search_view);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setIconified(false);

            }
        });

        myDataBaseHelper = new DatabaseHelper(this);

        if (myDataBaseHelper.checkDataBase()) {
            openDatabase();
        } else {
            LoadDataBaseAsync task = new LoadDataBaseAsync(MainActivity.this);
            task.execute();
        }

        final String[] from = new String[]{"en_word"};
        final int[] to = new int[]{R.id.suggestion_text};

        suggestionAdapter = new SimpleCursorAdapter(MainActivity.this, R.layout.suggestion_layout, null, from, to, 0) {
            @Override
            public void changeCursor(Cursor c) {
                 super.swapCursor(c);
            }
        };
        search.setSuggestionsAdapter(suggestionAdapter);

        search.setOnSuggestionListener(new SearchView.OnSuggestionListener() {


            @Override
            public boolean onSuggestionClick(int i) {
                CursorAdapter ca = search.getSuggestionsAdapter();
                Cursor cursor = ca.getCursor();
                cursor.moveToPosition(i);
                String clicked_word = cursor.getString(cursor.getColumnIndex("en_word"));
                search.setQuery(clicked_word, false);
                search.clearFocus();
                search.setFocusable(false);

                Intent intent = new Intent(MainActivity.this, WordMeaning.class);
                Bundle b = new Bundle();
                b.putString("en_word", clicked_word);
                intent.putExtras(b);
                startActivity(intent);


                return true;
            }

            @Override
            public boolean onSuggestionSelect(int i) {
                return true;
            }


        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String text = search.getQuery().toString();

                Pattern p = Pattern.compile("[A-Za-z \\-.]{1,25}");
                Matcher m = p.matcher(text);

                if (m.matches()) {
                    Cursor c = myDataBaseHelper.getMeaning(text);

                    if (c.getCount() == 0) {
                        showAlertDialog();
                    }
                    else {
                        search.clearFocus();
                        search.setFocusable(false);

                        Intent intent = new Intent(MainActivity.this, WordMeaning.class);
                        Bundle b = new Bundle();
                        b.putString("en_word", text);
                        intent.putExtras(b);
                        startActivity(intent);

                    }
                }    else {
                    showAlertDialog();
                }
                return false;
            }


            @Override
            public boolean onQueryTextChange(String s) {
                search.setIconifiedByDefault(false);

                Pattern p = Pattern.compile("[A-Za-z \\-.]{1,25}");
                Matcher m = p.matcher(s);

                if (m.matches()) {
                    Cursor cursorSuggestion = myDataBaseHelper.getSuggestions(s);
                    suggestionAdapter.changeCursor(cursorSuggestion);
                }

                return false;
            }
        });

        emptyHistory = (RelativeLayout) findViewById(R.id.empty_history);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_history);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        fetch_history();


    }

    protected static void openDatabase() {
        try {
            myDataBaseHelper.openDatabase();
            dataBaseOpened = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetch_history() {
        historyArrayList = new ArrayList<>();
        historyAdapter = new RecyclerViewAdaptorHistory(this, historyArrayList);
        recyclerView.setAdapter(historyAdapter);

        History h;

        if (dataBaseOpened) {
            cursorHistory = myDataBaseHelper.getHistory();
            if (cursorHistory.moveToFirst()) {
                do {
                    h = new History(cursorHistory.getString(cursorHistory.getColumnIndex("word")), cursorHistory.getString(cursorHistory.getColumnIndex("en_definition")));
                    historyArrayList.add(h);
                } while (cursorHistory.moveToNext());
            }

            historyAdapter.notifyDataSetChanged();
        }

            if (historyAdapter.getItemCount() == 0) {
                emptyHistory.setVisibility(View.VISIBLE);
            } else {
                emptyHistory.setVisibility(View.GONE);
            }
        }

        private void showAlertDialog() {
            search.setQuery("", false);
            AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this, R.style.myDialogTheme);
            b.setTitle("Word not Found!");
            b.setMessage("Please search again!");

            String positiveText = getString(android.R.string.ok);
            b.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            String negativeText = getString(android.R.string.cancel);
            b.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    search.clearFocus();
                }
            });

            AlertDialog dialog = b.create();
            dialog.show();
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_exit) {
            System.exit(0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetch_history();
    }



}
