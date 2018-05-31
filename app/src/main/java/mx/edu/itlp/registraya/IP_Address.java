package mx.edu.itlp.registraya;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import mx.edu.itlp.WebService.CONSTANTES;

public class IP_Address extends AppCompatActivity {
    EditText IP;
    Button Guardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip__address);
        IP = findViewById(R.id.Direccion_IP);
        IP.setText(CONSTANTES.IP_SERVIDOR);
        Guardar = findViewById(R.id.BotonGuardar);
        Guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CONSTANTES.IP_SERVIDOR = IP.getText().toString();
                finish();
            }
        });
    }
}
