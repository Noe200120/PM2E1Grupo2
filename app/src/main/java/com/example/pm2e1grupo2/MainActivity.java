package com.example.pm2e1grupo2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnRegistro, btnLista, btnMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRegistro = findViewById(R.id.btnRegistro);
        btnLista = findViewById(R.id.btnLista);
        btnMapa = findViewById(R.id.btnMapa);

        btnRegistro.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, RegistroActivity.class);
            startActivity(i);
        });

        btnLista.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, ListaActivity.class);
            startActivity(i);
        });

        btnMapa.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, MapaActivity.class);
            startActivity(i);
        });
    }
}
