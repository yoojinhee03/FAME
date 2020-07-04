package com.example.fame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class WordListViewAdapter extends BaseAdapter{

    private ArrayList<Word> data;
    private int page;
    LayoutInflater mLayoutInflater = null;

    public WordListViewAdapter( Context content,ArrayList<Word> data,int page){
        this.data = data;
        this.page=page;
        mLayoutInflater = LayoutInflater.from(content);
    }

    @Override
    public int getCount() {
        return 5;
    }//한페이지당 단어 5개

    @Override
    public Object getItem(int position) {
        //position %= 5;//0~4 포지션
        //position=5*(page-1)+position;
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        //position=5*(page-1)+position;
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //View view = LayoutInflater.from(context).inflate(R.layout.word, null);
        position=5*(page-1)+position;

        View view = mLayoutInflater.inflate(R.layout.word, null);

        final int finalPosition = position;
        TextView word = view.findViewById(R.id.endText);
        TextView mean = view.findViewById(R.id.startText);
        word.setText(data.get(position).getWord());
        mean.setText(data.get(position).getMean());
        return view;
    }
}
