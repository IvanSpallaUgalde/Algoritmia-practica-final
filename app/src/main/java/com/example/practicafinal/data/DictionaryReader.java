package com.example.practicafinal.data;

import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.practicafinal.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class DictionaryReader {

    private final Resources resources;

    public DictionaryReader(Resources resources) {
        this.resources = resources;
    }

    @Nullable
    private WordDictionary RawDictionary;

    private WordDictionary GetRawDictionary() {
        if (RawDictionary == null)
            RawDictionary = ReadRaw();
        return RawDictionary;
    }

    public WordDictionary ReadValid() {
        return GetRawDictionary();
    }

    private WordDictionary ReadRaw() {
        BufferedReader r = new BufferedReader(new InputStreamReader(resources.openRawResource(R.raw.paraules2)));

        ArrayList<Word> words = new ArrayList<>();
        try {
            String line;
            while ((line = r.readLine()) != null) {
                String[] parts = line.split(";");
                Word w = new Word(parts[0], parts[1]);
                words.add(w);
            }
            Log.d("Dictionary Reader",
                    "Dictionary Read Complete. Total entries: " + words.size());
        } catch (Exception e) {
            Log.e("Dictionary Read Error", e.getMessage());
        }

        return new WordDictionary(words.stream().filter(x -> NomesLletres(x.Raw)).toArray(Word[]::new));
    }


    public static boolean NomesLletres(String s) {
        return s.chars().allMatch(Character::isLetter);
    }
}