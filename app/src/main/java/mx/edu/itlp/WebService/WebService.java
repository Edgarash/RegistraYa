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

    String OPERACION_obtenerRestaurantes = "obtenerRestaurantes";
    String NOMBRE_WEB_SERVICE = "WebService.php";
    String WSDL_TARGET_NAMESPACE = CONSTANTES.DIRECCION_SERVIDOR;
    String DIRECCION_SOAP = WSDL_TARGET_NAMESPACE + NOMBRE_WEB_SERVICE + "?WSDL";
    String SOAP_ACTION_obtenerRestaurantes = WSDL_TARGET_NAMESPACE + OPERACION_obtenerRestaurantes;
    String CLAVE_WEB_SERVICE = "RegistraYAMovil";

    //registrar usuario
    String SOAP_ACTION_registrarusuario = "registrarUsuario";

    //hacer login

    String SOAP_ACTION_login= "hacerLogin";

    SoapObject request;
    WebServiceListener Listener;
    String Soap_action;

    public WebService(WebServiceListener Listener) {
        this.Listener = Listener;
    }

    public void obtenerRestaurante() {
        request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERACION_obtenerRestaurantes);
        this.Soap_action = SOAP_ACTION_obtenerRestaurantes;
        this.execute();
    }

    public void RegistrarUsuario( String Correo, String Contraseña, String Nombre, String Apellidos) {
        Soap_action = SOAP_ACTION_registrarusuario;
        request = new SoapObject(WSDL_TARGET_NAMESPACE, Soap_action);
        request.addProperty("Correo", Correo);
        request.addProperty("Password", Contraseña);
        request.addProperty("Nombre", Nombre);
        request.addProperty("Apellidos", Apellidos);

        execute();
    }

    public void HacerLogin(String Correo, String Contraseña) {
        Soap_action = SOAP_ACTION_login;
        request = new SoapObject(WSDL_TARGET_NAMESPACE, Soap_action);
        request.addProperty("Correo", Correo);
        request.addProperty("Password", Contraseña);
        execute();
    }

    @Override
    protected Object doInBackground(Void... voids) {
        request.addProperty("PASSWS", CLAVE_WEB_SERVICE);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(DIRECCION_SOAP, 60000);
        androidHttpTransport.debug = true;
        try {
            androidHttpTransport.call(this.Soap_action, envelope);
            String Respuesta = (String)envelope.getResponse();
            return Respuesta;
        } catch (IOException e1) {
            e1.printStackTrace();
            this.Listener.onError();
        } catch (XmlPullParserException e3) {
            e3.printStackTrace();
            this.Listener.onError();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object Resultado) {
        this.Listener.onTerminar(Resultado);
    }
}