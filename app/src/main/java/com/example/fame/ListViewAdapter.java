package com.example.fame;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class ListViewAdapter extends CursorAdapter {

    String table_name;

    public ListViewAdapter(Context context, Cursor c,String table) {
        super(context, c, false);
        this.table_name=table;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        if (table_name.equals("slide")) {
            View convertView=LayoutInflater.from(context)
                    .inflate(R.layout.slide_list_item, parent, false);
            ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
            layoutParams.height = 220;
            return convertView;
        } else if (table_name.equals("alarm")) {
            View convertView= LayoutInflater.from(context)
                    .inflate(R.layout.alarm_list_item, parent, false);
            ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
            layoutParams.height = 220;
            return convertView;
        }
        return LayoutInflater.from(context)
                .inflate(android.R.layout.simple_list_item_1, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int id= Integer.parseInt(cursor.getString(0));
        if(table_name.equals("alarm")){
            ((EffortmodeActivity)EffortmodeActivity.mContext).alarmsetlayout(view);
        }
        else if(table_name.equals("slide")) {
            ((EffortmodeActivity) EffortmodeActivity.mContext).slidesetlayout(view);
        }
    }
}