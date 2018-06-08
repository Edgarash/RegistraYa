package mx.edu.itlp.registraya;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import mx.edu.itlp.Datos.Reservacion;
import mx.edu.itlp.Datos.ReservacionAdapter;
import mx.edu.itlp.Datos.Restaurante;
import mx.edu.itlp.Datos.RestauranteAdapter;
import mx.edu.itlp.Datos.Sesion;
import mx.edu.itlp.WebService.WebService;
import mx.edu.itlp.WebService.WebServiceListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReservacionesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReservacionesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservacionesFragment extends Fragment implements WebServiceListener, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    WebService Cliente;
    ConstraintLayout btnReintentarParent;
    Button btnReintentar;
    ListView ListaDeReervaciones;
    ProgressBar Barrita;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ReservacionesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReservacionesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReservacionesFragment newInstance(String param1, String param2) {
        ReservacionesFragment fragment = new ReservacionesFragment();
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
        ListaDeReervaciones = view.findViewById(R.id.Restaurantes);
        Barrita = view.findViewById(R.id.progressBar);
        btnReintentarParent = view.findViewById(R.id.btnReintentarContenedor);
        btnReintentar = view.findViewById(R.id.btnReintenetar);
        ListaDeReervaciones.setEmptyView(btnReintentarParent);
        btnReintentar.setOnClickListener(this);
        BuscarReservaciones();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reservaciones, container, false);
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
        if (Cliente != null)
            Cliente.cancel(true);
    }

    @Override
    public void onIniciar() {
        showReintentar(false);
    }

    @Override
    public void onTerminar(Object Resultado) {
        if (Resultado != null) {
            String Res = (String) Resultado;
            if (Res.equals("SIN RESERVACIONES")) {
                //Mostrar que no tiene reservaciones
                mostrarSinReservacion();
            } else {
                Gson gson = new Gson();
                Type tipoListaReservaciones = new TypeToken<List<Reservacion>>() {
                }.getType();
                List<Reservacion> res = gson.fromJson((String) Resultado, tipoListaReservaciones);
                ReservacionAdapter AdaptadorRes = new ReservacionAdapter(res, getContext());
                ListaDeReervaciones.setAdapter(AdaptadorRes);
            }
            showReintentar(false);
        } else {
            Snackbar.make(ListaDeReervaciones, "Hubo un error al conectar con la base de datos", Snackbar.LENGTH_LONG).show();
            showReintentar(true);
        }
    }

    @Override
    public void onClick(View v) {
        BuscarReservaciones();
    }

    private void BuscarReservaciones() {
        if (Sesion.isLoggedIn()) {
            Cliente = new WebService(this);
            Cliente.getReservaciones(Sesion.getUsuario().getCorreo());
        } else {
            Toast.makeText(getContext(), "No ha iniciado sesión", Toast.LENGTH_SHORT).show();
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

    private void mostrarSinReservacion() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Barrita.setVisibility(View.GONE);
                btnReintentar.setVisibility(View.GONE);
                ((TextView) getActivity().findViewById(R.id.NoReservacion)).setText("NO TIENE RESERVACIONES");
            }
        });
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
