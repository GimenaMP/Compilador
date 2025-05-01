package analizadorLexico;

import java.util.HashMap;
import java.util.Map;

public class TablaSimbolos {
    private final Map<String, Simbolo> simbolos;

    public TablaSimbolos() {
        simbolos = new HashMap<>();
    }

    public void agregarSimbolo(String nombre, String tipoDato, String valor, int linea) {
        if (!simbolos.containsKey(nombre)) {
            Simbolo simbolo = new Simbolo(nombre, tipoDato, valor, linea);
            simbolos.put(nombre, simbolo);
            System.out.println("Agregado símbolo: " + nombre + ", Tipo: " + tipoDato);
        } else {
            System.out.println("⚠️ Error: Identificador repetido -> '" + nombre + "' en línea " + linea);
        }
    }

    public Simbolo obtenerSimbolo(String nombre) {
        return simbolos.get(nombre);
    }

    public void actualizarValor(String nombre, String nuevoValor) {
        Simbolo simbolo = simbolos.get(nombre);
        if (simbolo != null && nuevoValor != null) {
            simbolo.setValor(nuevoValor);
            System.out.println("Valor asignado al identificador " + nombre + ": " + nuevoValor);
        }
    }

    public void mostrarSimbolos() {
        if (simbolos.isEmpty()) {
            System.out.println("(No se encontraron símbolos)");
        }
        for (Simbolo simbolo : simbolos.values()) {
            System.out.println("Simbolo: " + simbolo);
        }
    }
}
