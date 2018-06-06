package mx.edu.itlp.registraya;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import mx.edu.itlp.Datos.Producto;
import mx.edu.itlp.Datos.ProductoAdapter;
import mx.edu.itlp.Datos.Restaurante;
import mx.edu.itlp.Datos.RestauranteAdapter;
import mx.edu.itlp.WebService.WebService;
import mx.edu.itlp.WebService.WebServiceListener;

public class MenuActivity extends AppCompatActivity implements WebServiceListener, ProductoAdapter.CuentaTotalListener {
    Restaurante restaurante;
    WebService Cliente;
    Button btnReintentar;
    ProgressBar progressBar;
    TextView Total;
    float TotalCuenta;
    ListView Lista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        restaurante = (Restaurante) getIntent().getSerializableExtra("Restaurante");
        btnReintentar = findViewById(R.id.btnReintenetar2);
        Lista = findViewById(R.id.Lista);
        Total = findViewById(R.id.Total);
        ((ImageView) findViewById(R.id.c1)).setBackgroundColor(Producto.getColor(0));
        ((ImageView) findViewById(R.id.c2)).setBackgroundColor(Producto.getColor(1));
        ((ImageView) findViewById(R.id.c3)).setBackgroundColor(Producto.getColor(2));
        ((ImageView) findViewById(R.id.c4)).setBackgroundColor(Producto.getColor(3));
        Lista.setEmptyView(findViewById(R.id.btnReintentarContenedor2));
        btnReintentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuscarMenu();
            }
        });
        progressBar = findViewById(R.id.progressBar2);
        BuscarMenu();
    }

    private void BuscarMenu() {
        if (Cliente != null) {
            Cliente.cancel(true);
        }
        Cliente = new WebService(this);
        Cliente.buscarMenu(restaurante.getID());
    }

    @Override
    public void onIniciar() {
        MostrarReintentar(false);
    }

    @Override
    public void onTerminar(Object Resultado) {
        if (Resultado != null) {
            Gson gson = new Gson();
            Type tipoListaRestaurante = new TypeToken<List<Producto>>() {
            }.getType();
            List<Producto> res = gson.fromJson((String) Resultado, tipoListaRestaurante);
            ProductoAdapter AdaptadorRes = new ProductoAdapter(res, getApplicationContext());
            AdaptadorRes.setCuentaTotalListener(this);
            Lista.setAdapter(AdaptadorRes);
        } else {
            Snackbar.make(btnReintentar, "No hubo respuesta del servidor", Snackbar.LENGTH_LONG).show();
            MostrarReintentar(true);
        }
    }

    private void MostrarReintentar(final boolean Mostrar) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Mostrar) {
                    progressBar.setVisibility(View.GONE);
                    btnReintentar.setVisibility(View.VISIBLE);
                } else {
                    btnReintentar.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void OnCuentaTotal(float Total) {
        this.TotalCuenta = Total;
        this.Total.setText("Total: " + String.format("$%,.2f", Total));
    }
}
