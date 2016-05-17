package com.example.reformed_swede.grammartrainer.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.reformed_swede.grammartrainer.Adapters.PartOfSpeechAdapter;
import com.example.reformed_swede.grammartrainer.Adapters.WordAdapter;
import com.example.reformed_swede.grammartrainer.R;
import com.example.reformed_swede.grammartrainer.grammar.GrammarContainer;
import com.example.reformed_swede.grammartrainer.grammar.GrammarManager;
import com.example.reformed_swede.grammartrainer.grammar.Session;

import java.util.ArrayList;
import java.util.List;

public class WordListActivity extends AppCompatActivity {

    GrammarManager grammar;
    ListView wordList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        Intent intent = getIntent();
        grammar = new GrammarManager(new Session(intent.getStringExtra("native"),
                intent.getStringExtra("foreign"),
                intent.getStringExtra("title")), this);

        wordList = (ListView) findViewById(R.id.wordList);
        Spinner spinner = (Spinner) findViewById(R.id.posChooser);
        PartOfSpeechAdapter adapter = new PartOfSpeechAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,grammar.getPartsOfSpeech());
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerItemSelected((int) id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void spinnerItemSelected(int itemId){
        wordList.setAdapter(new WordAdapter(grammar.getAllWords(grammar.getPartsOfSpeech().get(itemId))));
    }
}
