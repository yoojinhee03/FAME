package com.example.fame;

public class Word {
    public int id;
    public String word;
    public String mean;


    public void setId(int id) {
        this.id = id;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    public int getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public String getMean() {
        return mean;
    }


    //    public static class WordEntry implements BaseColumns{
//        public static final String TABLE_NAME="BeginningLevel";
//        public static final String COLUMN_NAME_ID="id";
//        public static final String COLUMN_NAME_WORD="word";
//        public static final String COLUMN_NAME_MEAN="mean";
//    }
}
