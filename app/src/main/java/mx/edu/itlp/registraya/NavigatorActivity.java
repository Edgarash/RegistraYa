package mx.edu.itlp.registraya;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import mx.edu.itlp.Datos.Sesion;
import mx.edu.itlp.Datos.Usuario;

public class NavigatorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoginFragment.OnFragmentInteractionListener {
    Fragment[] fragments = new Fragment[10];
    public static Usuario Usuario;
    TextView Nombre, Correo;
    ImageView Imagen;
    NavigationView navigationView;
    MenuItem IniciarSesion, Reservaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator);
        Sesion.initSesionManager(getApplicationContext());
        //Iniciando Controles
        Toolbar toolbar = findViewById(R.id.toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        Nombre = headerView.findViewById(R.id.NombreUsuario);
        Correo = headerView.findViewById(R.id.CorreoUsuario);
        Imagen = headerView.findViewById(R.id.imageView);
        IniciarSesion = navigationView.getMenu().findItem(R.id.nav_sesion);
        Reservaciones = navigationView.getMenu().findItem(R.id.nav_reservaciones);
        final Activity actTemp = this;
        Imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent b = new Intent(getApplicationContext(), cambiar_ip.class);
                startActivity(b);
            }
        });


        mostrarInfoUsuario();

        //Metodos Extras
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //Lamada al Fragment Principal
        fragments[0] = new getRestaurantes();
        navigationView.setCheckedItem(R.id.nav_ver_restaurantes);
        FragmentManager ft = getSupportFragmentManager();
        ft.beginTransaction().replace(R.id.content_frame, fragments[0]).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment Activity = null;

        if (id == R.id.nav_ver_restaurantes) {
            Activity = fragments[0] = fragments[0] == null ? new getRestaurantes() : fragments[0];
        } else if (id == R.id.nav_sesion) {
            if (Sesion.isLoggedIn()) {
                Sesion.cerrarSesion();
                Toast.makeText(getApplicationContext(), "Vuelva Pronto!!!", Toast.LENGTH_LONG).show();
                mostrarInfoUsuario();
                Activity = fragments[0];
            } else {
                Activity = fragments[1] = fragments[1] == null ? LoginFragment.newInstance(this) : fragments[1];
            }
        } else if (id == R.id.nav_reservaciones) {
            Activity = fragments[2] = fragments[2] == null ? new ReservacionesFragment() : fragments[2];
        }

        if (Activity != null) {
            FragmentManager ft = getSupportFragmentManager();
            ft.beginTransaction().replace(R.id.content_frame, Activity).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void mostrarInfoUsuario() {
        String sCorreo = "Inicia Sesión";
        String sNombre = "Invitado";
        if (Sesion.isLoggedIn()) {
            Usuario temp = Sesion.getUsuario();
            sCorreo = temp.getCorreo();
            sNombre = temp.getNombre();
            IniciarSesion.setTitle("Cerrar Sesión");
            Reservaciones.setVisible(true);
        } else {
            IniciarSesion.setTitle("Iniciar Sesión");
            Reservaciones.setVisible(false);
        }
        this.Nombre.setText(sNombre);
        this.Correo.setText(sCorreo);
    }

    @Override
    public void onFragmentInteraction(Object User) {
        final Gson gson = new Gson();
        final Usuario usuario = gson.fromJson((String) User, Usuario.class);
        NavigatorActivity.Usuario = usuario;
        Sesion.guardarUsuario(usuario);
        mostrarInfoUsuario();
        navigationView.setCheckedItem(R.id.nav_ver_restaurantes);
        FragmentManager ft = getSupportFragmentManager();
        ft.beginTransaction().replace(R.id.content_frame, new getRestaurantes()).commit();
    }
}