package mx.edu.itlp.WebService;

public class Restaurante {
    int ID;
    String Nombre;
    String Colonia;
    double Latitud, Longitud;
    String HorarioApertura, HorarioCierre;

    public Restaurante(int ID, String Nombre, String Colonia,
                       double Latitud, double Longitud,
                       String HorarioApertura, String HorarioCierre) {
        this.ID = ID;
        this.Nombre = Nombre;
        this.Colonia = Colonia;
        this.Latitud = Latitud;
        this.Longitud = Longitud;
        this.HorarioApertura = HorarioApertura;
        this.HorarioCierre = HorarioCierre;
    }
}