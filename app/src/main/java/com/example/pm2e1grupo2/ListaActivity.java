package com.example.pm2e1grupo2;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListaActivity extends AppCompatActivity {

    ListView lista;
    EditText txtBuscar;

    ArrayList<Integer> ids = new ArrayList<>();
    ArrayList<String> nombres = new ArrayList<>();
    ArrayList<String> telefonos = new ArrayList<>();
    ArrayList<String> correos = new ArrayList<>();
    ArrayList<String> lats = new ArrayList<>();
    ArrayList<String> lngs = new ArrayList<>();

    String URL_LISTAR = "http://10.0.2.2/api_pm2e1grupo2/listar.php";
    String URL_ELIMINAR = "http://10.0.2.2/api_pm2e1grupo2/eliminar.php?id=";
    String URL_ACTUALIZAR = "http://10.0.2.2/api_pm2e1grupo2/actualizar.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        lista = findViewById(R.id.listaContactos);
        txtBuscar = findViewById(R.id.txtBuscar);

        cargarDatos("");


        txtBuscar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence s, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable s) { cargarDatos(s.toString()); }
        });
    }


    void cargarDatos(String buscar) {
        String url = URL_LISTAR + "?buscar=" + buscar;

        StringRequest req = new StringRequest(Request.Method.GET, url,
                resp -> {
                    try {
                        JSONArray arr = new JSONArray(resp);
                        ids.clear(); nombres.clear(); telefonos.clear();
                        correos.clear(); lats.clear(); lngs.clear();

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            ids.add(obj.getInt("id"));
                            nombres.add(obj.getString("nombre"));
                            telefonos.add(obj.getString("telefono"));
                            correos.add(obj.getString("correo"));
                            lats.add(obj.getString("lat"));
                            lngs.add(obj.getString("lng"));
                        }

                        ContactoAdapter adaptador = new ContactoAdapter(
                                this, ids, nombres, telefonos, correos, lats, lngs
                        );
                        lista.setAdapter(adaptador);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                    }
                },
                err -> Toast.makeText(this, "Error de conexión con el servidor", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(req);
    }


    public void mostrarDialogoEliminar(int id, String nombre) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Eliminar contacto")
                .setMessage("¿Deseas eliminar a " + nombre + "?")
                .setPositiveButton("Sí", (dialog, which) -> eliminarContacto(id))
                .setNegativeButton("Cancelar", null)
                .show();
    }


    private void eliminarContacto(int id) {
        String url = URL_ELIMINAR + id;

        StringRequest req = new StringRequest(Request.Method.GET, url,
                resp -> {
                    if (resp.trim().equalsIgnoreCase("OK")) {
                        Toast.makeText(this, "Contacto eliminado correctamente", Toast.LENGTH_SHORT).show();
                        cargarDatos("");
                    } else {
                        Toast.makeText(this, "No se pudo eliminar: " + resp, Toast.LENGTH_LONG).show();
                    }
                },
                err -> Toast.makeText(this, "Error al Eliminar: " + err.getMessage(), Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(req);
    }


    public void mostrarDialogoActualizar(int id, String nombre, String telefono, String correo, String lat, String lng) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Actualizar contacto");

        final EditText txtNombre = new EditText(this);
        final EditText txtTelefono = new EditText(this);
        final EditText txtCorreo = new EditText(this);
        final EditText txtLat = new EditText(this);
        final EditText txtLng = new EditText(this);

        txtNombre.setHint("Nombre");
        txtTelefono.setHint("Teléfono");
        txtCorreo.setHint("Correo");
        txtLat.setHint("Latitud");
        txtLng.setHint("Longitud");

        txtNombre.setText(nombre);
        txtTelefono.setText(telefono);
        txtCorreo.setText(correo);
        txtLat.setText(lat);
        txtLng.setText(lng);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(60, 40, 60, 10);

        layout.addView(txtNombre);
        layout.addView(txtTelefono);
        layout.addView(txtCorreo);
        layout.addView(txtLat);
        layout.addView(txtLng);

        builder.setView(layout);

        builder.setPositiveButton("Actualizar", (dialog, which) -> {
            String nuevoNombre = txtNombre.getText().toString().trim();
            String nuevoTelefono = txtTelefono.getText().toString().trim();
            String nuevoCorreo = txtCorreo.getText().toString().trim();
            String nuevaLat = txtLat.getText().toString().trim();
            String nuevaLng = txtLng.getText().toString().trim();

            if (nuevoNombre.isEmpty() || nuevoTelefono.isEmpty()) {
                Toast.makeText(this, "Completa al menos nombre y teléfono", Toast.LENGTH_SHORT).show();
                return;
            }
            actualizarContacto(id, nuevoNombre, nuevoTelefono, nuevoCorreo, nuevaLat, nuevaLng);
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }


    private void actualizarContacto(int id, String nombre, String telefono, String correo, String lat, String lng) {
        StringRequest req = new StringRequest(Request.Method.POST, URL_ACTUALIZAR,
                resp -> {
                    if (resp.trim().equalsIgnoreCase("OK")) {
                        Toast.makeText(this, "Contacto actualizado correctamente", Toast.LENGTH_SHORT).show();
                        cargarDatos("");
                    } else {
                        Toast.makeText(this, "No se pudo actualizar: " + resp, Toast.LENGTH_LONG).show();
                    }
                },
                err -> Toast.makeText(this, "Error actualizar: " + err.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                params.put("nombre", nombre);
                params.put("telefono", telefono);
                params.put("correo", correo);
                params.put("lat", lat);
                params.put("lng", lng);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(req);
    }
}
