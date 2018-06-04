package mx.edu.itlp.registraya;

import android.app.Activity;
import android.app.Fragment;
import android.app.Notification;
import android.content.Context;
import android.net.Uri;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import mx.edu.itlp.WebService.WebService;
import mx.edu.itlp.WebService.WebServiceListener;

public class RegistrarActivity extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    EditText Nombre, Apellidos, Correo, Contraseña, Contraseña2;
    Button Registrar;
    ProgressBar ProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        Nombre = findViewById(R.id.campo_nombre);
        Apellidos = findViewById(R.id.campo_apellido);
        Correo = findViewById(R.id.campo_correo);
        Contraseña = findViewById(R.id.campo_contraseña);
        Contraseña2 = findViewById(R.id.campo_contraseña2);
        Registrar = findViewById(R.id.btnRegistrar);
        Registrar.setOnClickListener(this);
        ProgressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        WebService Registrar = new WebService(this);
        if (Contraseña.getText().toString().equals(Contraseña2.getText().toString())) {
            Registrar.RegistrarUsuario(Correo.getText().toString(), Contraseña.getText().toString(), Nombre.getText().toString(), Apellidos.getText().toString());
        } else {
            Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
        }
    }

    private void showRegistrar(final boolean Mostrar) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Mostrar) {
                    Registrar.setVisibility(View.VISIBLE);
                    ProgressBar.setVisibility(View.GONE);
                } else {
                    Registrar.setVisibility(View.GONE);
                    ProgressBar.setVisibility(View.VISIBLE);
                }
            }
        });
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
                Snackbar.make(Registrar, msgError, Snackbar.LENGTH_LONG).show();

            } else {
                Toast.makeText(getApplicationContext(), Res, Toast.LENGTH_LONG).show();
                finish();
            }
        } else {
            Snackbar.make(Registrar, "No hubo respuesta del servidor", Snackbar.LENGTH_LONG);
            showRegistrar(true);
        }
    }

}
