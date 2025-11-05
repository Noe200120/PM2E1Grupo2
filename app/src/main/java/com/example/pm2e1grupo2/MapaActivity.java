package com.example.pm2e1grupo2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MapaActivity extends AppCompatActivity {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Configuration.getInstance().load(
                getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
        );
        Configuration.getInstance().setUserAgentValue(getPackageName());

        setContentView(R.layout.activity_mapa);

        mapView = findViewById(R.id.mapa);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);


        GeoPoint punto = new GeoPoint(14.0723, -87.1921);
        mapView.getController().setZoom(15.0);
        mapView.getController().setCenter(punto);


        Marker marcador = new Marker(mapView);
        marcador.setPosition(punto);
        marcador.setTitle("Ubicación de ejemplo");
        mapView.getOverlays().add(marcador);
    }
}
