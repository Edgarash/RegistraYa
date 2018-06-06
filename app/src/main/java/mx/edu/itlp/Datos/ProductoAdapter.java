package mx.edu.itlp.Datos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.opengl.Visibility;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mx.edu.itlp.registraya.R;
import mx.edu.itlp.registraya.ScrollingRestauranteActivity;

public class ProductoAdapter extends BaseAdapter {
    public List<Producto> Productos;
    private Context Contexto;
    ViewHolder[] Views;
    CuentaTotalListener mListener;

    public ProductoAdapter(List<Producto> Productos, Context Contexto) {
        this.Productos = Productos;
        this.Contexto = Contexto;
        Views = new ViewHolder[Productos.size()];
    }

    public void setCuentaTotalListener(CuentaTotalListener Listener) {
        this.mListener = Listener;
    }

    private float getTotal() {
        float Total = 0;
        for (int i = 0; i < Views.length; i++) {
            ViewHolder t = Views[i];
            if (t != null)
                Total += t.Cantidad * Productos.get(i).getPrecio();
        }
        return Total;
    }

    @Override
    public int getCount() {
        if (Productos != null) return Productos.size();
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        return Productos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Obteniendo Producto
        final Producto temp = Productos.get(position);
        //Haciendo Diseño
        LayoutInflater f = (LayoutInflater) Contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = f.inflate(R.layout.menu_design, null);
        //Valores guardados
        final ViewHolder actual;
        //Valores inmutables
        ((TextView) v.findViewById(R.id.NombreProducto)).setText(temp.getNombre());
        ((TextView) v.findViewById(R.id.DescripcionProducto)).setText(temp.getDescripcion());
        ((TextView) v.findViewById(R.id.Precio)).setText(String.format("$%,.2f", temp.getPrecio()));
        ImageView Tipo = v.findViewById(R.id.tipo);
        Tipo.setBackgroundColor(temp.getColor());
        //TextView Mutable
        final TextView
                SubTotal = v.findViewById(R.id.subTotal);
        final TextView
                Cantidad = v.findViewById(R.id.Cantidad);
        final Button
                btnMinus = v.findViewById(R.id.btnMinus),
                btnPlus = v.findViewById(R.id.btnPlus);
        //Asignacion en caso de nulo
        actual = Views[position] = Views[position] == null ? new ViewHolder() : Views[position];
        //Guardar Precio
        Cantidad.setText(String.valueOf(actual.Cantidad));
        SubTotal.setText(String.format("$%,.2f", actual.Cantidad * temp.getPrecio()));
        //Eventos para Botones;
        View.OnClickListener inter = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Cant = Integer.valueOf(Cantidad.getText().toString());
                if (v.getId() == btnMinus.getId())
                    Cant -= Cant > 0 ? 1 : 0;
                else
                    Cant += Cant < 99 ? 1 : 0;
                actual.Cantidad = Cant;
                Cantidad.setText(String.valueOf(Cant));
                SubTotal.setText(String.format("$%,.2f", temp.getPrecio() * actual.Cantidad));
                mListener.OnCuentaTotal(getTotal());
            }
        };
        btnMinus.setOnClickListener(inter);
        btnPlus.setOnClickListener(inter);

        return v;
    }

    private class ViewHolder {
        int Cantidad = 0;
    }

    public interface CuentaTotalListener {
        void OnCuentaTotal(float Total);
    }
}