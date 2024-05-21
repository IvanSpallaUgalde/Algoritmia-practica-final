package com.example.practicafinal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private List<Button> btnList = new ArrayList<>();

    private TextView TVpalabra;

    // Numero de letras de la partida
    private int numLetras = 4;

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

        btnList.add(letra1 = findViewById(R.id.letra1));
        btnList.add(letra2 = findViewById(R.id.letra2));
        btnList.add(letra3 = findViewById(R.id.letra3));
        btnList.add(letra4 = findViewById(R.id.letra4));
        btnList.add(letra5 = findViewById(R.id.letra5));
        btnList.add(letra6 = findViewById(R.id.letra6));
        btnList.add(letra7 = findViewById(R.id.letra7));

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
}