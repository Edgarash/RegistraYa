package mx.edu.itlp.WebService;

public interface WebServiceListener {
    public void onIniciar();
    public void onActualizar();
    public void onError();
    public void onTerminar(Object Resultado);
}
