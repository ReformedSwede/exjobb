package com.example.reformed_swede.grammartrainer.main;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.reformed_swede.grammartrainer.Adapters.CheckboxAdapter;
import com.example.reformed_swede.grammartrainer.R;
import com.example.reformed_swede.grammartrainer.grammar.GrammarContainer;
import com.example.reformed_swede.grammartrainer.grammar.GrammarManager;
import com.example.reformed_swede.grammartrainer.grammar.Session;

import java.util.ArrayList;
import java.util.List;

public class PracticeActivity extends AppCompatActivity {

    private GrammarManager grammar;
    private GrammarContainer.Word currentWord, prevWord = null; //Used in random word generation
    private int inflectionId; //Used in random word generation
    private boolean translateToNative = true; //Determines if the user is to translate the given word to the native lang

    ToggleButton toggle;
    EditText inputFld;
    TextView practiceWordLbl;
    TextView infoLbl;
    RelativeLayout container;
    ListView posCheckboxes;
    ListView inflCheckboxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //Get intent extras
        Intent intent = getIntent();
        grammar = new GrammarManager(new Session(intent.getStringExtra("native"),
                intent.getStringExtra("foreign"),
                intent.getStringExtra("title")), this);

        //Set ui components
        toggle = (ToggleButton)findViewById(R.id.langToggle);
        container = (RelativeLayout)findViewById(R.id.container);
        inputFld = (EditText)findViewById(R.id.inputField);
        practiceWordLbl = (TextView)findViewById(R.id.practiceWordLabel);
        infoLbl = (TextView)findViewById(R.id.infoLabel);
        posCheckboxes = (ListView)findViewById(R.id.posCheckboxes);
        inflCheckboxes = (ListView)findViewById(R.id.inflCheckboxes);

        //Init components
        inputFld.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
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
        posCheckboxes.setAdapter(new CheckboxAdapter(this, grammar.getPartsOfSpeech()));
        inflCheckboxes.setAdapter(new CheckboxAdapter(this, new ArrayList<String>()));
        for(String pos : grammar.getPartsOfSpeech())
            ((CheckboxAdapter)inflCheckboxes.getAdapter()).addItems(grammar.getInflections(pos));

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
            currentWord = grammar.getRandomWord(getUsablePartsOfSpeech());
        }while(currentWord.equals(prevWord) && grammar.getNrOfWords() > 1);
        prevWord = currentWord;
        inflectionId = grammar.getRandomInflectionId(currentWord.partOfSpeech, getUsableInflections(currentWord.partOfSpeech));

        //Update the view
        practiceWordLbl.setText(
                translateToNative ? currentWord.foreignInflections.get(inflectionId)
                        : currentWord.nativeInflections.get(inflectionId));
    }

    /**
     * Checks whether the user's answer is correct
     */
    public void submit(){
        //Check if answer was correct
        boolean correct;
        String answer = inputFld.getText().toString().trim();
        correct = checkAnswer(answer);

        //Handle both cases
        if(correct){
            inputFld.setBackgroundColor(Color.rgb(100, 200, 100));
            infoLbl.setText(String.format("Correct! \"%s\" translates to \"%s\".", practiceWordLbl.getText(), inputFld.getText()));
        }else {
            inputFld.setBackgroundColor(Color.rgb(200, 100, 100));
            infoLbl.setText(String.format("Incorrect! \"%s\" does not translate to  \"%s\".\nThe correct answer was \"%s\".",
                    practiceWordLbl.getText(),
                    inputFld.getText(), translateToNative ? currentWord.nativeInflections.get(inflectionId)
                    : currentWord.foreignInflections.get(inflectionId)));
        }
        setNextWord();
    }

    private boolean checkAnswer(String answer) {
        //Iterate over all inflections. If one is equal to the one being shown, check if it equals answer
        if(translateToNative){
            if(currentWord.nativeInflections.get(inflectionId).equals(answer))
                return  true;

            for(int i = 0; i < currentWord.foreignInflections.size(); i ++){
                if(currentWord.foreignInflections.get(inflectionId).equals(currentWord.foreignInflections.get(i))
                        && currentWord.nativeInflections.get(i).equals(answer))
                    return true;
            }
        }else{
            if(currentWord.foreignInflections.get(inflectionId).equals(answer))
                return  true;

            for(int i = 0; i < currentWord.nativeInflections.size(); i ++){
                if(currentWord.nativeInflections.get(inflectionId).equals(currentWord.nativeInflections.get(i))
                        && currentWord.foreignInflections.get(i).equals(answer))
                    return true;
            }
        }

        return false;
    }

    public void toggleLang(View view){
        translateToNative = !translateToNative;
        setNextWord();
    }

    private List<String> getUsablePartsOfSpeech(){
        List<String> list = new ArrayList<>();
        for(String pos : grammar.getPartsOfSpeech())
            if(((CheckboxAdapter)posCheckboxes.getAdapter()).getCheckedState(pos))
                list.add(pos);
        return list;
    }

    private List<String> getUsableInflections(String partOfSpeech){
        List<String> list = new ArrayList<>();
        for(String inflection : grammar.getInflections(partOfSpeech))
            if(((CheckboxAdapter)inflCheckboxes.getAdapter()).getCheckedState(inflection))
                list.add(inflection);
        return list;
    }

    public void checkboxChecked(String boxName, boolean isChecked){
        //Determine if checkbox was pos or inflection
        boolean isPos = false;
        for(String pos : grammar.getPartsOfSpeech())
            if(pos.equals(boxName)){
                isPos = true;
                break;
            }
        if(isPos){
            if(!isChecked) {
                //Check if last deselected
                int nrSelected = 0;
                for (String pos : grammar.getPartsOfSpeech())
                    if (((CheckboxAdapter) posCheckboxes.getAdapter()).getCheckedState(pos))
                        nrSelected++;
                //If so, fix it
                if (nrSelected == 0) {
                    ((CheckboxAdapter) posCheckboxes.getAdapter()).setCheckedState(boxName, true);
                    return;
                }
            }

            //Change inflection boxes
            for(String inflection : grammar.getInflections(boxName))
                ((CheckboxAdapter)inflCheckboxes.getAdapter()).setCheckedState(inflection, isChecked);

            setNextWord();

        }else{
            if(!isChecked) {
                //Find out what pos it belongs to
                String pos = null;
                for (String s : grammar.getPartsOfSpeech())
                    for (String i : grammar.getInflections(s))
                        if (i.equals(boxName)) {
                            pos = s;
                            break;
                        }

                    //Check if this was last to be deselected
                int nrSelected = 0;
                for (String infl : grammar.getInflections(pos))
                    if (((CheckboxAdapter) inflCheckboxes.getAdapter()).getCheckedState(infl))
                        nrSelected++;
                //If so, fix it
                System.out.println(nrSelected);
                if(nrSelected == 0) {
                    ((CheckboxAdapter) inflCheckboxes.getAdapter()).setCheckedState(boxName, true);
                    return;
                }

                setNextWord();
            }
            //TODO: If a pos is deselected and one of its inflections is selected, select pos again
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_viewList) {
            Intent intent = new Intent(this, WordListActivity.class);
            Session session = grammar.getCurrentSession();
            intent.putExtra("title", session.getTitle());
            intent.putExtra("native", session.getNativeCode());
            intent.putExtra("foreign", session.getForeignCode());
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


}
