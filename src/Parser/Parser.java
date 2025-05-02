package Parser;

import java.util.ArrayList;
import java.util.List;
import analizadorLexico.Token;
import analizadorLexico.Tipos;

public class Parser {
    private final List<Token> tokens;
    private int posicionActual = 0;
    private final List<String> errores = new ArrayList<>();
    private Nodo arbol;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }
// Método principal para iniciar el análisis sintáctico
    public Nodo parsear() {
        arbol = new Nodo("programa");
        programa();
        return arbol;
    }

    // Método para obtener el token actual
    private Token tokenActual() {
        if (posicionActual < tokens.size()) {
            return tokens.get(posicionActual);
        }
        return null;
    }
// Método para consumir un token y avanzar a la siguiente posición
    private Token consumir(Tipos tipoEsperado, String mensajeError) {
        Token token = tokenActual();
        if (token != null && token.getTipo() == tipoEsperado) {
            posicionActual++;
            return token;
        }
        errores.add("Error sintáctico en línea " + (token != null ? "..." : "final") +
                ": " + mensajeError + ". Se encontró: " +
                (token != null ? token.getValor() : "EOF"));
        return null;
    }

    private boolean coincide(Tipos tipo) {
        Token token = tokenActual();
        return token != null && token.getTipo() == tipo;
    }

    private boolean coincideValor(String valor) {
        Token token = tokenActual();
        return token != null && token.getValor().equals(valor);
    }
// Método para procesar el programa completo
    private void programa() {
        consumir(Tipos.INICIO_PROGRAMA, "Se esperaba 'Kilala' al inicio del programa");

        Nodo nodoInstrucciones = new Nodo("instrucciones");
        arbol.agregarHijo(nodoInstrucciones);

        while (!coincide(Tipos.CIERRE)) {
            instruccion(nodoInstrucciones);
        }

        consumir(Tipos.CIERRE, "Se esperaba 'Fin' al final del programa");
    }


    // Método para procesar instrucciones
    private void instruccion(Nodo padre) {
        Token token = tokenActual();
        if (token == null) return;

        Nodo nodoInstruccion = new Nodo("instruccion");
        padre.agregarHijo(nodoInstruccion);

        switch (token.getTipo()) {
            case DECLARAR_VARIABLE:
                declaracion_variable(nodoInstruccion);
                break;
            case DECLARAR_CONSTANTE:
                declaracion_constante(nodoInstruccion);
                break;
            case FUNCION:
                funcion_declaracion(nodoInstruccion);
                break;
            case SI:
                condicional(nodoInstruccion);
                break;
            case SINO_SI:
                condicional_sino(nodoInstruccion);
                break;
            case MIENTRAS:
                bucle_while(nodoInstruccion);
                break;
            case PARA:
                bucle_for(nodoInstruccion);
                break;
            case OPERACION:
                operacion(nodoInstruccion);
                break;
            case RETORNAR:
                retornar(nodoInstruccion);
                break;
            case DETENER:
                detener(nodoInstruccion);
                break;
            case IDENTIFICADORES:
                if (coincideValor("=") || (posicionActual + 1 < tokens.size() &&
                        tokens.get(posicionActual + 1).getValor().equals("="))) {
                    asignacion(nodoInstruccion);
                } else if (coincide(Tipos.OPERADORES) || coincide(Tipos.SEPARADORES)) {
                    errores.add("Error sintáctico: Operación incompleta o mal formada que comienza con '" + token.getValor() + "'");
                    posicionActual++;
                } else {
                    errores.add("Error sintáctico: Instrucción no reconocida que comienza con '" + token.getValor() + "'");
                    posicionActual++;
                }
                break;
            case COMENTARIO_LINEA:
            case COMENTARIO_BLOQUE:
                posicionActual++;
                padre.getHijos().remove(nodoInstruccion);
                break;
            default:
                if (token.getTipo() == Tipos.SEPARADORES && !token.getValor().equals(";")) {
                    // Solo consumir separadores que no sean punto y coma
                    posicionActual++;
                    padre.getHijos().remove(nodoInstruccion);
                } else {
                    errores.add("Error sintáctico: Instrucción no reconocida que comienza con '" + token.getValor() + "'");
                    posicionActual++;
                    padre.getHijos().remove(nodoInstruccion);
                }
        }

        // Verificar si el nodo de instrucción quedó vacío
        if (nodoInstruccion.getHijos().isEmpty()) {
            padre.getHijos().remove(nodoInstruccion);
        }
    }

    private void declaracion_variable(Nodo padre) {
        Nodo nodoDeclaracion = new Nodo("declaracion_variable");
        padre.agregarHijo(nodoDeclaracion);

        consumir(Tipos.DECLARAR_VARIABLE, "Se esperaba 'Bakusaiga'");
        tipo(nodoDeclaracion);
        identificador(nodoDeclaracion);
        consumir(Tipos.ASIGNADOR_SIMPLE, "Se esperaba '='");
        valor(nodoDeclaracion);
        consumir(Tipos.SEPARADORES, "Se esperaba ';'");
    }

    private void declaracion_constante(Nodo padre) {
        Nodo nodoDeclaracion = new Nodo("declaracion_constante");
        padre.agregarHijo(nodoDeclaracion);

        consumir(Tipos.DECLARAR_CONSTANTE, "Se esperaba 'Tetsusaiga'");
        tipo(nodoDeclaracion);

        identificador(nodoDeclaracion);
        consumir(Tipos.ASIGNADOR_SIMPLE, "Se esperaba '='");
        valor(nodoDeclaracion);
        consumir(Tipos.SEPARADORES, "Se esperaba ';'");
    }

    private void asignacion(Nodo padre) {
        Nodo nodoAsignacion = new Nodo("asignacion");
        padre.agregarHijo(nodoAsignacion);

        identificador(nodoAsignacion);
        consumir(Tipos.ASIGNADOR_SIMPLE, "Se esperaba '='");
        valor(nodoAsignacion);
        consumir(Tipos.SEPARADORES, "Se esperaba ';'");
    }

    private void tipo(Nodo padre) {
        Token token = tokenActual();
        if (token != null && (token.getTipo() == Tipos.TIPO_ENTERO ||
                token.getTipo() == Tipos.TIPO_DECIMAL ||
                token.getTipo() == Tipos.TIPO_CADENA)) {
            Nodo nodoTipo = new Nodo("tipo", token.getValor());
            padre.agregarHijo(nodoTipo);
            posicionActual++;
        } else {
            errores.add("Error sintáctico: Se esperaba un tipo de dato (Miroku, Sesshomaru o Sango)");
        }
    }

    private void identificador(Nodo padre) {
        Token token = tokenActual();
        if (token != null && token.getTipo() == Tipos.IDENTIFICADORES) {
            Nodo nodoIdentificador = new Nodo("identificador", token.getValor());
            padre.agregarHijo(nodoIdentificador);
            posicionActual++;
        } else {
            errores.add("Error sintáctico: Se esperaba un identificador");
        }
    }

    private void valor(Nodo padre) {
        Nodo nodoValor = new Nodo("valor");
        padre.agregarHijo(nodoValor);

        // Manejar valores simples
        Token token = tokenActual();
        if (token == null) {
            errores.add("Error sintáctico: Se esperaba un valor");
            return;
        }

        if (token.getTipo() == Tipos.NUMERO || token.getTipo() == Tipos.CADENA_TEXTO) {
            Nodo nodoTerminal = new Nodo(token.getTipo().name().toLowerCase(), token.getValor());
            nodoValor.agregarHijo(nodoTerminal);
            posicionActual++;
        }
        else if (token.getTipo() == Tipos.IDENTIFICADORES) {
            identificador(nodoValor);
        }
        else if (token.getValor().equals("(")) {
            consumir(Tipos.SEPARADORES, "Se esperaba '('");
            expresion_aritmetica(nodoValor);
            consumir(Tipos.SEPARADORES, "Se esperaba ')'");
        }

        // Manejar operaciones después del valor inicial
        while (coincide(Tipos.OPERADORES)) {
            operador_aritmetico(nodoValor);
            valor(nodoValor); // Llamada recursiva
        }
    }

    private void funcion_declaracion(Nodo padre) {
        Nodo nodoFuncion = new Nodo("funcion_declaracion");
        padre.agregarHijo(nodoFuncion);

        consumir(Tipos.FUNCION, "Se esperaba 'Shippo'");
        identificador(nodoFuncion);
        consumir(Tipos.SEPARADORES, "Se esperaba '('");

        Nodo nodoParametro = new Nodo("parametro");
        nodoFuncion.agregarHijo(nodoParametro);

        if (!coincideValor(")")) {
            tipo(nodoParametro);
            identificador(nodoParametro);
        }

        consumir(Tipos.SEPARADORES, "Se esperaba ')'");
        consumir(Tipos.SEPARADORES, "Se esperaba '{'");

        Nodo nodoInstrucciones = new Nodo("instrucciones");
        nodoFuncion.agregarHijo(nodoInstrucciones);

        while (!coincideValor("}")) {
            instruccion(nodoInstrucciones);
        }

        consumir(Tipos.SEPARADORES, "Se esperaba '}'");
    }

    private void condicional(Nodo padre) {
        Nodo nodoCondicional = new Nodo("condicional");
        padre.agregarHijo(nodoCondicional);

        consumir(Tipos.SI, "Se esperaba 'Kagome'");
        consumir(Tipos.SEPARADORES, "Se esperaba '('");

        Nodo nodoExpresion = new Nodo("expresion_logica");
        nodoCondicional.agregarHijo(nodoExpresion);
        expresion_logica(nodoExpresion);

        consumir(Tipos.SEPARADORES, "Se esperaba ')'");
        consumir(Tipos.SEPARADORES, "Se esperaba '{'");

        Nodo nodoInstrucciones = new Nodo("instrucciones");
        nodoCondicional.agregarHijo(nodoInstrucciones);

        while (!coincideValor("}")) {
            instruccion(nodoInstrucciones);
        }

        consumir(Tipos.SEPARADORES, "Se esperaba '}'");

        if (coincide(Tipos.SINO) || coincide(Tipos.SINO_SI)) {
            condicional_sino(nodoCondicional);
        }
    }

    private void condicional_sino(Nodo padre) {
        Nodo nodoSino = new Nodo("condicional_sino");
        padre.agregarHijo(nodoSino);

        if (coincide(Tipos.SINO)) {
            consumir(Tipos.SINO, "Se esperaba 'Kikyo'");
            consumir(Tipos.SEPARADORES, "Se esperaba '{'");

            Nodo nodoInstrucciones = new Nodo("instrucciones");
            nodoSino.agregarHijo(nodoInstrucciones);

            while (!coincideValor("}")) {
                instruccion(nodoInstrucciones);
            }

            consumir(Tipos.SEPARADORES, "Se esperaba '}'");
        } else if (coincide(Tipos.SINO_SI)) {
            consumir(Tipos.SINO_SI, "Se esperaba 'Naraku'");
            consumir(Tipos.SEPARADORES, "Se esperaba '('");

            Nodo nodoExpresion = new Nodo("expresion_logica");
            nodoSino.agregarHijo(nodoExpresion);
            expresion_logica(nodoExpresion);

            consumir(Tipos.SEPARADORES, "Se esperaba ')'");
            consumir(Tipos.SEPARADORES, "Se esperaba '{'");

            Nodo nodoInstrucciones = new Nodo("instrucciones");
            nodoSino.agregarHijo(nodoInstrucciones);

            while (!coincideValor("}")) {
                instruccion(nodoInstrucciones);
            }

            consumir(Tipos.SEPARADORES, "Se esperaba '}'");

            if (coincide(Tipos.SINO) || coincide(Tipos.SINO_SI)) {
                condicional_sino(padre);
            }
        }
    }

    private void expresion_logica(Nodo padre) {
        Nodo nodoExpresion = new Nodo("expresion_logica");
        padre.agregarHijo(nodoExpresion);

        valor(nodoExpresion);

        while (coincide(Tipos.OPERADOR_RELACIONAL) || coincide(Tipos.OPERADOR_ESPECIAL) ||
                coincide(Tipos.ASIGNACION_COMPUESTA)) {
            operador_relacional(nodoExpresion);
            valor(nodoExpresion);
        }
    }

    private void operador_relacional(Nodo padre) {
        Token token = tokenActual();
        if (token != null && (token.getTipo() == Tipos.OPERADOR_RELACIONAL ||
                token.getTipo() == Tipos.OPERADOR_ESPECIAL ||
                token.getTipo() == Tipos.ASIGNACION_COMPUESTA)) {
            Nodo nodoOperador = new Nodo("operador_relacional", token.getValor());
            padre.agregarHijo(nodoOperador);
            posicionActual++;
        } else {
            errores.add("Error sintáctico: Se esperaba un operador relacional");
        }
    }

    private void bucle_while(Nodo padre) {
        Nodo nodoWhile = new Nodo("bucle_while");
        padre.agregarHijo(nodoWhile);

        consumir(Tipos.MIENTRAS, "Se esperaba 'Inuyasha'");
        consumir(Tipos.SEPARADORES, "Se esperaba '('");

        Nodo nodoExpresion = new Nodo("expresion_logica");
        nodoWhile.agregarHijo(nodoExpresion);
        expresion_logica(nodoExpresion);

        consumir(Tipos.SEPARADORES, "Se esperaba ')'");
        consumir(Tipos.SEPARADORES, "Se esperaba '{'");

        Nodo nodoInstrucciones = new Nodo("instrucciones");
        nodoWhile.agregarHijo(nodoInstrucciones);

        while (!coincideValor("}")) {
            instruccion(nodoInstrucciones);
        }

        consumir(Tipos.SEPARADORES, "Se esperaba '}'");
    }

    private void bucle_for(Nodo padre) {
        Nodo nodoFor = new Nodo("bucle_for");
        padre.agregarHijo(nodoFor);

        consumir(Tipos.PARA, "Se esperaba 'Myoga'");
        consumir(Tipos.SEPARADORES, "Se esperaba '('");

        // Permitir declaración o asignación
        if (coincide(Tipos.TIPO_ENTERO) || coincide(Tipos.TIPO_DECIMAL) ||
                coincide(Tipos.TIPO_CADENA)) {
            declaracion_variable(nodoFor);
        } else {
            asignacion(nodoFor);
        }

        Nodo nodoExpresion = new Nodo("expresion_logica");
        nodoFor.agregarHijo(nodoExpresion);
        expresion_logica(nodoExpresion);

        consumir(Tipos.SEPARADORES, "Se esperaba ';'");

        incremento(nodoFor);

        consumir(Tipos.SEPARADORES, "Se esperaba ')'");
        consumir(Tipos.SEPARADORES, "Se esperaba '{'");

        Nodo nodoInstrucciones = new Nodo("instrucciones");
        nodoFor.agregarHijo(nodoInstrucciones);

        while (!coincideValor("}")) {
            instruccion(nodoInstrucciones);
        }

        consumir(Tipos.SEPARADORES, "Se esperaba '}'");
    }

    private void incremento(Nodo padre) {
        Nodo nodoIncremento = new Nodo("incremento");
        padre.agregarHijo(nodoIncremento);

        identificador(nodoIncremento);

        Token token = tokenActual();
        if (token != null && token.getTipo() == Tipos.ASIGNACION_COMPUESTA) {
            Nodo nodoOperador = new Nodo("operador", token.getValor());
            nodoIncremento.agregarHijo(nodoOperador);
            posicionActual++;

            valor(nodoIncremento);
        } else {
            errores.add("Error sintáctico: Se esperaba un operador de asignación compuesta (+=, -=, *=, /=, %=)");
        }
    }

    private void operacion(Nodo padre) {
        Nodo nodoOperacion = new Nodo("operacion");
        padre.agregarHijo(nodoOperacion);

        consumir(Tipos.OPERACION, "Se esperaba 'Hiraikotsu'");
        consumir(Tipos.SEPARADORES, "Se esperaba '('");

        Nodo nodoExpresion = new Nodo("expresion_aritmetica");
        nodoOperacion.agregarHijo(nodoExpresion);
        expresion_aritmetica(nodoExpresion);

        consumir(Tipos.SEPARADORES, "Se esperaba ')'");
        consumir(Tipos.SEPARADORES, "Se esperaba ';'");
    }



    private void expresion_aritmetica(Nodo padre) {
        Nodo nodoExpresion = new Nodo("expresion_aritmetica");
        padre.agregarHijo(nodoExpresion);

        valor(nodoExpresion);

        while (coincide(Tipos.OPERADORES)) {
            operador_aritmetico(nodoExpresion);
            valor(nodoExpresion);
        }
    }

    private void operador_aritmetico(Nodo padre) {
        Token token = tokenActual();
        if (token != null && token.getTipo() == Tipos.OPERADORES) {
            Nodo nodoOperador = new Nodo("operador_aritmetico", token.getValor());
            padre.agregarHijo(nodoOperador);
            posicionActual++;
        } else {
            errores.add("Error sintáctico: Se esperaba un operador aritmético (+, -, *, /, %)");
        }
    }

    private void retornar(Nodo padre) {
        Nodo nodoRetornar = new Nodo("retornar");
        padre.agregarHijo(nodoRetornar);

        consumir(Tipos.RETORNAR, "Se esperaba 'Tenseiga'");

        // Permitir expresiones complejas en el retorno
        expresion_aritmetica(nodoRetornar);
        consumir(Tipos.SEPARADORES, "Se esperaba ';'");
    }

    private void detener(Nodo padre) {
        Nodo nodoDetener = new Nodo("detener");
        padre.agregarHijo(nodoDetener);

        consumir(Tipos.DETENER, "Se esperaba 'Jaken'");
        consumir(Tipos.SEPARADORES, "Se esperaba ';'");
    }

    public List<String> mostrarErrores() {
        if (errores.isEmpty()) {
            System.out.println("No se encontraron errores sintácticos.");
        } else {
            for (String error : errores) {
                System.out.println(error);
            }
        }
        return errores;
    }
}