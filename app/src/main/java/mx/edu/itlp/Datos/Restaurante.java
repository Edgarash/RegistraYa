package mx.edu.itlp.Datos;

import java.io.Serializable;

public class Restaurante implements Serializable {
    private int ID;
    String Nombre;
    String Colonia;
    private double Latitud, Longitud;
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

    public double getLatitud() {
        return Latitud;
    }

    public double getLongitud() {
        return Longitud;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getColonia() {
        return Colonia;
    }

    public String getHorarioApertura() {
        return HorarioApertura;
    }

    public String getHorarioCierre() {
        return HorarioCierre;
    }
}