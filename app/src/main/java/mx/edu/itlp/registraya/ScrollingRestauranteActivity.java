package mx.edu.itlp.registraya;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import mx.edu.itlp.Datos.Restaurante;
import mx.edu.itlp.Datos.Sesion;
import mx.edu.itlp.Datos.Usuario;

public class ScrollingRestauranteActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    Restaurante restaurante;
    Button HacerReservacion, verMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_restaurante);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        restaurante = (Restaurante) getIntent().getSerializableExtra("Restaurante");
        setTitle(restaurante.getNombre());
        ((TextView) findViewById(R.id.NombreRestaurante)).setText(restaurante.getNombre());
        ((TextView) findViewById(R.id.ColoniaRestaurante)).setText(restaurante.getColonia());
        ((TextView) findViewById(R.id.HorarioAperturaRestaurante)).setText(restaurante.getHorarioApertura());
        ((TextView) findViewById(R.id.HorarioCierreRestaurante)).setText(restaurante.getHorarioCierre());
        ((Button) findViewById(R.id.btnReservar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Sesion.isLoggedIn()) {
                    Intent temp = new Intent(getApplicationContext(), Reservar.class);
                    temp.putExtra("Restaurante", restaurante);
                    startActivity(temp);
                } else {
                    Toast.makeText(getApplicationContext(), "No ha Iniciado Sesión", Toast.LENGTH_LONG).show();
                }
            }
        });
        ((Button) findViewById(R.id.verMenu)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent temp = new Intent(getApplicationContext(), MenuActivity.class);
                temp.putExtra("Restaurante", restaurante);
                startActivity(temp);
            }
        });
        HacerReservacion = findViewById(R.id.btnReservar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (status == ConnectionResult.SUCCESS) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.GoogleMap);
            mapFragment.getMapAsync(this);
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, (Activity) getApplicationContext(), 10);
            dialog.show();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Lat = String.valueOf(restaurante.getLatitud());
                String Lon = String.valueOf(restaurante.getLongitud());
                Uri uri = Uri.parse("geo:" + Lat + "," + Lon + "?z=16&q=" + Lat + "," + Lon + "(" + restaurante.getNombre() + ")");
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setAllGesturesEnabled(false);
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setCompassEnabled(false);
        // Add a marker in Sydney and move the camera
        LatLng Marker = new LatLng(restaurante.getLatitud(), restaurante.getLongitud());
        mMap.addMarker(
                new MarkerOptions().position(Marker).title(
                        "Restaurante: " + restaurante.getNombre()).
                        icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        float ZoomLevel = 16;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Marker, ZoomLevel));
    }
}