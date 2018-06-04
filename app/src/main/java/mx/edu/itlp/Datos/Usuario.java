package mx.edu.itlp.Datos;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import java.net.PasswordAuthentication;

public class Usuario {


    private String Password;
    private String Correo;
    private String Nombre;
    private String Apellidos;

    public Usuario(String Correo, String Password, String Nombre, String Apellidos) {
        this.Correo = Correo;
        this.Password = Password;
        this.Nombre = Nombre;
        this.Apellidos = Apellidos;
    }

    public String getPassword() {
        return Password;
    }

    public String getCorreo() {
        return Correo;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getApellidos() {
        return Apellidos;
    }
}
