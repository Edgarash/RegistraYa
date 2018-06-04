package mx.edu.itlp.registraya;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import mx.edu.itlp.Datos.Restaurante;
import mx.edu.itlp.Datos.RestauranteAdapter;
import mx.edu.itlp.WebService.WebService;
import mx.edu.itlp.WebService.WebServiceListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link getRestaurantes.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link getRestaurantes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class getRestaurantes extends Fragment implements WebServiceListener, View.OnClickListener {
    View vista;
    ConstraintLayout btnReintentarParent;
    Button btnReintentar;
    ListView ListaDeRestaurantes;
    ProgressBar Barrita;
    WebService Cliente;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public getRestaurantes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment getRestaurantes.
     */
    // TODO: Rename and change types and number of parameters
    public static getRestaurantes newInstance(String param1, String param2) {
        getRestaurantes fragment = new getRestaurantes();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View v = view;
        ListaDeRestaurantes = v.findViewById(R.id.Restaurantes);
        Barrita = v.findViewById(R.id.progressBar);
        btnReintentarParent = v.findViewById(R.id.btnReintentarContenedor);
        btnReintentar = v.findViewById(R.id.btnReintenetar);
        ListaDeRestaurantes.setEmptyView(btnReintentarParent);
        btnReintentar.setOnClickListener(this);
        BuscarRestaurantes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.activity_main, container, false);
        return vista;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        Cliente.cancel(true);
    }

    public void BuscarRestaurantes() {
        Cliente = new WebService(this);
        Cliente.obtenerRestaurante();
    }

    @Override
    public void onIniciar() {
        showReintentar(false);
    }

    @Override
    public void onTerminar(Object Resultado) {
        if (Resultado != null) {
            Gson gson = new Gson();
            Type tipoListaRestaurante = new TypeToken<List<Restaurante>>() {
            }.getType();
            List<Restaurante> res = gson.fromJson((String) Resultado, tipoListaRestaurante);
            RestauranteAdapter AdaptadorRes = new RestauranteAdapter(res, getContext());
            ListaDeRestaurantes.setAdapter(AdaptadorRes);
        } else {
            Snackbar.make(ListaDeRestaurantes, "Hubo un error al conectar con la base de datos", Snackbar.LENGTH_INDEFINITE).show();
            showReintentar(true);
        }
    }



    private void showReintentar(final boolean Mostrar) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean x = Mostrar;
                if (x) {
                    Barrita.setVisibility(View.GONE);
                    btnReintentar.setVisibility(View.VISIBLE);
                } else {
                    Barrita.setVisibility(View.VISIBLE);
                    btnReintentar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        BuscarRestaurantes();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
