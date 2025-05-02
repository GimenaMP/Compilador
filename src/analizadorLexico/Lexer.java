package analizadorLexico;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    public static List<Token> lex(String entrada) {
        ArrayList<Token> tokens = new ArrayList<>();           // Lista para tokens válidos
        ArrayList<Token> errores = new ArrayList<>();          // Lista para tokens erróneos
        TablaSimbolos tablaSimbolos = new TablaSimbolos();     // Tabla de símbolos

        int posicion = 0;          // Posición actual en la entrada
        int numeroLinea = 1;       // Número de línea actual
        String tipoAnterior = null;         // Tipo de dato encontrado antes de un identificador
        String ultimoIdentificador = null;  // Último identificador encontrado

        // Recorre la entrada carácter por carácter
        while (posicion < entrada.length()) {
            boolean encontrado = false;  // Marca si se encontró un token válido

            // Recorre todos los tipos definidos en el enum Tipos
            for (Tipos tipo : Tipos.values()) {
                Pattern patron = Pattern.compile("^" + tipo.patron); // Patrón regex para el tipo actual
                Matcher matcher = patron.matcher(entrada.substring(posicion));

                if (matcher.find()) {
                    String lexema = matcher.group();  // Lexema encontrado

                    if (tipo != Tipos.ESPACIOS) {
                        if (lexema.equals("\n")) numeroLinea++;  // Aumenta línea si hay salto

                        if (tipo.name().contains("INVALIDO") || tipo == Tipos.ERROR) {
                            // Agrega a errores si es inválido
                            errores.add(new Token(tipo, lexema));
                        } else {
                            // Agrega a tokens válidos
                            tokens.add(new Token(tipo, lexema));

                            // Guarda el tipo anterior si es tipo de dato
                            if (tipo == Tipos.TIPO_ENTERO || tipo == Tipos.TIPO_DECIMAL || tipo == Tipos.TIPO_CADENA) {
                                tipoAnterior = lexema;
                            }

                            // Si es un identificador, verifica si tiene tipo anterior
                            // En el método lex(), modifica la condición para identificadores:
                            // Por esto:
                            // Modifica la condición para identificadores:
                            if (tipo == Tipos.IDENTIFICADORES) {
                                if (tipoAnterior != null) {
                                    tablaSimbolos.agregarSimbolo(lexema, tipoAnterior, null, numeroLinea);
                                    ultimoIdentificador = lexema;
                                    tipoAnterior = null;
                                } else if (!tablaSimbolos.existeSimbolo(lexema)) {
                                    // Verificar si es un nombre de función (token anterior es FUNCION o estamos en parámetros)
                                    boolean esNombreFuncion = !tokens.isEmpty() &&
                                            (tokens.get(tokens.size() - 1).getTipo() == Tipos.FUNCION ||
                                                    tokens.get(tokens.size() - 1).getValor().equals("("));

                                    if (!esNombreFuncion) {
                                        errores.add(new Token(Tipos.ERROR, "Identificador '" + lexema + "' sin tipo de dato en línea " + numeroLinea));
                                    }
                                }
                                ultimoIdentificador = lexema;
                            }


                            // Si es un signo de asignación, buscar el valor a la derecha
                            if (tipo == Tipos.ASIGNADOR_SIMPLE && ultimoIdentificador != null) {
                                int nuevaPos = posicion + lexema.length();
                                String restante = entrada.substring(nuevaPos).trim();  // lo que hay después del '='

                                System.out.println("Restante después del '=': '" + restante + "'");

                                // Buscar el valor asignado (cadena, número o identificador)
                                for (Tipos t : Tipos.values()) {
                                    if (t == Tipos.NUMERO || t == Tipos.IDENTIFICADORES || t == Tipos.CADENA_TEXTO) {
                                        Matcher mValor = Pattern.compile("^" + t.patron).matcher(restante);

                                        if (mValor.find()) {
                                            String valor = mValor.group();
                                            System.out.println("Valor capturado: " + valor);

                                            // 🔽 Aquí se limpian las comillas si es una cadena
                                            if (t == Tipos.CADENA_TEXTO && valor.length() >= 2 &&
                                                    valor.startsWith("\"") && valor.endsWith("\"")) {
                                                valor = valor.substring(1, valor.length() - 1);  // elimina comillas
                                            }

                                            // Se actualiza el valor del identificador en la tabla
                                            tablaSimbolos.actualizarValor(ultimoIdentificador, valor);
                                            break;
                                        }
                                    }
                                }
                                // Se reinicia el identificador porque ya fue asignado
                                ultimoIdentificador = null;
                            }
                        }
                    }

                    posicion += lexema.length();  // Avanza la posición
                    encontrado = true;
                    break;
                }
            }

            // Si no se encontró ningún token válido, se registra como error
            if (!encontrado) {
                errores.add(new Token(Tipos.ERROR, String.valueOf(entrada.charAt(posicion))));
                posicion++;
            }
        }

        // Imprimir resultados
        System.out.println("======= TOKENS VÁLIDOS =======");
        tokens.forEach(System.out::println);

        System.out.println("\n======= ERRORES DETECTADOS =======");
        errores.forEach(System.out::println);

        System.out.println("\n======= TABLA DE SÍMBOLOS =======");
        tablaSimbolos.mostrarSimbolos();
        return tokens.isEmpty() ? new ArrayList<>() : tokens;
    }
}

