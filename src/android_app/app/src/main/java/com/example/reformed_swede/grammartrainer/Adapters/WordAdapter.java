package com.example.reformed_swede.grammartrainer.Adapters;


import android.app.AlertDialog;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.reformed_swede.grammartrainer.grammar.GrammarContainer;

import java.util.ArrayList;
import java.util.List;

public class WordAdapter extends BaseAdapter{

    List<GrammarContainer.Word> words = new ArrayList<>();

    public WordAdapter(List<GrammarContainer.Word> words){
        this.words = words;
    }

    @Override
    public int getCount() {
        return words.size();
    }

    @Override
    public Object getItem(int position) {
        return words.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        TextView textView = new TextView(parent.getContext());

        final String shortText, longText;
        shortText = words.get(position).foreignInflections.get(0);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.get(position).foreignInflections.size(); i++) {
            builder.append(words.get(position).nativeInflections.get(i));
            builder.append(" - ");
            builder.append(words.get(position).foreignInflections.get(i));
            builder.append('\n');
        }
        longText = builder.toString();
        textView.setText(shortText);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(parent.getContext())
                        .setTitle("Information")
                        .setMessage(longText)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            }
        });

        return textView;
    }
}
