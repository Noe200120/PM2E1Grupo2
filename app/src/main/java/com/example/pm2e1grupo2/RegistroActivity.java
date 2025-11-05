package com.example.pm2e1grupo2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    EditText txtNombre, txtTelefono, txtCorreo, txtLat, txtLng;
    Button btnGuardar, btnUbicacion;
    LienzoFirma firmaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        txtNombre = findViewById(R.id.txtNombre);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtLat = findViewById(R.id.txtLat);
        txtLng = findViewById(R.id.txtLng);
        firmaView = findViewById(R.id.firmaView);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnUbicacion = findViewById(R.id.btnUbicacion);

        // 🔹 Al presionar "Obtener mi ubicación", abre el mapa
        btnUbicacion.setOnClickListener(v -> {
            Intent intent = new Intent(RegistroActivity.this, MapaActivity.class);
            startActivity(intent);
        });

        // 🔹 Al presionar "Guardar contacto", envía los datos y la firma al servidor
        btnGuardar.setOnClickListener(v -> guardarContacto());
    }

    private void guardarContacto() {
        String url ="http://10.0.2.2/api_pm2e1grupo2/insertar.php";

        VolleyMultipartRequest req = new VolleyMultipartRequest(Request.Method.POST, url,
                r -> Toast.makeText(this, "Guardado correctamente", Toast.LENGTH_SHORT).show(),
                e -> Toast.makeText(this, "Error movil", Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("nombre", txtNombre.getText().toString());
                map.put("telefono", txtTelefono.getText().toString());
                map.put("correo", txtCorreo.getText().toString());
                map.put("lat", txtLat.getText().toString());
                map.put("lng", txtLng.getText().toString());
                return map;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                firmaView.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
                params.put("firma", new DataPart("firma.png", stream.toByteArray(), "image/png"));
                return params;
            }
        };
        Volley.newRequestQueue(this).add(req);
    }
}
