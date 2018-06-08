package mx.edu.itlp.Datos;

import java.net.IDN;

/**
 * Created by emcg9 on 08/06/2018.
 */

public class Reservacion {
    private String ID, Fecha, Mesa, Restaurante, Colonia;

    public Reservacion(String ID, String Fecha, String Mesa, String Restaurante, String Colonia) {
        this.ID = ID;
        this.Fecha = Fecha;
        this.Mesa = Mesa;
        this.Restaurante = Restaurante;
        this.Colonia = Colonia;
    }

    public String getID() {
        return ID;
    }

    public String getFecha() {
        return Fecha;
    }

    public String getMesa() {
        return Mesa;
    }

    public String getRestaurante() {
        return Restaurante;
    }

    public String getColonia() {
        return Colonia;
    }
}
