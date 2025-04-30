package analizadorLexico;

public enum Tipos {

    // 1. Comentarios (deben procesarse primero para ignorarlos)
    COMENTARIO_LINEA("//.*"),
    COMENTARIO_BLOQUE("/\\*(.|\\n)*?\\*/"),

    // 2. Operadores inválidos (prioridad más alta)
    ASIGNACION_INVALIDA("\\*{2,}=|\\+{2,}=|\\-{2,}=|/{2,}=|%{2,}="),

    // 3. Operadores compuestos y relacionales
    ASIGNACION_COMPUESTA("(\\+=|\\-=|\\*=|\\/=|\\%=)"),
    OPERADOR_RELACIONAL("(<=|>=|==|!=|<|>)"),
    OPERADOR_ESPECIAL("(<>|=>|<=>)"),

    // 4. Operadores simples y asignador
    ASIGNADOR_SIMPLE("="),
    OPERADOR_INVALIDO("(\\+\\+|--|\\*=|/=|\\%=|\\+{2,}|\\-{2,}|\\*{2,}|\\/{2,}|%{2,})"),
    OPERADORES("[+\\-\\*\\/\\%]"),

    // 5. Palabras reservadas
    PALABRAS_RESERVADAS("(Kagome|Inuyasha|Miroku|Sango|Kirara|Shippo|Kikyo|Tessaiga|Shikon_no_Tama|Naraku)"),

    // 6. Números e identificadores
    NUMERO_INVALIDOPUNTO("\\d*\\.\\d*\\.\\d*"),
    NUMERO_INVALIDOCOMA("\\d+\\,\\d*"),
    VALOR_INVALIDO_MIXTO("[^\\s\\w]*\\d+[a-zA-Z]+[\\w]*"),

    NUMERO("\\d+(\\.\\d+)?"),
    IDENTIFICADOR_INVALIDO("[a-zA-Z][a-zA-Z0-9]{16,}"),

    IDENTIFICADORES("[a-zA-Z][a-zA-Z0-9]{5,15}"),

    // 7. Separadores y espacios
    SEPARADORES("[(){}\\[\\];:]"),
    ESPACIOS("\\s+"),

    // 8. Token de error (último recurso)
    ERROR(".+");

    public final String patron;

    Tipos(String patron) {
        this.patron = patron;
    }
}

