package com.example.reformed_swede.grammartrainer.Adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class PartOfSpeechAdapter extends ArrayAdapter<String> {
    public PartOfSpeechAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }
}
