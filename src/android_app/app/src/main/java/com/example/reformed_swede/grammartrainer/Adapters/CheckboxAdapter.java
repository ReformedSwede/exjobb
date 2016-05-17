package com.example.reformed_swede.grammartrainer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.example.reformed_swede.grammartrainer.R;
import com.example.reformed_swede.grammartrainer.main.PracticeActivity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class CheckboxAdapter extends BaseAdapter{

    LinkedHashMap<String, Boolean> items = new LinkedHashMap<>();
    PracticeActivity activity;
    private static LayoutInflater inflater = null;

    public CheckboxAdapter(PracticeActivity activity, List<String> items){
        this.activity = activity;
        for(String item : items)
            this.items.put(item, true);
        inflater = ( LayoutInflater ) this.activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItems(List<String> newItems){
        for(String item : newItems)
            items.put(item, true);
        notifyDataSetChanged();
    }

    public void setCheckedState(String itemName, boolean state){
        items.put(itemName, state);
        notifyDataSetChanged();
    }

    public boolean getCheckedState(String itemName){
        return items.get(itemName);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(new ArrayList<>(items.keySet()).get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        CheckBox box;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.checkbox_element, null);
        holder.box = (CheckBox) rowView.findViewById(R.id.box);
        holder.box.setText(new ArrayList<>(items.keySet()).get(position));
        holder.box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox box = (CheckBox)v;
                boolean isChecked = box.isChecked();

                items.put(box.getText().toString(), isChecked);
                activity.checkboxChecked(box.getText().toString(), isChecked);
                notifyDataSetChanged();
            }
        });
        holder.box.setChecked(items.get(holder.box.getText().toString()));
        return rowView;
    }
}
