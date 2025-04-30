package analizadorLexico;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    public static void lex(String entrada) {
        ArrayList<Token> tokens = new ArrayList<>();
        ArrayList<Token> errores = new ArrayList<>();
        int posicion = 0;

        while (posicion < entrada.length()) {
            boolean encontrado = false;

            for (Tipos tipo : Tipos.values()) {
                Pattern patron = Pattern.compile("^" + tipo.patron);
                Matcher matcher = patron.matcher(entrada.substring(posicion));

                if (matcher.find()) {
                    String lexema = matcher.group();

                    // Ignorar espacios
                    if (tipo != Tipos.ESPACIOS) {
                        // Identificar errores por nombre del tipo
                        if (tipo.name().contains("INVALIDO") || tipo == Tipos.ERROR) {
                            errores.add(new Token(tipo, lexema));
                        } else {
                            tokens.add(new Token(tipo, lexema));
                        }
                    }

                    posicion += lexema.length();
                    encontrado = true;
                    break;
                }
            }

            // Si ningún patrón coincide
            if (!encontrado) {
                errores.add(new Token(Tipos.ERROR, String.valueOf(entrada.charAt(posicion))));
                posicion++;
            }
        }

        // Mostrar tablas separadas
        System.out.println("======= TOKENS VÁLIDOS =======");
        tokens.forEach(System.out::println);

        System.out.println("\n======= ERRORES DETECTADOS =======");
        errores.forEach(System.out::println);
    }

}
