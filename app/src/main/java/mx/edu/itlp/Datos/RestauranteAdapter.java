package mx.edu.itlp.Datos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import mx.edu.itlp.registraya.MapsActivity;
import mx.edu.itlp.registraya.R;
import mx.edu.itlp.registraya.ScrollingRestauranteActivity;

public class RestauranteAdapter extends BaseAdapter {
    public List<Restaurante> Restaurantes;
    private Context Contexto;

    public RestauranteAdapter(List<Restaurante> Restaurantes, Context Contexto) {
        this.Restaurantes = Restaurantes;
        this.Contexto = Contexto;
    }

    @Override
    public int getCount() {
        if (Restaurantes!=null) return Restaurantes.size();
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        return Restaurantes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater f = (LayoutInflater) Contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = f.inflate(R.layout.layout_restaurante, null);
        TextView
                Nombre = v.findViewById(R.id.tvNombreRestaurante),
                Colonia = v.findViewById(R.id.tvColoniaRestaurante),
                Horario = v.findViewById(R.id.tvHorarioRestaurante);
        final Restaurante temp = Restaurantes.get(position);
        Nombre.setText(temp.Nombre);
        Colonia.setText(temp.Colonia);
        Horario.setText(
                "Abre a las " + temp.HorarioApertura.substring(0,5) + ", Cierra a las " + temp.HorarioCierre.substring(0,5)
        );
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ScrollingRestauranteActivity.class);
                intent.putExtra("Restaurante", temp);
                v.getContext().startActivity(intent);
            }
        });
        return v;
    }
}
