package com.example.reformed_swede.grammartrainer.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.reformed_swede.grammartrainer.R;
import com.example.reformed_swede.grammartrainer.grammar.GrammarManager;
import com.example.reformed_swede.grammartrainer.grammar.Session;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StartActivity extends AppCompatActivity {

    SyncThread receiver = new SyncThread();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        refreshSessionList();
    }

    private void refreshSessionList(){
        ListView sessionList = (ListView)findViewById(R.id.session_list);
        SessionAdapter adapter = new SessionAdapter(this, GrammarManager.getSessions(getApplicationContext()));
        sessionList.setAdapter(adapter);
    }

    public void handleReceivedData(Object obj){

        //Parse received data
        Object[] stuff = (Object[])obj;
        String[] meta = ((String)stuff[0]).split(",");
        HashMap<String, List<String>> partsOfSpeech = (HashMap<String, List<String>>)stuff[1];
        LinkedHashMap<String, List<List<String>>> natives = (LinkedHashMap<String, List<List<String>>>)stuff[2];
        LinkedHashMap<String, List<List<String>>> foreigns = (LinkedHashMap<String, List<List<String>>>)stuff[3];

        GrammarContainer gc = new GrammarContainer(new Session(meta[1], meta[2], meta[0]));
        for(Map.Entry<String, List<String>> e : partsOfSpeech.entrySet())
            gc.addPartOfSpeech(e.getKey(), e.getValue());

        for(String pos : natives.keySet()){
            for (int i = 0; i < natives.get(pos).size(); i++) {
                gc.addWord(pos, natives.get(pos).get(i), foreigns.get(pos).get(i));
            }
        }

        //TODO Prompt user
        //TODO Check if file exists. Ask user overwrite or not

        String file = getFilesDir().getAbsoluteFile() + File.separator
                + "grammar" + File.separator + gc.getSession().getTitle();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(gc);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        refreshSessionList();
    }

    public void launch(Session session){
        Intent intent = new Intent(this, PracticeActivity.class);
        intent.putExtra("title", session.getTitle());
        intent.putExtra("native", session.getNativeCode());
        intent.putExtra("foreign", session.getForeignCode());
        startActivity(intent);
    }

    public void delete(){

    }

    /**
     * Dispatch onResume() to fragments.
     */
    @Override
    protected void onResume() {
        super.onResume();
        receiver.startListening(this);
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        receiver.stopListening();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
