package com.example.reformed_swede.grammartrainer.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.reformed_swede.grammartrainer.R;
import com.example.reformed_swede.grammartrainer.grammar.GrammarManager;
import com.example.reformed_swede.grammartrainer.grammar.Session;

public class PracticeActivity extends AppCompatActivity {

    private GrammarManager grammar;
    private GrammarContainer.Word currentWord, prevWord = null; //Used in random word generation
    private int inflectionForm; //Used in random word generation
    private boolean translateToNative = true; //Determines if the user is to translate the given word to the native lang

    EditText inputFld;
    TextView practiceWordLbl;
    TextView infoLbl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get intent extras
        Intent intent = getIntent();
        grammar = new GrammarManager(new Session(intent.getStringExtra("native"),
                intent.getStringExtra("foreign"),
                intent.getStringExtra("title")), this);

        //Set ui components
        inputFld = (EditText)findViewById(R.id.inputField);
        practiceWordLbl = (TextView)findViewById(R.id.practiceWordLabel);
        infoLbl = (TextView)findViewById(R.id.infoLabel);

        //Init components
        inputFld.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)){
                    submit();
                }
                return false;
            }
        });
        inputFld.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    submit();
                    return true;
                }
                return false;
            }
        });

        infoLbl.setText(String.valueOf(grammar.getNrOfWords()));

        //Start!
        setNextWord();
    }

    /**
     * Displays a new random word to the user
     */
    private void setNextWord(){
        inputFld.setText("");

        //Generate a word, distinct from the previous one
        do{
            currentWord = grammar.getRandomWord();
        }while(currentWord.equals(prevWord) && grammar.getNrOfWords() > 1);
        prevWord = currentWord;
        inflectionForm = grammar.getRandomInflectionId(currentWord.partOfSpeech);

        //Update the view
        practiceWordLbl.setText(
                translateToNative ? currentWord.foreignInflections.get(inflectionForm)
                        : currentWord.nativeInflections.get(inflectionForm));
    }

    /**
     * Checks whether the user's answer is correct
     */
    public void submit(){
        //Check if answer was correct
        boolean correct;
        String answer = inputFld.getText().toString().trim();
        correct = translateToNative ? currentWord.nativeInflections.get(inflectionForm).equals(answer)
                : currentWord.foreignInflections.get(inflectionForm).equals(answer);

        //Handle both cases
        if(correct){
            infoLbl.setText("Correct! \"" + practiceWordLbl.getText() +
                    "\" translates to \"" + inputFld.getText() + "\".");
        }else {
            infoLbl.setText("Incorrect! \"" + practiceWordLbl.getText() +
                    "\" does not translate to  \"" + inputFld.getText() + "\".\n" + "The correct answer was \"" +
                    (translateToNative ? currentWord.nativeInflections.get(inflectionForm)
                    : currentWord.foreignInflections.get(inflectionForm)) + "\".");
        }
            setNextWord();
    }
}
