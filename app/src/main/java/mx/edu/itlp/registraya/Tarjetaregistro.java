package mx.edu.itlp.registraya;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.Serializable;

import mx.edu.itlp.Datos.Restaurante;
import mx.edu.itlp.Datos.Sesion;
import mx.edu.itlp.WebService.WebService;
import mx.edu.itlp.WebService.WebServiceListener;

public class Tarjetaregistro extends AppCompatActivity implements WebServiceListener, View.OnClickListener {
    Button btnConfirmar;
    ProgressBar progressBar;
    WebService Cliente;
    Restaurante restaurante;
    String Fecha, Mesa;
    AppCompatEditText Tarjeta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarjetaregistro);
        restaurante = (Restaurante) getIntent().getSerializableExtra("Restaurante");
        Fecha = getIntent().getStringExtra("Fecha");
        Mesa = getIntent().getStringExtra("Mesa");
        Tarjeta = findViewById(R.id.tarjeta);
        progressBar = findViewById(R.id.progressBar);
        btnConfirmar = findViewById(R.id.btnRegistrar);
        btnConfirmar.setOnClickListener(this);
    }

    @Override
    public void onIniciar() {
        showRegistrar(false);
    }

    @Override
    public void onTerminar(Object Resultado) {
        if (Resultado != null) {
            String Res = (String) Resultado;
            if (Res.substring(0, 5).equals("ERROR")) {
                String noError = Res.substring(6, 9);
                String msgError = Res.substring(10);
                Snackbar.make(btnConfirmar, msgError, Snackbar.LENGTH_LONG).show();
                showRegistrar(true);
            } else {
                Toast.makeText(getApplicationContext(), "Reservacion hecha con éxito", Toast.LENGTH_LONG).show();
                Intent temp = new Intent(getApplicationContext(), NavigatorActivity.class);
                startActivity(temp);
                finishAffinity();
            }
        } else {
            showRegistrar(true);
            Snackbar.make(btnConfirmar, "No hubo respuesta del servidor", Snackbar.LENGTH_LONG).show();
        }
    }

    private void showRegistrar(final boolean Mostrar) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Mostrar) {
                    progressBar.setVisibility(View.GONE);
                    btnConfirmar.setVisibility(View.VISIBLE);
                } else {
                    btnConfirmar.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (Cliente != null)
            Cliente.cancel(true);
        if (Tarjeta.getText().toString().length() < 16)
            Snackbar.make(Tarjeta, "Tarjeta no válida", Snackbar.LENGTH_LONG).show();
        else {
            Cliente = new WebService(this);
            String Correo = Sesion.getUsuario().getCorreo();
            String Res = String.valueOf(restaurante.getID());
            Cliente.hacerReservacion(Correo, Res, Fecha, Mesa);
        }
    }
}
