package mx.edu.itlp.Datos;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.concurrent.ExecutionException;

import mx.edu.itlp.WebService.CONSTANTES;

public class Sesion {
    private static SharedPreferences sharedPreferences;
    private static final String PACK_NAME = "mx.itlp.edu.ReservaYA!";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String USER_NAME = "UserName";
    private static final String USER_EMAIL = "UserMail";
    private static final String USER_PASSWORD = "UserPassword";
    private static final String USER_LASTNAME = "UserLastName";
    private static final String IP = "IP_ADDRESS";
    private static final String Puerto = "PORT";
    private static boolean REQUIERE_TARJETA = false;

    public static void initSesionManager(Context applicationContext) {
        sharedPreferences = applicationContext.getSharedPreferences(PACK_NAME, Context.MODE_PRIVATE);
        CONSTANTES.IP_SERVIDOR = getIP();
        CONSTANTES.PUERTO_SERVIDOR = getPuerto();
    }

    public static void setTarjetaRequerida(boolean Requerido) {
        REQUIERE_TARJETA = Requerido;
    }

    public static boolean getTarjetaRequerida() {
        return REQUIERE_TARJETA;
    }

    private static boolean sesionManagerIniciado() {
        if (sharedPreferences != null) {
            return true;
        } else {
            throw new Error("Sesion Manager no iniciado");
        }
    }

    public static boolean isLoggedIn() {
        sesionManagerIniciado();
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public static Usuario getUsuario() {
        sesionManagerIniciado();
        return new Usuario(
                sharedPreferences.getString(USER_EMAIL, ""),
                sharedPreferences.getString(USER_PASSWORD, ""),
                sharedPreferences.getString(USER_NAME, ""),
                sharedPreferences.getString(USER_LASTNAME, "")
        );
    }

    public static void guardarUsuario(Usuario usuario) {
        sesionManagerIniciado();
        SharedPreferences.Editor temp = sharedPreferences.edit();
        temp.putString(USER_NAME, usuario.getNombre());
        temp.putString(USER_LASTNAME, usuario.getApellidos());
        temp.putString(USER_EMAIL, usuario.getCorreo());
        temp.putString(USER_PASSWORD, usuario.getPassword());
        temp.putBoolean(IS_LOGIN, true);
        temp.apply();
    }

    public static void cerrarSesion() {
        sesionManagerIniciado();
        SharedPreferences.Editor temp = sharedPreferences.edit();
        temp.putString(USER_NAME, "");
        temp.putString(USER_LASTNAME, "");
        temp.putString(USER_EMAIL, "");
        temp.putString(USER_PASSWORD, "");
        temp.putBoolean(IS_LOGIN, false);
        temp.apply();
    }

    public static String getIP() {
        sesionManagerIniciado();
        return sharedPreferences.getString(IP, CONSTANTES.IP_SERVIDOR);
    }

    public static void setIP(String Dir_IP) {
        sesionManagerIniciado();
        sharedPreferences.edit().putString(Sesion.IP, Dir_IP).apply();
        CONSTANTES.IP_SERVIDOR = Dir_IP;
    }

    public static String getPuerto() {
        sesionManagerIniciado();
        return sharedPreferences.getString(Puerto, CONSTANTES.PUERTO_SERVIDOR);
    }

    public static void setPuerto(String Port) {
        sesionManagerIniciado();
        sharedPreferences.edit().putString(Puerto, Port).apply();
        CONSTANTES.PUERTO_SERVIDOR = Port;
    }

}