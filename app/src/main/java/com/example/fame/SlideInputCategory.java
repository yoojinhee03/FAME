package com.example.fame;

import android.provider.BaseColumns;

public class SlideInputCategory {
    public SlideInputCategory(){}

    public static class CategoryEntry implements BaseColumns {
        public static final String TABLE_NAME="SlideBasicCategory";
        public static final String COLUMN_NAME_WORDCOUNT="wordcount";
        public static final String COLUMN_NAME_REPEATCOUNT="repeatcount";
        public static final String COLUMN_NAME_CATEGORY="category";
        public static final String COLUMN_NAME_INPUTCOUNT="inputcount";
    }
}
