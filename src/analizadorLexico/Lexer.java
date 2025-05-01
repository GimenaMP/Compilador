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
                                            null,  // Inicialmente se pone null, ya que el valor se asignará después
                                            numeroLinea
                                    );
                                   // System.out.println("Agregado símbolo: " + lexema + ", Tipo: " + tipoAnterior); // Mensaje de depuración
                                    tipoAnterior = null;
                                } else {
                                    errores.add(new Token(Tipos.ERROR, "Identificador '" + lexema + "' sin tipo de dato en línea " + numeroLinea));
                                }
                            }

                            // Si es signo de igual y hay un identificador antes
                            if (tipo == Tipos.ASIGNADOR_SIMPLE && ultimoIdentificador != null) {
                                int nuevaPos = posicion + lexema.length();
                                String restante = entrada.substring(nuevaPos).trim();

                                // Debug: Imprimir el resto de la cadena después del '='
                              //  System.out.println("Restante después del '=': '" + restante + "'");

                                for (Tipos t : Tipos.values()) {
                                    if (t == Tipos.NUMERO || t == Tipos.IDENTIFICADORES || t == Tipos.CADENA_TEXTO) {
                                        Pattern patValor = Pattern.compile("^" + t.patron);
                                        Matcher mValor = patValor.matcher(restante);

                                        if (mValor.find()) {
                                            String valor = mValor.group();

                                            // Verificar el tipo de valor
                                           // System.out.println("Valor capturado: " + valor);

                                            // Aseguramos que el valor se asigne al símbolo correcto
                                            tablaSimbolos.actualizarValor(ultimoIdentificador, valor);

                                            // Debug: Verificar si el valor se asignó correctamente
                                            //System.out.println("Valor asignado al identificador " + ultimoIdentificador + ": " + valor);

                                            break;
                                        } else {
                                            // Debug: Verificar si el valor no fue encontrado
                                           // System.out.println("No se encontró un valor válido después del '=' para " + ultimoIdentificador);
                                        }
                                    }
                                }
                                ultimoIdentificador = null;  // Resetear el identificador después de asignar el valor
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
