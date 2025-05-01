package Parser;

import analizadorLexico.Token;

public class ErrorSintactico {
    private final String mensaje;
    private final Token token;

    public ErrorSintactico(String mensaje, Token token) {
        this.mensaje = mensaje;
        this.token = token;
    }

    @Override
    public String toString() {
        return "Error sintáctico: " + mensaje + " en token " + token;
    }
}