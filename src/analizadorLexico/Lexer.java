package analizadorLexico;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    public static void lex(String entrada) {
        ArrayList<Token> tokens = new ArrayList<>();
        ArrayList<Token> errores = new ArrayList<>();
        TablaSimbolos tablaSimbolos = new TablaSimbolos();

        int posicion = 0;
        int numeroLinea = 1;
        String tipoAnterior = null;
        String ultimoIdentificador = null;

        while (posicion < entrada.length()) {
            boolean encontrado = false;

            for (Tipos tipo : Tipos.values()) {
                Pattern patron = Pattern.compile("^" + tipo.patron);
                Matcher matcher = patron.matcher(entrada.substring(posicion));

                if (matcher.find()) {
                    String lexema = matcher.group();

                    if (tipo != Tipos.ESPACIOS) {
                        if (lexema.equals("\n")) {
                            numeroLinea++;
                        }

                        if (tipo.name().contains("INVALIDO") || tipo == Tipos.ERROR) {
                            errores.add(new Token(tipo, lexema));
                        } else {
                            tokens.add(new Token(tipo, lexema));

                            // Guardar si es tipo de dato
                            if (tipo == Tipos.TIPO_ENTERO || tipo == Tipos.TIPO_DECIMAL || tipo == Tipos.TIPO_CADENA) {
                                tipoAnterior = lexema;
                            }

                            // Si es identificador válido
                            if (tipo == Tipos.IDENTIFICADORES) {
                                ultimoIdentificador = lexema;

                                if (tipoAnterior != null) {
                                    tablaSimbolos.agregarSimbolo(
                                            lexema,
                                            tipoAnterior,
                                            null,
                                            numeroLinea
                                    );
                                    tipoAnterior = null;
                                } else {
                                    errores.add(new Token(Tipos.ERROR, "Identificador '" + lexema + "' sin tipo de dato en línea " + numeroLinea));
                                }
                            }

                            // Si es signo de igual y hay un identificador antes
                            if (tipo == Tipos.ASIGNADOR_SIMPLE && ultimoIdentificador != null) {
                                int nuevaPos = posicion + lexema.length();
                                String restante = entrada.substring(nuevaPos).trim();

                                for (Tipos t : Tipos.values()) {
                                    if (t == Tipos.NUMERO || t == Tipos.IDENTIFICADORES || t == Tipos.TIPO_CADENA) {
                                        Pattern patValor = Pattern.compile("^" + t.patron);
                                        Matcher mValor = patValor.matcher(restante);

                                        if (mValor.find()) {
                                            String valor = mValor.group();
                                            tablaSimbolos.actualizarValor(ultimoIdentificador, valor);
                                            break;
                                        }
                                    }
                                }
                                ultimoIdentificador = null;
                            }
                        }
                    }

                    posicion += lexema.length();
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                errores.add(new Token(Tipos.ERROR, String.valueOf(entrada.charAt(posicion))));
                posicion++;
            }
        }

        // Mostrar resultados
        System.out.println("======= TOKENS VÁLIDOS =======");
        tokens.forEach(System.out::println);

        System.out.println("\n======= ERRORES DETECTADOS =======");
        errores.forEach(System.out::println);

        System.out.println("\n======= TABLA DE SÍMBOLOS =======");
        tablaSimbolos.mostrarSimbolos();
    }
}

