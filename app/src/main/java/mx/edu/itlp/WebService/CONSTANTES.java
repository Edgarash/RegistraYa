package mx.edu.itlp.WebService;

public class CONSTANTES {
    public static String IP_SERVIDOR = "192.168.1.64";
    public static String PUERTO_SERVIDOR = "8080";

    public static String getIpServidor() {
        return "http://" + IP_SERVIDOR + ":" + PUERTO_SERVIDOR + "/";
    }
}