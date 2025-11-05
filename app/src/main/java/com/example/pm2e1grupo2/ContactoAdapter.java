package com.example.pm2e1grupo2;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactoAdapter extends ArrayAdapter<String> {

    private final ListaActivity activity;
    private final ArrayList<Integer> ids;
    private final ArrayList<String> nombres;
    private final ArrayList<String> telefonos;
    private final ArrayList<String> correos;
    private final ArrayList<String> lats;
    private final ArrayList<String> lngs;

    public ContactoAdapter(ListaActivity activity,
                           ArrayList<Integer> ids,
                           ArrayList<String> nombres,
                           ArrayList<String> telefonos,
                           ArrayList<String> correos,
                           ArrayList<String> lats,
                           ArrayList<String> lngs) {
        super(activity, R.layout.item_contacto, nombres);
        this.activity = activity;
        this.ids = ids;
        this.nombres = nombres;
        this.telefonos = telefonos;
        this.correos = correos;
        this.lats = lats;
        this.lngs = lngs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.item_contacto, parent, false);

        TextView tvNombreTelefono = view.findViewById(R.id.tvNombreTelefono);
        TextView tvCorreo = view.findViewById(R.id.tvCorreo);
        Button btnActualizar = view.findViewById(R.id.btnActualizar);
        Button btnEliminar = view.findViewById(R.id.btnEliminar);

        tvNombreTelefono.setText(nombres.get(position) + " - " + telefonos.get(position));
        tvCorreo.setText("Correo: " + correos.get(position));


        btnActualizar.setOnClickListener(v -> {
            int id = ids.get(position);
            activity.mostrarDialogoActualizar(
                    id,
                    nombres.get(position),
                    telefonos.get(position),
                    correos.get(position),
                    lats.get(position),
                    lngs.get(position)
            );
        });


        btnEliminar.setOnClickListener(v -> {
            int id = ids.get(position);
            String nombre = nombres.get(position);
            activity.mostrarDialogoEliminar(id, nombre);
        });

        return view;
    }
}
