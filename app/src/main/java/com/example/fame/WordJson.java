package com.example.fame;

import org.json.JSONException;
import org.json.JSONObject;

public class WordJson {

    public static String toJSon(Word word) {
        try {

            JSONObject jsonObj = new JSONObject();

            jsonObj.put("id", word.getId());
            jsonObj.put("word", word.getWord());
            jsonObj.put("mean", word.getMean());
            return jsonObj.toString();

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
