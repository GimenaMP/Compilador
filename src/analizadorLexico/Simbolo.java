package analizadorLexico;

public class Simbolo {
    private String nombre;
    private String tipoDato;
    private String valor;
    private int linea;

    public Simbolo(String nombre, String tipoDato, String valor, int linea) {
        this.nombre = nombre;
        this.tipoDato = tipoDato;
        this.valor = valor;
        this.linea = linea;
    }

    public String getNombre() {
        return nombre;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + ", Tipo: " + tipoDato + ", Valor: " + valor + ", Línea: " + linea;
    }
}

