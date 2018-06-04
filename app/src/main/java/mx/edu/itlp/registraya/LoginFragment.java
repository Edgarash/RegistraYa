package mx.edu.itlp.registraya;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import mx.edu.itlp.WebService.WebService;
import mx.edu.itlp.WebService.WebServiceListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener, WebServiceListener {
    EditText Correo;
    EditText Contraseña;
    Button Entrar, Registrar;
    ProgressBar ProgressBar;
    WebService cliente;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View vista;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(OnFragmentInteractionListener listener) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.mListener = listener;
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
        vista = view;
        Correo = vista.findViewById(R.id.email);
        Contraseña = vista.findViewById(R.id.Password);
        Entrar = vista.findViewById(R.id.btnLogin);
        Registrar = vista.findViewById(R.id.btnSignIn);
        ProgressBar = vista.findViewById(R.id.progressBar);
        Entrar.setOnClickListener(this);
        Registrar.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.activity_login, container, false);
        return vista;

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
        if (cliente != null)
            cliente.cancel(true);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == Entrar.getId()) {
            cliente = new WebService(this);
            cliente.HacerLogin(Correo.getText().toString(), Contraseña.getText().toString());
        } else {

        }
    }

    @Override
    public void onIniciar() {
        showLogin(false);
    }

    @Override
    public void onTerminar(Object Resultado) {
        if (Resultado != null) {
            String Res = (String) Resultado;
            if (Res.substring(0, 5).equals("ERROR")) {
                String noError = Res.substring(6, 9);
                String msgError = Res.substring(10);
                Snackbar.make(Registrar, msgError, Snackbar.LENGTH_LONG).show();
                showLogin(true);
            } else {
                //Toast.makeText(getContext(), Res, Toast.LENGTH_LONG).show();
                mListener.onFragmentInteraction(Res);
            }
        } else {
            showLogin(true);
            Snackbar.make(Correo, "No hubo respuesta del servidor", Snackbar.LENGTH_LONG);
        }
    }

    private void showLogin(final boolean Mostrar) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Mostrar) {
                    ProgressBar.setVisibility(View.GONE);
                    Entrar.setVisibility(View.VISIBLE);
                    Registrar.setVisibility(View.VISIBLE);
                } else {
                    ProgressBar.setVisibility(View.VISIBLE);
                    Entrar.setVisibility(View.GONE);
                    Registrar.setVisibility(View.GONE);
                }
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
        void onFragmentInteraction(Object Objeto);
    }
}
