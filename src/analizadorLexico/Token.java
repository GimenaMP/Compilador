package analizadorLexico;

public class Token {
    private final Tipos tipo;  // Si no necesitas modificarlo, usa 'final'
    private final String valor; // Si no necesitas modificarlo, usa 'final'


    // Constructor corregido
    public Token(Tipos tokenTipo, String palabra) {
        if (tokenTipo == null || palabra == null) {
            throw new IllegalArgumentException("Tipo y valor no pueden ser nulos");
        }
        this.tipo = tokenTipo;
        this.valor = palabra;
    }

    // Getters (sin setters para inmutabilidad)
    public Tipos getTipo() {
        return tipo;
    }

    public String getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return "Token{tipo=" + tipo + ", valor='" + valor + "'}";
    }
}

