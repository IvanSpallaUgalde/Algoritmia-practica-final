package com.example.practicafinal.data;

public class Word implements Comparable<Word>{
    public String Accentuada;
    public String Raw;

    public Word(String accentuada, String raw) {
        Accentuada = accentuada;
        Raw = raw;
    }

    public int getLongitud() {
        return Raw.length();
    }

    @Override
    public int compareTo(Word o) {
        return Raw.compareTo(o.Raw);
    }
}
