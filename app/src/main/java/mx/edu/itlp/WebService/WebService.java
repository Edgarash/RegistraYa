package mx.edu.itlp.WebService;

import android.os.AsyncTask;

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


public class WebService extends AsyncTask<Void, Void, Object> {

    public static final String OPERACION_obtenerRestaurantes = "obtenerRestaurantes";
    public static final String NOMBRE_WEB_SERVICE = "WebService.php";
    public static final String WSDL_TARGET_NAMESPACE = CONSTANTES.DIRECCION_SERVIDOR;
    public static final String DIRECCION_SOAP = WSDL_TARGET_NAMESPACE + NOMBRE_WEB_SERVICE + "?WSDL";
    public static final String SOAP_ACTION_obtenerRestaurantes = WSDL_TARGET_NAMESPACE + OPERACION_obtenerRestaurantes;
    private static final String CLAVE_WEB_SERVICE = "RegistraYAMovil";

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

    @Override
    protected Object doInBackground(Void... voids) {
        request.addProperty("PASSWS", CLAVE_WEB_SERVICE);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(DIRECCION_SOAP, 90000);
        androidHttpTransport.debug = true;
        try {
            androidHttpTransport.call(this.Soap_action, envelope);
            String Respuesta = (String)envelope.getResponse();
            return Respuesta;
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (XmlPullParserException e3) {
            e3.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object Resultado) {
        this.Listener.onTerminar(Resultado);
    }
}

