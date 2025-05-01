package analizadorLexico;

import java.util.*;

public class TablaSimbolos {
    private final Deque<Map<String, Simbolo>> pilaDeAmbitos;

    public TablaSimbolos() {
        pilaDeAmbitos = new ArrayDeque<>();
        pilaDeAmbitos.push(new HashMap<>()); // Ámbito global
    }

    // Entra a un nuevo ámbito (por ejemplo, al entrar en una función)
    public void entrarAmbito() {
        pilaDeAmbitos.push(new HashMap<>());
    }

    // Sale del ámbito actual
    public void salirAmbito() {
        if (pilaDeAmbitos.size() > 1) {
            pilaDeAmbitos.pop();
        } else {
            System.out.println("⚠️ Error: No se puede eliminar el ámbito global");
        }
    }

    public void agregarSimbolo(String nombre, String tipoDato, String valor, int linea) {
        Map<String, Simbolo> ambitoActual = pilaDeAmbitos.peek();
        if (!ambitoActual.containsKey(nombre)) {
            Simbolo simbolo = new Simbolo(nombre, tipoDato, valor, linea);
            ambitoActual.put(nombre, simbolo);
            System.out.println("Agregado símbolo: " + nombre + ", Tipo: " + tipoDato);
        } else {
            System.out.println("⚠️ Error: Identificador repetido en el ámbito actual -> '" + nombre + "' en línea " + linea);
        }
    }

    public Simbolo obtenerSimbolo(String nombre) {
        for (Map<String, Simbolo> ambito : pilaDeAmbitos) {
            if (ambito.containsKey(nombre)) {
                return ambito.get(nombre);
            }
        }
        return null;
    }

    public void actualizarValor(String nombre, String nuevoValor) {
        for (Map<String, Simbolo> ambito : pilaDeAmbitos) {
            if (ambito.containsKey(nombre)) {
                Simbolo simbolo = ambito.get(nombre);
                simbolo.setValor(nuevoValor);
                System.out.println("Valor asignado al identificador " + nombre + ": " + nuevoValor);
                return;
            }
        }
        System.out.println("⚠️ Error: Identificador no encontrado -> '" + nombre + "'");
    }

    public boolean existeSimbolo(String nombre) {
        for (Map<String, Simbolo> ambito : pilaDeAmbitos) {
            if (ambito.containsKey(nombre)) {
                return true;
            }
        }
        return false;
    }

    public void mostrarSimbolos() {
        System.out.println("Tabla de Símbolos (desde el ámbito actual hasta el global):");
        int nivel = 0;
        for (Map<String, Simbolo> ambito : pilaDeAmbitos) {
            System.out.println("Ámbito nivel " + nivel++ + ":");
            for (Simbolo simbolo : ambito.values()) {
                System.out.println("  " + simbolo);
            }
        }
    }
}
