package mx.edu.itlp.Datos;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mx.edu.itlp.registraya.R;
import mx.edu.itlp.registraya.ScrollingRestauranteActivity;

public class ReservacionAdapter extends BaseAdapter {
    public List<Reservacion> Reservaciones;
    private Context Contexto;

    public ReservacionAdapter(List<Reservacion> Reservaciones, Context Contexto) {
        this.Reservaciones = Reservaciones;
        this.Contexto = Contexto;
    }

    @Override
    public int getCount() {
        if (Reservaciones != null) return Reservaciones.size();
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        return Reservaciones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Reservacion temp = Reservaciones.get(position);
        LayoutInflater f = (LayoutInflater) Contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = f.inflate(R.layout.layout_reservacion, null);
        ((TextView) v.findViewById(R.id.NombreRestaurante)).setText(temp.getRestaurante());
        ((TextView) v.findViewById(R.id.ColoniaRestaurante)).setText(temp.getColonia());
        ((TextView) v.findViewById(R.id.NombreRestaurante)).setText(temp.getRestaurante());
        String Fechita = temp.getFecha().substring(0, 10);
        String Horita = temp.getFecha().substring(11, 19);
        Calendar Fecha = Calendar.getInstance();
        Fecha.set(Calendar.YEAR, Integer.valueOf(Fechita.substring(0, 4)));
        Fecha.set(Calendar.MONTH, Integer.valueOf(Fechita.substring(6, 7)));
        Fecha.set(Calendar.DATE, Integer.valueOf(Fechita.substring(9, 10)));
        ((TextView) v.findViewById(R.id.Fecha)).setText("Fecha: " + Fechita + " (" + getDayOfWeek(Fecha.get(Calendar.DAY_OF_WEEK)) + ")");
        ((TextView) v.findViewById(R.id.Hora)).setText("Hora: " + Horita);
        ((TextView) v.findViewById(R.id.Mesa)).setText("Mesa para " + temp.getMesa() + " persona" + (Integer.valueOf(temp.getMesa()) > 1 ? "s" : ""));
        return v;
    }

    String getDayOfWeek(int Day) {
        switch (Day) {
            case Calendar.MONDAY:
                return "Lunes";
            case Calendar.TUESDAY:
                return "Martes";
            case Calendar.WEDNESDAY:
                return "Miércoles";
            case Calendar.THURSDAY:
                return "Jueves";
            case Calendar.FRIDAY:
                return "Viernes";
            case Calendar.SATURDAY:
                return "Sábado";
            case Calendar.SUNDAY:
                return "Domingo";
            default:
                return "";
        }
    }
}
