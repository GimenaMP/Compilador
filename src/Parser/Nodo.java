package Parser;

import java.util.ArrayList;
import java.util.List;

public class Nodo {
    private final String etiqueta;
    private String valor;
    private final List<Nodo> hijos;

    public Nodo(String etiqueta) {
        this.etiqueta = etiqueta;
        this.hijos = new ArrayList<>();
    }

    public Nodo(String etiqueta, String valor) {
        this(etiqueta);
        this.valor = valor;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public String getValor() {
        return valor;
    }

    public List<Nodo> getHijos() {
        return hijos;
    }

    public void agregarHijo(Nodo hijo) {
        hijos.add(hijo);
    }

    @Override
    public String toString() {
        return valor != null ? etiqueta + ": " + valor : etiqueta;
    }
}