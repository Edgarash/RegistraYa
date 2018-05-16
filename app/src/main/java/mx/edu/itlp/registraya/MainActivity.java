package mx.edu.itlp.registraya;

import android.app.Dialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

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

    private void BuscarRestaurantes() {
        WebService Cliente = new WebService(this);
        Cliente.obtenerRestaurante();
    }

    @Override
    public void onIniciar() {
    }

    @Override
    public void onActualizar() {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onTerminar(Object Resultado) {
        if (Resultado != null) {
            Gson gson = new Gson();
            Type tipoListaRestaurante = new TypeToken<List<Restaurante>>() {}.getType();
            List<Restaurante> res = gson.fromJson((String) Resultado, tipoListaRestaurante);
            RestauranteAdapter AdaptadorRes = new RestauranteAdapter(res, this);
            ListaDeRestaurantes.setAdapter(AdaptadorRes);
        } else {
            Snackbar.make(Barrita, "ERROR", Snackbar.LENGTH_LONG).show();

        }
    }
}
