package mx.edu.itlp.Datos;

import android.graphics.Color;

/**
 * Created by emcg9 on 05/06/2018.
 */

public class Producto {
    private String Nombre;
    private String Descripcion;
    private float Precio;
    private int Tipo;

    public Producto(String Nombre, String Descripcion, float Precio, int Tipo) {
        this.Nombre = Nombre;
        this.Descripcion = Descripcion;
        this.Precio = Precio;
        this.Tipo = Tipo;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public float getPrecio() {
        return Precio;
    }

    public int getColor() {
        return getColor(this.Tipo);
    }

    public static int getColor(int Tipo) {
        int Colorcito = 0;
        switch (Tipo) {
            case 0:
                Colorcito = 0xFFFFC400;
                break;
            case 1:
                Colorcito = Color.RED;
                break;
            case 2:
                Colorcito = Color.GREEN;
                break;
            case 3:
                Colorcito = Color.BLUE;
                break;
            case 4:
                break;
            case 5:
                break;
        }
        return Colorcito;
    }
}
