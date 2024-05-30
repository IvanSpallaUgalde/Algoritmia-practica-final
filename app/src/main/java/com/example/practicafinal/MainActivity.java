package com.example.practicafinal;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.practicafinal.data.Partida;
import com.example.practicafinal.data.Word;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {


    //region Variables
    private ConstraintLayout main;
    private final int colour = Color.rgb(255, 100, 30);

    //region Llistes
    // Llista de botons del cercle
    private Button[] btnList;
    private int[] visibleBtns = {0, 1, 2, 3, 4, 5, 6};

    // Llista de guidelines de la seccio de paraules
    private Guideline[] wordGuides;
    // Llista de arrays de TextViews per a les paraules ocultes
    // Crear las filas de TextViews para las palabras ocultas para testear
    private TextView[][] hiddenWords;
    //endregion

    private TextView TVpalabra;
    private Button bonusBtn;
    private TextView progressText;

    //endregion


    private com.example.practicafinal.data.Partida Partida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // region View initialization
        main = findViewById(R.id.main);

        wordGuides = new Guideline[]{
                findViewById(R.id.topWordGuides),
                findViewById(R.id.firstGuide),
                findViewById(R.id.secondGuide),
                findViewById(R.id.thirdGuide),
                findViewById(R.id.fourthGuide),
                findViewById(R.id.botWordsGuide)
        };


        btnList = new Button[]{
                findViewById(R.id.letra1),
                findViewById(R.id.letra2),
                findViewById(R.id.letra3),
                findViewById(R.id.letra4),
                findViewById(R.id.letra5),
                findViewById(R.id.letra6),
                findViewById(R.id.letra7)};

        TVpalabra = findViewById(R.id.TVpalabra);
        bonusBtn = findViewById(R.id.bonusBTN);
        progressText = findViewById(R.id.progressText);

        //endregion

        startNewGame();

        mostraMissatge("Benvingut al ZenWord!");

    }

    public void startNewGame() {

        Partida = new Partida(getResources(), 7);

        //setColors();

        mezclarBotones();

        clear();

        hiddenWords = new TextView[Partida.getNoTrobades().length][];

        // Ordre revertit per que surtin aixi com demana la practica
        for (int i = 0; i < Partida.getNoTrobades().length; i++) {
            hiddenWords[i] = crearFilaTextViews(i + 1, Partida.getNoTrobades()[i].second.getLongitud());
        }

        updateUi();
    }


    public void setVisibilityLetra(int mode, Button btn) {
        btn.setVisibility(mode);
    }

    public void sendBTN(View view) {
        String p = TVpalabra.getText().toString();
        Partida.enviarParaula(p);
        updateUi();
    }

    // Actualitza les paraules trobades/no trobades, els bonus i la progresio
    @SuppressLint("SetTextI18n")
    private void updateUi() {
        clear();

        // Paraules trobades/no trobades
        Pair<Integer, Word>[] Trobades = Partida.getTrobades();
        for (Pair<Integer, Word> p : Trobades) {
            mostraParaula(p);
        }


        // Bonus
        int saldo = Partida.getTrobadesBonus().length - Partida.getPistesDonadesCount() * com.example.practicafinal.data.Partida.ObjectiuBonus;
        bonusBtn.setText(String.valueOf(saldo));

        if (saldo >= com.example.practicafinal.data.Partida.ObjectiuBonus)
            bonusBtn.setTextColor(Color.rgb(61, 145, 84));
        else
            bonusBtn.setTextColor(Color.BLACK);


        // Progresio
        String s = "Has encertat " + Partida.getTrobades().length + " de " + (Partida.getTrobades().length + Partida.getNoTrobades().length) + " possibles";
        int bcount = Partida.getTrobadesBonus().length;
        if (bcount > 0){
            s += " (+ " + bcount + " paraules bonus)";
        }
        progressText.setText(s);
    }

    public void clearBTN(View view) {
        // Elimina el texto en el Text View palabra y devuelve las letras al circulo
        clear();
    }

    @SuppressLint("SetTextI18n")
    public void setLletra(View view) {
        // Obtener el boton pulsado
        Button btn = (Button) view;

        // Obtener texto del boton pulsado
        String letra = btn.getText().toString().toUpperCase();

        // Añadir el texto
        TVpalabra.setText(TVpalabra.getText().toString() + letra);

        // Hacer la letra no visible
        setVisibilityLetra(View.GONE, btn);
    }

    public void randomBTN(View view) {
        clear(); // TODO: check si aixo s'ha de fer

        mezclarBotones();
    }

    private void mezclarBotones() {
        char[] aux = Partida.getLetrasRandom();
        visibleBtns = new int[aux.length];

        Random random = new Random();
        int i = 0;
        while (i < aux.length - 1) {
            int num = random.nextInt(btnList.length);
            if (IntStream.of(visibleBtns).noneMatch(x -> x == num)) {
                visibleBtns[i] = num;
                i++;
            }
        }

        for (int j = 0, k = 0; j < btnList.length; j++) {
            int finalJ = j;
            if (IntStream.of(visibleBtns).anyMatch(x -> x == finalJ) && k < aux.length) {
                setVisibilityLetra(View.VISIBLE, btnList[j]);
                btnList[j].setText(String.valueOf(aux[k++]));
            } else {
                setVisibilityLetra(View.GONE, btnList[j]);
            }
        }
    }

    public void clear() {
        TVpalabra.setText("");

        Arrays.stream(visibleBtns).forEach(x -> setVisibilityLetra(View.VISIBLE, btnList[x]));
    }

    // Quan s'implementi la logica del programa informara de quantes paraules s'han encertat i llistarles
    public void bonusBTN(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        Word[] bonus = Partida.getTrobadesBonus();

        // Titol del AlertDialog amb la progresio de paraules encertades
        builder.setTitle("Paraules bonus trobades:");

        // Text del AlertDialog que mes endevant es cambiara per una llista ordenada alfabetiment de les paraules
        builder.setMessage(String.join("\n", Arrays.stream(bonus).map(x -> x.Accentuada).toArray(String[]::new)));

        // Un boto Ok per tancar la finestra
        builder.setPositiveButton("OK", null);

        // Mostrar el AlertDialog per pantalla
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void pistaBtn(View view){
        Optional<Pair<Integer, Word>> indexPista = Partida.demanarPista();

        if(indexPista.isPresent()){
            mostraPrimeraLletra(indexPista.get());
        }
        else{
            mostraMissatge("No tens suficients bonus per demanar una pista");
        }

        updateUi();
    }
    public void setColors() {
        Button aux;
        ImageView circle = findViewById(R.id.circle);
    }

    public TextView[] crearFilaTextViews(int guia, int lletres) {
        TextView[] linea = new TextView[lletres];

        // Obtenir les metriques de la pantalla
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int scrWidth = metrics.widthPixels;
        int scrHeight = metrics.heightPixels;

        // Margin Y
        double altura = scrHeight * 0.00293571;

        // Width TextView
        double width = scrHeight * 0.068;
        double widthMargin = (scrWidth - (lletres * width)) / 2;

        // Margin X
        int finalWMargin = (int) widthMargin;

        for (int i = 0; i < linea.length; i++) {
            // Crear el array de TextView
            TextView letter = new TextView(this);

            // Afegir un ID
            int id = View.generateViewId();
            letter.setId(id);

            // Afegir el text
            letter.setText("");
            letter.setTextSize(38);
            letter.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            letter.setTextColor(Color.BLACK);

            // Afegir el bacground
            letter.setBackgroundResource(R.drawable.textview_border);

            // Afegir color (el mateix que el cercle)
            //letter.setBackgroundColor(colour);
            letter.setVisibility(View.VISIBLE);

            // Posar el TextView dins el Layout
            main.addView(letter);

            // Afegir les constraints al TextView
            ConstraintSet constraintSet = new ConstraintSet();

            // Afegim el constraint horizontals del TextView
            if (i == 0) { // Pirmer TextView
                constraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, finalWMargin);
            } else if (i < linea.length - 1) {  // Qualsevol TextView menos el primer i el darrer
                constraintSet.connect(id, ConstraintSet.START, linea[i - 1].getId(), ConstraintSet.END, 0);
            } else { // Darrer TextView
                constraintSet.connect(id, ConstraintSet.START, linea[i - 1].getId(), ConstraintSet.END, 0);
                constraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, finalWMargin);
            }

            // Afegim el constraint vertical del TextView
            constraintSet.connect(id, ConstraintSet.TOP, wordGuides[guia - 1].getId(), ConstraintSet.TOP, 5);
            constraintSet.connect(id, ConstraintSet.BOTTOM, wordGuides[guia].getId(), ConstraintSet.TOP, 5);

            // Definim les dimensions
            constraintSet.constrainWidth(id, (int) width);
            constraintSet.constrainHeight(id, (int) width);
            // Aplicar les restriccions
            constraintSet.applyTo(main);

            linea[i] = letter;
        }

        return linea;
    }


    // Método para mostrar la palabra en una posición específica: P2
    private void mostraParaula(Pair<Integer, Word> val) {
        if (val.first >= 0 && val.first < hiddenWords.length) {
            for (int i = 0; i < val.second.Accentuada.length(); i++) {
                TextView aux = hiddenWords[val.first][i];
                aux.setText(String.valueOf(val.second.Accentuada.charAt(i)));
            }
        } else {
            throw new IllegalArgumentException("Posición fuera de rango");
        }
    }

    // Método para mostrar la primera letra en una posición específica
    private void mostraPrimeraLletra(Pair<Integer, Word> val) {
        if (val.first >= 0 && val.first < hiddenWords.length) {
            char primeraLletra = Character.toLowerCase(val.second.Accentuada.charAt(0));
            hiddenWords[val.first][0].setText(String.valueOf(primeraLletra));
           // setVisibilityLetra(View.VISIBLE, primeraLletra; mejor en el botón ayuda donde se llama a este método
        } else {
            throw new IllegalArgumentException("Posición fuera de rango");
        }
    }

    // Funcio per desactivar tots els elements menos restartBTN i bonusBTN
    private void disableViews(){
        for (int i = 0; i < main.getChildCount(); i++) {
            View v = main.getChildAt(i);
            if (v.getId() != R.id.restartBTN && v.getId() != R.id.bonusBTN && v.getId() != R.id.circle){
                v.setVisibility(View.GONE);
                v.setEnabled(false);
            }
        }
    }

    // Funcio per activar tots els elements
    private void enableViews(){
        for (int i = 0; i < main.getChildCount(); i++) {
            View v = main.getChildAt(i);
            v.setVisibility(View.VISIBLE);
            v.setEnabled(true);
        }
    }

    private void mostraMissatge(String s) {
        mostraMissatge(s, false);
    }

    private void mostraMissatge(String s, boolean llarg) {
        Toast.makeText(this, s, llarg ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }
}