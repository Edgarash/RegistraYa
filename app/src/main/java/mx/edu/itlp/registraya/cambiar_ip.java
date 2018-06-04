package mx.edu.itlp.registraya;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import mx.edu.itlp.Datos.Sesion;

public class cambiar_ip extends AppCompatActivity {

    EditText IP, Puerto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ip);
        IP = findViewById(R.id.IP);
        Puerto = findViewById(R.id.Puerto);
        IP.setText(Sesion.getIP());
        Puerto.setText(Sesion.getPuerto());
        findViewById(R.id.guardarIP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sesion.setPuerto(Puerto.getText().toString());
                Sesion.setIP(IP.getText().toString());
                finish();
            }
        });
    }
}
