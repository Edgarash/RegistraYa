package mx.edu.itlp.registraya;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.inputmethod.CorrectionInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.widget.ExpandableListView;
import android.widget.Toast;


import java.util.Calendar;

import mx.edu.itlp.Datos.Restaurante;
import mx.edu.itlp.Datos.Sesion;
import mx.edu.itlp.WebService.WebService;
import mx.edu.itlp.WebService.WebServiceListener;

public class Reservar extends AppCompatActivity implements View.OnClickListener, WebServiceListener {

    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    private static final String BARRA = "-";
    private static String ACCION = "";
    private static String Mesa;

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    //Hora
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);

    //Widgets
    EditText etFecha, etHora;
    ImageButton ibObtenerFecha, ibObtenerHora;
    Button btnRegistrar;
    WebService Cliente;
    Restaurante restaurante;
    ProgressBar progressBar;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservar);

        restaurante = (Restaurante) getIntent().getSerializableExtra("Restaurante");
        ((TextView)findViewById(R.id.NombreRestaurante)).setText(restaurante.getNombre());
        etFecha = (EditText) findViewById(R.id.et_mostrar_fecha_picker);
        etHora = (EditText) findViewById(R.id.et_mostrar_hora_picker);

        ibObtenerFecha = (ImageButton) findViewById(R.id.ib_obtener_fecha);
        ibObtenerHora = (ImageButton) findViewById(R.id.ib_obtener_hora);

        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);

        ibObtenerFecha.setOnClickListener(this);
        ibObtenerHora.setOnClickListener(this);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_obtener_fecha:
                obtenerFecha();
                break;
            case R.id.ib_obtener_hora:
                obtenerHora();
                break;
            case R.id.btnRegistrar:
                if (Cliente != null)
                    Cliente.cancel(true);
                Cliente = new WebService(this);
                String Correo = Sesion.getUsuario().getCorreo();
                String Res = String.valueOf(restaurante.getID());
                String Fecha = etFecha.getText().toString() + " " + etHora.getText().toString().substring(0, 5) + ":00";
                Cliente.hacerReservacion(Correo, Res, Fecha, Mesa);
                break;
        }
    }


    private void obtenerFecha() {
        DatePickerDialog recogerFecha = new DatePickerDialog(this, R.style.TemaFecha, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                final int mesActual = month + 1;

                String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);

                etFecha.setText(year + "-" + mesFormateado + "-" + diaFormateado);


            }
        }, anio, mes, dia);

        recogerFecha.show();

    }

    private void obtenerHora() {
        TimePickerDialog recogerHora = new TimePickerDialog(this, R.style.TemaFecha, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                String horaFormateada = (hourOfDay < 9) ? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                String minutoFormateado = (minute < 9) ? String.valueOf(CERO + minute) : String.valueOf(minute);

                String AM_PM;
                if (hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }

                etHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
            }

        }, hora, minuto, false);

        recogerHora.show();
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Mesa para");

        // Adding child data
        List<String> mesas = new ArrayList<String>();
        mesas.add("6 Personas");
        mesas.add("4 Personas");
        mesas.add("2 Personas");
        mesas.add("1 Persona");

        listDataChild.put(listDataHeader.get(0), mesas); // Header, Child data

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                Mesa = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).substring(0, 1);
                return false;
            }
        });

    }

    @Override
    public void onIniciar() {
        showLogin(false);
    }

    @Override
    public void onTerminar(Object Resultado) {
        if (Resultado != null) {
            String Res = (String) Resultado;
            if (Res.substring(0, 5).equals("ERROR")) {
                String noError = Res.substring(6, 9);
                String msgError = Res.substring(10);
                Snackbar.make(btnRegistrar, msgError, Snackbar.LENGTH_LONG).show();
                showLogin(true);
            } else {
                Toast.makeText(getApplicationContext(), "Reservacion hecha con éxito", Toast.LENGTH_LONG).show();
                finish();
            }
        } else {
            showLogin(true);
            Snackbar.make(btnRegistrar, "No hubo respuesta del servidor", Snackbar.LENGTH_LONG).show();
        }
    }

    private void showLogin(final boolean Mostrar) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Mostrar) {
                    progressBar.setVisibility(View.GONE);
                    btnRegistrar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    btnRegistrar.setVisibility(View.GONE);
                }
            }
        });
    }
}