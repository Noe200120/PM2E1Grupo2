package com.example.pm2e1grupo2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
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


        InputFilter soloLetras = new InputFilter() {
            public CharSequence filter(CharSequence src, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    char c = src.charAt(i);
                    if (!Character.isLetter(c) && c != ' ') {
                        Toast.makeText(RegistroActivity.this, "Solo se permiten letras en el nombre", Toast.LENGTH_SHORT).show();
                        return "";
                    }
                }
                return null;
            }
        };
        txtNombre.setFilters(new InputFilter[]{soloLetras, new InputFilter.LengthFilter(50)});


        txtTelefono.setKeyListener(DigitsKeyListener.getInstance("0123456789"));

        btnUbicacion.setOnClickListener(v -> {
            Intent intent = new Intent(RegistroActivity.this, MapaActivity.class);
            startActivity(intent);
        });

        btnGuardar.setOnClickListener(v -> guardarContacto());
    }

    private void guardarContacto() {
        if (txtNombre.getText().toString().trim().isEmpty() ||
                txtTelefono.getText().toString().trim().isEmpty() ||
                txtCorreo.getText().toString().trim().isEmpty() ||
                txtLat.getText().toString().trim().isEmpty() ||
                txtLng.getText().toString().trim().isEmpty()) {

            Toast.makeText(this, "Por favor completa todos los campos antes de guardar", Toast.LENGTH_LONG).show();
            return;
        }

        String url = "http://10.0.2.2/api_pm2e1grupo2/insertar.php";

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(
                Request.Method.POST, url,
                response -> {
                    if (response.trim().equalsIgnoreCase("OK")) {
                        Toast.makeText(this, "Contacto guardado correctamente", Toast.LENGTH_SHORT).show();
                        limpiarCampos();
                    } else {
                        Toast.makeText(this, "Error al guardar: " + response, Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Error móvil: " + error.getMessage(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("nombre", txtNombre.getText().toString().trim());
                map.put("telefono", txtTelefono.getText().toString().trim());
                map.put("correo", txtCorreo.getText().toString().trim());
                map.put("lat", txtLat.getText().toString().trim());
                map.put("lng", txtLng.getText().toString().trim());
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

        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtLat.setText("");
        txtLng.setText("");
        firmaView.limpiar();
    }
}
