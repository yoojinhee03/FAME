package com.example.fame;

import android.provider.BaseColumns;

public class SlideCategory {



//
//    public int wordcount;
//    public int repeatcount;
//    public String category;
//    public String level;
//
//    public void setCategory(String category) {
//        this.category = category;
//    }
//
//    public void setLevel(String level) {
//        this.level = level;
//    }
//
//    public void setRepeatcount(int repeatcount) {
//        this.repeatcount = repeatcount;
//    }
//
//    public void setWordcount(int wordcount) {
//        this.wordcount = wordcount;
//    }
        public static class CategoryEntry implements BaseColumns{
        public static final String TABLE_NAME="SlideCategory";
        public static final String COLUMN_NAME_ID="_id";
        public static final String COLUMN_NAME_WORDCOUNT="wordcount";
        public static final String COLUMN_NAME_REPEATCOUNT="repeatcount";
        public static final String COLUMN_NAME_LEVEL="level";
        public static final String COLUMN_NAME_CATEGORY="category";
        public static final String COLUMN_NAME_INPUTCOUNT="inputcount";
}

}

