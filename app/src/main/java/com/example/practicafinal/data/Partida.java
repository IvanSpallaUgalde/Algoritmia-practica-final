package com.example.practicafinal.data;

import android.content.res.Resources;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;

public class Partida {

    // Fields
    private final WordDictionary Diccionari;
    private final String ParaulaSolucio;

    private final int numLetrasMax;
    private final ArrayList<Pair<Integer, Word>> NoTrobades;
    private final HashSet<Pair<Integer, Word>> Trobades = new HashSet<>();
    private final HashSet<Word> Solucions = new HashSet<>();
    private final HashSet<Word> TrobadesBonus = new HashSet<>();

    public static final int CantParaules = 5;
    public static final int ObjectiuBonus = 5;
    public static final int MinimoLetras = 3;
    public static final int MaximoLetras = 7;


    // Constructor
    public Partida(Resources resources, int numLetrasMax) {
        if (MaximoLetras < numLetrasMax || numLetrasMax < MinimoLetras)
            throw new IllegalArgumentException("Numero de letras no valido");

        this.numLetrasMax = numLetrasMax;

        Diccionari = new DictionaryReader(resources).ReadValid();

        Word[] longitud = Diccionari.getLongitud(this.numLetrasMax);
        ParaulaSolucio = longitud[new Random().nextInt(longitud.length)].Raw;

        Word[][] solucionsPerLongitud = Diccionari.getSolucions(numLetrasMax, ParaulaSolucio);


        for (int i = 1; i <= numLetrasMax; i++) {
            Word[] solucio = solucionsPerLongitud[i - 1];
            Solucions.addAll(Arrays.asList(solucio));
        }


        // Agafam CantParaules
        NoTrobades = GenerarNoTrobades(solucionsPerLongitud);
    }

    private ArrayList<Pair<Integer, Word>> GenerarNoTrobades(Word[][] solucionsPerLongitud) {
        ArrayList<Pair<Integer, Word>> noTrobades = new ArrayList<>();
        Random r = new Random();
        for (int i = numLetrasMax; i > MinimoLetras; i--) {
            int index = r.nextInt(solucionsPerLongitud[i - 1].length);
            noTrobades.add(0, new Pair<>(0, solucionsPerLongitud[i - 1][index]));
        }


        Word[] limitInferior = solucionsPerLongitud[MinimoLetras - 1];// 3 - 1
        int maxAGenerar = Math.min(CantParaules, noTrobades.size() + limitInferior.length);

        while (noTrobades.size() < maxAGenerar) {
            int index = -1;
            do {
                index = r.nextInt(limitInferior.length);
            } while (noTrobades.contains(limitInferior[index]));

            noTrobades.add(0, new Pair<>(0, limitInferior[index]));
        }

        for (int i = 0; i < noTrobades.size(); i++) {
            noTrobades.set(i, new Pair<>(i, noTrobades.get(i).second));
        }

        return noTrobades;
    }

    // Getters i setters
    public int getNumeroLetras() {
        return numLetrasMax;
    }

    // Logica
    public void enviarParaula(String paraula) {

        Optional<Pair<Integer, Word>> candidat = NoTrobades.stream().filter(w -> w.second.Raw.equals(paraula)).findFirst();

        if (candidat.isPresent()) {
            Pair<Integer, Word> w = candidat.get();
            Trobades.add(w);
            NoTrobades.remove(w);
        } else {
            Solucions.stream().filter(w -> w.Raw.equals(paraula)).findFirst().ifPresent(TrobadesBonus::add);
        }
    }

    public Pair<Integer, Word>[] getTrobades() {
        //noinspection unchecked
        return (Pair<Integer, Word>[]) Trobades.toArray(new Pair[0]);
    }

    public Pair<Integer, Word>[] getNoTrobades() {
        //noinspection unchecked
        return (Pair<Integer, Word>[]) NoTrobades.toArray(new Pair[0]);
    }

    public Word[] getTrobadesBonus() {
        return TrobadesBonus.toArray(new Word[0]);
    }

    public char[] getLetrasRandom() {

        return randomize(ParaulaSolucio);
    }


    // Funcio de randomitzacio de Fisher-Yates per reordenar les lletres dels botons
    private static char[] randomize(String str) {
        // Creating a object for Random class
        Random r = new Random();

        char[] arr = str.toCharArray();

        // Get the size of the array
        int n = arr.length;

        // Start from the last element and swap one by one. We don't
        // need to run for the first element that's why i > 0
        for (int i = n - 1; i > 0; i--) {

            // Pick a random index from 0 to i
            int j = r.nextInt(i + 1);

            // Swap arr[i] with the element at random index
            char temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }

        return arr;
    }

}
