package mx.edu.itlp.WebService;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Time;
import java.util.List;
import java.util.concurrent.TimeoutException;


public class WebService extends AsyncTask<Void, Void, Object> {

    //Constantes
    String NOMBRE_WEB_SERVICE = "WebService.php";
    String CLAVE_WEB_SERVICE = "RegistraYAMovil";

    //Metodos Constantes
    String getWSDL_TARGET_NAMESPACE() {
        return CONSTANTES.getIpServidor();
    }

    String getDIRECCION_SOAP() {
        return getWSDL_TARGET_NAMESPACE() + NOMBRE_WEB_SERVICE + "?wsdl";
    }

    String getSOAP_ACTION(String Action) {
        OPERACION = Action;
        return getWSDL_TARGET_NAMESPACE() + Action;
    }

    //Operaciones
    String OPERACION_obtenerRestaurantes = "obtenerRestaurantes";
    String OPERACION_registrarUsuario = "registrarUsuario";
    String OPERACION_login = "hacerLogin";
    String OPERACION_reservar = "hacerReservacion";

    //Locales
    private SoapObject REQUEST;
    private WebServiceListener LISTENER;
    private String SOAP_ACTION;
    private String OPERACION;

    public WebService(WebServiceListener Listener) {
        this.LISTENER = Listener;
    }

    public void obtenerRestaurante() {
        this.SOAP_ACTION = getSOAP_ACTION(OPERACION_obtenerRestaurantes);
        REQUEST = new SoapObject(getWSDL_TARGET_NAMESPACE(), OPERACION);
        this.execute();
    }

    public void RegistrarUsuario(String Correo, String Contraseña, String Nombre, String Apellidos) {
        this.SOAP_ACTION = getSOAP_ACTION(OPERACION_registrarUsuario);
        REQUEST = new SoapObject(getWSDL_TARGET_NAMESPACE(), OPERACION);
        REQUEST.addProperty("Correo", Correo);
        REQUEST.addProperty("Password", Contraseña);
        REQUEST.addProperty("Nombre", Nombre);
        REQUEST.addProperty("Apellidos", Apellidos);
        execute();
    }

    public void HacerLogin(String Correo, String Contraseña) {
        SOAP_ACTION = getSOAP_ACTION(OPERACION_login);
        REQUEST = new SoapObject(getWSDL_TARGET_NAMESPACE(), OPERACION);
        REQUEST.addProperty("Correo", Correo);
        REQUEST.addProperty("Password", Contraseña);
        execute();
    }

    public void hacerReservacion(String Correo, String Restaurante, String Fecha, String Mesa) {
        SOAP_ACTION = getSOAP_ACTION(OPERACION_reservar);
        REQUEST = new SoapObject(getWSDL_TARGET_NAMESPACE(), OPERACION);
        REQUEST.addProperty("Correo", Correo);
        REQUEST.addProperty("Restaurante", Restaurante);
        REQUEST.addProperty("Fecha", Fecha);
        REQUEST.addProperty("Mesa", Mesa);
        execute();
    }

    @Override
    protected void onPreExecute() {
        LISTENER.onIniciar();
    }

    @Override
    protected Object doInBackground(Void... voids) {
        REQUEST.addProperty("PASSWS", CLAVE_WEB_SERVICE);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(REQUEST);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(getDIRECCION_SOAP(), 30000);
        androidHttpTransport.debug = true;
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            String Respuesta = (String) envelope.getResponse();
            return Respuesta;
        } catch (IOException | XmlPullParserException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object Resultado) {
        LISTENER.onTerminar(Resultado);
    }
}