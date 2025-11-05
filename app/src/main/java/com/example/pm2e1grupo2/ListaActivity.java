package com.example.pm2e1grupo2;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListaActivity extends AppCompatActivity {

    ListView lista;
    EditText txtBuscar;
    ArrayList<String> datos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_lista);

        lista = findViewById(R.id.listaContactos);
        txtBuscar = findViewById(R.id.txtBuscar);

        cargarDatos("");

        txtBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                cargarDatos(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence c, int a, int b, int d) {}

            @Override
            public void onTextChanged(CharSequence c, int a, int b, int d) {}
        });
    }

    void cargarDatos(String buscar) {
        String url = "http://10.0.2.2/api_pm2e1grupo2/listar.php?buscar=" + buscar;

        StringRequest req = new StringRequest(Request.Method.GET, url,
                resp -> {
                    try {
                        JSONArray arr = new JSONArray(resp);
                        datos.clear();
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            datos.add(obj.getString("nombre") + " - " + obj.getString("telefono"));
                        }
                        lista.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datos));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                err -> {}
        );

        Volley.newRequestQueue(this).add(req);
    }
}
