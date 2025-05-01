package analizadorLexico;

public class Simbolo {
    private String nombre;
    private String tipo;
    private String valor;
    private int linea;

    public Simbolo(String nombre, String tipo, String valor, int linea) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;
        this.linea = linea;
    }

    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public String getValor() { return valor; }
    public int getLinea() { return linea; }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + ", Tipo: " + tipo + ", Valor: " + valor + ", Línea: " + linea;
    }
}

