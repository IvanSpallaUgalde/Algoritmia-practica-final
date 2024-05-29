package com.example.practicafinal.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordDictionary {
    private final Word[] Words;
    private final Word[][] Longituds;

    public WordDictionary(Word[] words) {
        Words = words;

        Longituds = new Word[7][];
        for (int i = 0; i < 7; i++) {
            int finalI = i;
            Longituds[i] = Arrays.stream(getValides()).filter(word -> word.getLongitud() == finalI + 1).toArray(Word[]::new);
        }
    }

    public int getCount() {
        return Words.length;
    }

    public Word[] getValides() {
        return Words;
    }

    public Word[] getLongitud(int longitud) {
        return Longituds[longitud - 1];
    }

    public Word[][] getLongituds() {
        return Longituds;
    }

    public Word[][] getSolucions(int numLetrasMax, String lletres) {
        Word[][] longitudes = getLongituds();

        Word[][] result = new Word[numLetrasMax][];
        for (int i = 1; i <= numLetrasMax; i++) {
            Word[] r = Arrays.stream(longitudes[i - 1]).filter(word -> esParaulaSolucio(lletres, word.Raw)).toArray(Word[]::new);
            result[i - 1] = r;
        }
        return result;
    }


    // Comprova si es pot crear `p2` a partir de les lletres de `p1`.
    // Les repeticions son estrictes, es a dir, calen n repeticions d'una lletra en p1 per
    // poder crear p2 que conte dita lletra n vegades.
    public static boolean esParaulaSolucio(String p1, String p2) {
        if (p1.length() < p2.length())
            return false;

        int[] contador = new int[26];
        for (char c : p1.toCharArray()) {
            contador[c - 'a']++;
        }

        for (char c : p2.toCharArray()) {
            if (contador[c - 'a'] == 0)
                return false;
            contador[c - 'a']--;
        }

        return true;
    }
}