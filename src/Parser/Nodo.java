package Parser;

import java.util.ArrayList;
import java.util.List;

public class Nodo {
    private final String etiqueta;
    private final List<Nodo> hijos;

    public Nodo(String etiqueta) {
        this.etiqueta = etiqueta;
        this.hijos = new ArrayList<>();
    }

    public void agregarHijo(Nodo hijo) {
        hijos.add(hijo);
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public List<Nodo> getHijos() {
        return hijos;
    }
}