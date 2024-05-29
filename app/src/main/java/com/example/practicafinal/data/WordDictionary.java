package com.example.practicafinal.data;

import java.util.ArrayList;

public class WordDictionary {
    public ArrayList<Word> Words;

    public WordDictionary() {
        Words = new ArrayList<Word>();
    }

    public void addWord(Word word) {
        Words.add(word);
        //Log.d("Dictionary Line", MessageFormat.format("Added word {0} with raw {1}", word.Accentuada, word.Raw));
    }

    public int getCount() {
        return Words.size();
    }
}