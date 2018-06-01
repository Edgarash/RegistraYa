package mx.edu.itlp.registraya;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import mx.edu.itlp.Datos.Restaurante;
import mx.edu.itlp.Datos.RestauranteAdapter;
import mx.edu.itlp.WebService.*;

public class MainActivity extends AppCompatActivity implements WebServiceListener {
    ListView ListaDeRestaurantes;
    ProgressBar Barrita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListaDeRestaurantes = findViewById(R.id.Restaurantes);
        Barrita = findViewById(R.id.progressBar);
        ListaDeRestaurantes.setEmptyView(Barrita);
        BuscarRestaurantes();
    }

    public void BuscarRestaurantes() {
        WebService Cliente = new WebService(this);
        Cliente.obtenerRestaurante();
    }

    @Override
    public void onError() {
        BuscarRestaurantes();
    }

    @Override
    public void onTerminar(Object Resultado) {
        if (Resultado != null) {
            Gson gson = new Gson();
            Type tipoListaRestaurante = new TypeToken<List<Restaurante>>() {
            }.getType();
            List<Restaurante> res = gson.fromJson((String) Resultado, tipoListaRestaurante);
            RestauranteAdapter AdaptadorRes = new RestauranteAdapter(res, this);
            ListaDeRestaurantes.setAdapter(AdaptadorRes);
        } else {
            Snackbar.make(Barrita, "Hubo un error al conectar con la base de datos", Snackbar.LENGTH_LONG).show();
            BuscarRestaurantes();
        }
    }
}