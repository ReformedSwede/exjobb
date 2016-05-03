package com.example.reformed_swede.grammartrainer.main;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.reformed_swede.grammartrainer.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class SessionAdapter extends BaseAdapter {
    Context context;
    List<String> natives = new ArrayList<>();
    List<String> foreigns = new ArrayList<>();

    private static LayoutInflater inflater=null;

    public SessionAdapter(StartActivity startActivity, LinkedHashMap<String, String> sessions) {
        context = startActivity;
        for(Map.Entry<String, String> e : sessions.entrySet()){
            natives.add(e.getKey());
            foreigns.add(e.getValue());
        }
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return natives.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView nativeLabel;
        TextView foreignLabel;
        Button launchBtn;
        Button deleteBtn;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.session_element, null);
        holder.nativeLabel = (TextView) rowView.findViewById(R.id.native_label);
        holder.foreignLabel = (TextView) rowView.findViewById(R.id.foreign_label);
        holder.launchBtn = (Button) rowView.findViewById(R.id.launch_button);
        holder.deleteBtn = (Button) rowView.findViewById(R.id.delete_button);

        holder.nativeLabel.setText(natives.get(position));
        holder.foreignLabel.setText(foreigns.get(position));
        holder.launchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("session list", "you launched a session!");
            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("session list", "you deleted a session!");
            }
        });

        return rowView;
    }

}