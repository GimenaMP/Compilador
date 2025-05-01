package analizadorLexico;

import java.util.HashMap;
import java.util.Map;

public class TablaSimbolos {
    private final Map<String, Simbolo> simbolos;

    public TablaSimbolos() {
        simbolos = new HashMap<>();
    }

    // Agrega un nuevo símbolo si no existe
    public void agregarSimbolo(String nombre, String tipoDato, String valor, int linea) {
        System.out.println("Agregando símbolo: Nombre = " + nombre + ", Tipo = " + tipoDato + ", Valor = " + valor); // Depuración
        if (!simbolos.containsKey(nombre)) {
            Simbolo simbolo = new Simbolo(nombre, tipoDato, valor, linea);
            simbolos.put(nombre, simbolo);
        } else {
            System.out.println("⚠️ Error: Identificador repetido -> '" + nombre + "' en línea " + linea);
        }
    }

    // Devuelve un símbolo específico
    public Simbolo obtenerSimbolo(String nombre) {
        return simbolos.get(nombre);
    }

    // Actualiza el valor de un símbolo existente
    public void actualizarValor(String nombre, String nuevoValor) {
        Simbolo simbolo = simbolos.get(nombre);
        if (simbolo != null) {
            simbolo.setValor(nuevoValor);
        }
    }

    // Imprime todos los símbolos registrados
    public void mostrarSimbolos() {
        for (Simbolo simbolo : simbolos.values()) {
            System.out.println("Simbolo: " + simbolo.toString());  // Depuración
        }
    }
}
