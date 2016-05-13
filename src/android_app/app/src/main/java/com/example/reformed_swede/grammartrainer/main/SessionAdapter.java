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
import com.example.reformed_swede.grammartrainer.grammar.Session;

import java.util.ArrayList;
import java.util.List;

class SessionAdapter extends BaseAdapter {
    StartActivity startActivity;
    List<Session> sessions = new ArrayList<>();

    private static LayoutInflater inflater=null;

    public SessionAdapter(StartActivity startActivity, List<Session> sessions) {
        this.startActivity = startActivity;
        this.sessions = sessions;
        inflater = ( LayoutInflater ) this.startActivity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return sessions.size();
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
        TextView titleLabel;
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
        holder.titleLabel = (TextView) rowView.findViewById(R.id.title_label);
        holder.nativeLabel = (TextView) rowView.findViewById(R.id.native_label);
        holder.foreignLabel = (TextView) rowView.findViewById(R.id.foreign_label);
        holder.launchBtn = (Button) rowView.findViewById(R.id.launch_button);
        holder.deleteBtn = (Button) rowView.findViewById(R.id.delete_button);

        holder.titleLabel.setText(sessions.get(position).getTitle());
        holder.nativeLabel.setText(sessions.get(position).getNativeCode());
        holder.foreignLabel.setText(sessions.get(position).getForeignCode());
        holder.launchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity.launch(sessions.get(position));
            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {startActivity.delete();
            }
        });

        return rowView;
    }

}