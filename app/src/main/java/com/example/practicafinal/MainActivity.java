package com.example.practicafinal;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //region Llistes
    // Llista de botons del cercle
    private List<Button> btnList = new ArrayList<>();
    // Llista de guidelines de la seccio de paraules
    private List<Guideline> wordGuides = new ArrayList<>();
    // Lliste dels arrays de TextViews del panel de Paraules
    private List<TextView[]> panelParaules = new ArrayList<>();
    //endregion

    //region Variables
    private ConstraintLayout main;
    private int colour;
    private Button letra1;
    private Button letra2;
    private Button letra3;
    private Button letra4;
    private Button letra5;
    private Button letra6;
    private Button letra7;

    private int totalParaules;

    private int totalEncertades;

    private TextView TVpalabra;

    // Numero de letras de la partida
    private int numLetras = 4;
    //endregion

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

        colour = Color.rgb(255,100,30);

        main = findViewById(R.id.main);

        btnList.add(letra1 = findViewById(R.id.letra1));
        btnList.add(letra2 = findViewById(R.id.letra2));
        btnList.add(letra3 = findViewById(R.id.letra3));
        btnList.add(letra4 = findViewById(R.id.letra4));
        btnList.add(letra5 = findViewById(R.id.letra5));
        btnList.add(letra6 = findViewById(R.id.letra6));
        btnList.add(letra7 = findViewById(R.id.letra7));

        // Llista de guies per la creacio del panel de paraules
        wordGuides.add(findViewById(R.id.topWordGuides));
        wordGuides.add(findViewById(R.id.firstGuide));
        wordGuides.add(findViewById(R.id.secondGuide));
        wordGuides.add(findViewById(R.id.thirdGuide));
        wordGuides.add(findViewById(R.id.fourthGuide));
        wordGuides.add(findViewById(R.id.botWordsGuide));

        TVpalabra = findViewById(R.id.TVpalabra);

        // Escondemos los botones dentro del circulo
        for (int i = 0; i < btnList.size(); i++){
            setVisibilityLetra(View.GONE, btnList.get(i));
        }

        for (int i = 0; i < numLetras; i++){
            setVisibilityLetra(View.VISIBLE, btnList.get(i));
        }

        // Vaciar texto del TextView
        TVpalabra.setText("");
        setColors();
    }

    public void setVisibilityLetra(int mode, Button btn){
        btn.setVisibility(mode);
    }

    public void sendBTN(View view){
        // De forma temporal este boton añadira un texto al TextView palabra
        TVpalabra.setText("PRUEBA");
    }

    public void clearBTN(View view){
        // Elimina el texto en el Text View palabra
        clear();
    }

    public void setLletra(View view){
        // Obtener el boton pulsado
        Button btn = (Button) view;

        // Obtener texto del boton pulsado
        String letra = btn.getText().toString();

        // Añadir el texto
        StringBuilder sb = new StringBuilder(TVpalabra.getText().toString());
        sb.append(letra);
        TVpalabra.setText(sb.toString());

        // Hacer la letra no visible
        setVisibilityLetra(View.GONE, btn);
    }

    public void randomBTN(View view){
        clear();

        String[] aux = new String[numLetras];
        for (int i = 0; i < numLetras; i++){
            Button b = btnList.get(i);
            aux[i] = b.getText().toString();
        }
        randomize(aux, aux.length);

        for (int i = 0; i < numLetras; i++){
            Button b = btnList.get(i);
            b.setText(aux[i]);
        }

    }

    public void clear(){
        TVpalabra.setText("");
        for (int i = 0; i < numLetras; i++){
            setVisibilityLetra(View.VISIBLE, btnList.get(i));
        }
    }

    // Funcio de randomitzacio de Fisher-Yates per reordenar les lletres dels botons
    public void randomize(String[] arr, int n){
        // Creating a object for Random class
        Random r = new Random();

        // Start from the last element and swap one by one. We don't
        // need to run for the first element that's why i > 0
        for (int i = n-1; i > 0; i--) {

            // Pick a random index from 0 to i
            int j = r.nextInt(i+1);

            // Swap arr[i] with the element at random index
            String temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    // Quan s'implementi la logica del programa informara de quantes paraules s'han encertat i
    // llistarles
    public void bonusBTN(View view){
        // Definicio de valors temporal, una vegada creada la logica del joc s'eliminaran aquestes linies
        totalEncertades = 0;
        totalParaules = 0;


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Titol del AlertDialog amb la progresio de paraules encertades
        builder.setTitle("Encertades ("+totalEncertades+" de "+totalParaules+"):");

        // Text del AlertDialog que mes endevant es cambiara per una llista ordenada alfabetiment de les paraules
        builder.setMessage("INSERTAR LISTA DE PALABRAS ORDENADAS ALFABETICAMENTE");

        // Un boto Ok per tancar la finestra
        builder.setPositiveButton("OK", null);

        // Mostrar el AlertDialog per pantalla
        AlertDialog dialog =  builder.create();
        dialog.show();
    }

    public void setColors(){
        Button aux;
        for(int i = 0; i < btnList.size(); i++){
            aux = btnList.get(i);
            aux.setBackgroundColor(colour);
        }
        ImageView circle = findViewById(R.id.circle);
        circle.setBackgroundColor(colour);
    }

    public TextView[] crearFilaTextViews(int guia, int lletres){
        TextView[] linea = new TextView[lletres];
        for (int i = 0; i < lletres; i++){
            TextView letter = new TextView(this);

            // Afegir un ID
            int id = View.generateViewId();
            letter.setId(id);

            // Afegir el text
            letter.setText("");

            // Afegir color (el mateix que el cercle)
            letter.setBackgroundColor(colour);

            // Posar el TextView dins el Layout
            main.addView(letter);
    }

        return linea;
    }
    // Método para mostrar la palabra en una posición específica: P2
    private void mostraParaula(String s, int posicio) {
        if (posicio >= 0 && posicio < hiddenWords.length) {
            hiddenWords[posicio].setText(s);
        } else {
            throw new IllegalArgumentException("Posición fuera de rango");
        }
    }

    // Método para mostrar la primera letra en una posición específica
    private void mostraPrimeraLletra(String s, int posicio) {
        if (posicio >= 0 && posicio < hiddenWords.length) {
            char primeraLletra = Character.toLowerCase(s.charAt(0));
            hiddenWords[posicio].setText(String.valueOf(primeraLletra));
           // setVisibilityLetra(View.VISIBLE, primeraLletra; mejor en el botón ayuda donde se llama a este método
        } else {
            throw new IllegalArgumentException("Posición fuera de rango");
        }
    }
}