package com.example.practicafinal.data;

import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.practicafinal.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DictionaryReader {

    private final Resources resources;

    public DictionaryReader(Resources resources){
        this.resources = resources;
    }

    @Nullable
    private WordDictionary RawDictionary;

    private WordDictionary GetRawDictionary(){
        if(RawDictionary == null)
            RawDictionary = ReadRaw();
        return RawDictionary;
    }

    public WordDictionary ReadValid(){
        return GetRawDictionary();
    }

    private WordDictionary ReadRaw(){
        BufferedReader r = new BufferedReader(new InputStreamReader(resources.openRawResource(R.raw.paraules2)));

        WordDictionary dict = new WordDictionary();
        try {
            String line;
            while ((line = r.readLine()) != null) {
                String[] parts = line.split(";");
                Word w = new Word(parts[0], parts[1]);
                dict.addWord(w);
            }
            Log.d("Dictionary Reader",
                    "Dictionary Read Complete. Total entries: " + dict.getCount());
        }
        catch(Exception e)
        {
            Log.e("Dictionary Read Error", e.getMessage());
        }

        return dict;
    }
}