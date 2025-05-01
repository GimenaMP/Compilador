package Parser;

import analizadorLexico.Token;
import analizadorLexico.Tipos;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int posicion;
    private final List<ErrorSintactico> errores;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.posicion = 0;
        this.errores = new ArrayList<>();
    }

    public Nodo parse() {
        Nodo raiz = new Nodo("Programa");
        while (posicion < tokens.size()) {
            Token tokenActual = tokens.get(posicion);
            if (tokenActual.getTipo() == Tipos.TIPO) {
                raiz.agregarHijo(parseDeclaracion());
            } else {
                errores.add(new ErrorSintactico("Se esperaba un tipo", tokenActual));
                posicion++;
            }
        }
        return raiz;
    }

    private Nodo parseDeclaracion() {
        Nodo declaracion = new Nodo("Declaracion");
        Token tipoToken = tokens.get(posicion++);
        declaracion.agregarHijo(new Nodo(tipoToken.getValor()));

        if (posicion < tokens.size() && tokens.get(posicion).getTipo() == Tipos.IDENTIFICADORES) {
            Token idToken = tokens.get(posicion++);
            declaracion.agregarHijo(new Nodo(idToken.getValor()));
        } else {
            errores.add(new ErrorSintactico("Se esperaba un identificador", tokens.get(posicion)));
            posicion++;
        }

        return declaracion;
    }

    public List<ErrorSintactico> getErrores() {
        return errores;
    }

    public static void main(String[] args) {
        // Ejemplo de uso
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token(Tipos.TIPO, "int"));
        tokens.add(new Token(Tipos.IDENTIFICADORES, "x"));

        Parser parser = new Parser(tokens);
        Nodo arbolSintactico = parser.parse();

        if (!parser.getErrores().isEmpty()) {
            for (ErrorSintactico error : parser.getErrores()) {
                System.out.println(error);
            }
        } else {
            System.out.println("Análisis sintáctico exitoso");
        }
    }
    public void mostrarErrores() {
        if (errores.isEmpty()) {
            System.out.println("No se encontraron errores sintácticos.");
        } else {
            for (ErrorSintactico error : errores) {
                System.out.println(error);
            }
        }
    }
    public void agregarError(ErrorSintactico error) {
        errores.add(error);
    }
    public void limpiarErrores() {
        errores.clear();
    }


    public void agregarErrorSintactico(String mensaje, Token token) {
        errores.add(new ErrorSintactico(mensaje, token));
    }

    public List<Token> getTokens() {
        return tokens;
    }
    public int getPosicion() {
        return posicion;
    }
    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }
    public void avanzarPosicion() {
        posicion++;
    }
    public Token getTokenActual() {
        if (posicion < tokens.size()) {
            return tokens.get(posicion);
        }
        return null;  // o lanzar una excepción
    }
    public Token getTokenSiguiente() {
        if (posicion + 1 < tokens.size()) {
            return tokens.get(posicion + 1);
        }
        return null;  // o lanzar una excepción
    }
    public boolean hayMasTokens() {
        return posicion < tokens.size();
    }
    public void reiniciarPosicion() {
        posicion = 0;
    }
    public void avanzar(int pasos) {
        posicion += pasos;
    }
    public void retroceder(int pasos) {
        posicion -= pasos;
        if (posicion < 0) {
            posicion = 0;  // o lanzar una excepción
        }
    }
    public void agregarToken(Token token) {
        tokens.add(token);
    }
    public void eliminarToken(int index) {
        if (index >= 0 && index < tokens.size()) {
            tokens.remove(index);
        } else {
            System.out.println("Índice fuera de rango");
        }
    }
    public void limpiarTokens() {
        tokens.clear();
    }
    public int getNumeroTokens() {
        return tokens.size();
    }
    public Token getToken(int index) {
        if (index >= 0 && index < tokens.size()) {
            return tokens.get(index);
        }
        return null;  // o lanzar una excepción
    }
    public void setToken(int index, Token token) {
        if (index >= 0 && index < tokens.size()) {
            tokens.set(index, token);
        } else {
            System.out.println("Índice fuera de rango");
        }
    }
    public void agregarErrorSintactico(String mensaje, Token token) {
        errores.add(new ErrorSintactico(mensaje, token));
    }


}