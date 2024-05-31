package com.example.practicafinal.data;

import android.content.res.Resources;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.TreeSet;

public class Partida {

    private final String ParaulaSolucio;

    private final int numLetrasMax;
    private final ArrayList<Pair<Integer, Word>> NoTrobades;
    private final HashSet<Pair<Integer, Word>> Trobades = new HashSet<>();
    private final HashSet<Word> Solucions = new HashSet<>();
    private final TreeSet<Word> TrobadesBonus = new TreeSet<>();
    private final TreeSet<Word> AllTrobades = new TreeSet<>();

    public static final int CantParaules = 5;
    public static final int ObjectiuBonus = 2;
    public static final int MinimoLetras = 3;
    public static final int MaximoLetras = 7;
    private final ArrayList<Integer> redeemedTips = new ArrayList<>();
    private Runnable BonusAction = () -> {
    };
    private Runnable RepeatedBonusAction = () -> {
    };
    private PairAction TrobatAction = a -> {
    };
    private PairAction RepeatedTrobatAction = a -> {
    };

    public interface PairAction {
        void apply(Pair<Integer, Word> pair);
    }

    public void setActions(Runnable bonusAction, Runnable repeatedBonusAction, PairAction trobatAction, PairAction repeatedTrobatAction) {
        BonusAction = bonusAction;
        RepeatedBonusAction = repeatedBonusAction;
        TrobatAction = trobatAction;
        RepeatedTrobatAction = repeatedTrobatAction;
    }

    // Constructor
    public Partida(Resources resources, int numLetrasMax) {
        if (MaximoLetras < numLetrasMax || numLetrasMax < MinimoLetras)
            throw new IllegalArgumentException("Numero de letras no valido");

        this.numLetrasMax = numLetrasMax;

        // Fields
        WordDictionary diccionari = new DictionaryReader(resources).ReadValid();

        Word[] longitud = diccionari.getLongitud(this.numLetrasMax);
        ParaulaSolucio = longitud[new Random().nextInt(longitud.length)].Raw;

        Word[][] solucionsPerLongitud = diccionari.getSolucions(numLetrasMax, ParaulaSolucio);


        for (int i = 1; i <= numLetrasMax; i++) {
            Word[] solucio = solucionsPerLongitud[i - 1];
            Solucions.addAll(Arrays.asList(solucio));
        }

        // Agafam CantParaules
        NoTrobades = GenerarNoTrobades(solucionsPerLongitud);

        // Debug in Logcat
        for (Word word : Solucions) {
            System.out.println("SOLUCION: " + word.Accentuada);
        }
    }

    private ArrayList<Pair<Integer, Word>> GenerarNoTrobades(Word[][] solucionsPerLongitud) {
        ArrayList<Pair<Integer, Word>> noTrobades = new ArrayList<>();
        Random r = new Random();
        for (int i = numLetrasMax; i > MinimoLetras; i--) {
            int length = solucionsPerLongitud[i - 1].length;
            if (length == 0)
                continue;
            int index = r.nextInt(length);
            noTrobades.add(0, new Pair<>(0, solucionsPerLongitud[i - 1][index]));
        }


        Word[] limitInferior = solucionsPerLongitud[MinimoLetras - 1];// 3 - 1
        int maxAGenerar = Math.min(CantParaules, noTrobades.size() + limitInferior.length);

        while (noTrobades.size() < maxAGenerar) {
            int index;
            do {
                index = r.nextInt(limitInferior.length);
                boolean b = true;
                for (Pair<Integer, Word> w : noTrobades) {
                    if (w.second.equals(limitInferior[index])) {
                        b = false;
                        break;
                    }
                }
                if (b)
                    break;
            } while (true);

            noTrobades.add(0, new Pair<>(0, limitInferior[index]));
        }

        for (int i = 0; i < noTrobades.size(); i++) {
            noTrobades.set(i, new Pair<>(i, noTrobades.get(i).second));
        }

        return noTrobades;
    }

    // Logica
    public boolean enviarParaula(String paraula) {

        boolean result = false;

        HashSet<Pair<Integer, Word>> aux = new HashSet<>();

        do {
            Optional<Pair<Integer, Word>> candidat = NoTrobades.stream().filter(w -> w.second.Raw.equals(paraula)).findFirst();


            if (candidat.isPresent()) {
                Pair<Integer, Word> w = candidat.get();
                AllTrobades.add(w.second); // Afegeix la paraula a la llista de totes les paraules trobades
                Trobades.add(w);
                NoTrobades.remove(w);
                aux.add(w);
                TrobatAction.apply(w);

                result = true;
                continue;
            } else {
                Optional<Pair<Integer, Word>> jaTrobada = Trobades.stream().filter(w -> !aux.contains(w) && w.second.Raw.equals(paraula)).findFirst();

                if (jaTrobada.isPresent()) {
                    RepeatedTrobatAction.apply(jaTrobada.get());
                    result = true;
                } else {
                    Optional<Word> match = Solucions.stream().filter(w -> aux.stream().map(p -> p.second).noneMatch(w2 -> w2.Raw.equals(paraula)) && w.Raw.equals(paraula)).findFirst();
                    if (match.isPresent()) {
                        if (TrobadesBonus.contains(match.get())) {
                            RepeatedBonusAction.run();
                        } else {
                            TrobadesBonus.add(match.get());
                            AllTrobades.add(match.get()); // Afegeix la paraula a la llista de totes les paraules trobades
                            BonusAction.run();
                        }

                        result = true;
                    }
                }
                break;

            }
        } while (true);

        return result;
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

    public String getAllTrobades() {
        return String.join(", ", AllTrobades.stream().map(w -> w.Accentuada).toArray(String[]::new));
    }

    public char[] getLetrasRandom() {

        return randomize(ParaulaSolucio);
    }

    public int getTotalSoluciones() {
        return Solucions.size();
    }

    public int getTotalTrobades() {
        return AllTrobades.size();
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

    public Optional<Pair<Integer, Word>> demanarPista() {

        int bcount = getTrobadesBonus().length;

        if (bcount < ObjectiuBonus)
            return Optional.empty();

        int pcount = redeemedTips.size();

        if (bcount % ObjectiuBonus - pcount >= 0) {
            Random r = new Random();


            Pair<Integer, Word> pair;
            do {
                pair = NoTrobades.get(r.nextInt(NoTrobades.size()));
            }
            while (redeemedTips.contains(pair.first));

            redeemedTips.add(pair.first);
            return Optional.of(pair);
        } else {
            return Optional.empty();
        }
    }

    public int getPistesDonadesCount() {
        return redeemedTips.size();
    }

    public void addBonusListener(Object o) {
    }
}
