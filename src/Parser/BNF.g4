grammar BNF;



// Regla principal
programa: 'Kilala' instrucciones 'Fin';

// Lista de instrucciones
instrucciones: (instruccion)*;

// Tipos de instrucciones
instruccion: declaracion_variable
           | declaracion_constante
           | asignacion
           | funcion_declaracion
           | condicional
           | bucle_while
           | bucle_for
           | operacion
           | retornar
           | detener
           | comentario;

// Declaración de variables
declaracion_variable: 'Bakusaiga' tipo identificador '=' expresion ';';

// Declaración de constantes
declaracion_constante: 'Tetsusaiga' tipo identificador '=' expresion ';';

// Asignación
asignacion: identificador '=' expresion ';';

// Tipos de datos
tipo: 'Miroku'     // Entero
    | 'Sesshomaru' // Decimal
    | 'Sango';     // Cadena

// Declaración de funciones
funcion_declaracion: 'Shippo' identificador '(' parametros? ')' '{' instrucciones '}';

// Parámetros de funciones
parametros: parametro (',' parametro)*;
parametro: tipo identificador;

// Condicionales
condicional: 'Kagome' '(' expresion_logica ')' '{' instrucciones '}' condicional_sino?;
condicional_sino: 'Kikyo' '{' instrucciones '}'
                | 'Naraku' '(' expresion_logica ')' '{' instrucciones '}' condicional_sino?;

// Bucle while
bucle_while: 'Inuyasha' '(' expresion_logica ')' '{' instrucciones '}';

// Bucle for
bucle_for: 'Myoga' '(' (declaracion_variable | asignacion) expresion_logica ';' incremento ')' '{' instrucciones '}';

// Incremento
incremento: identificador ('+=' | '-=') numero;

// Operaciones
operacion: 'Hiraikotsu' '(' expresion_aritmetica ')' ';';

// Retorno
retornar: 'Tenseiga' expresion ';';

// Detener
detener: 'Jaken' ';';

// Expresiones
expresion: expresion_logica
         | expresion_aritmetica;

expresion_logica: termino (operador_relacional termino)*;
expresion_aritmetica: termino (operador_aritmetico termino)*;

// Términos
termino: numero
       | cadena
       | identificador
       | '(' expresion ')';

// Operadores relacionales
operador_relacional: '<' | '>' | '<=' | '>=' | '==' | '!=' | '<>' | '=>' | '<=>';

// Operadores aritméticos
operador_aritmetico: '+' | '-' | '*' | '/' | '%';

// Números
numero: DIGIT+ ('.' DIGIT+)?;

// Cadenas
cadena: '"' (ESC | .)*? '"';

// Identificadores
identificador: LETRA (LETRA | DIGIT)*;

// Comentarios
comentario: '//' .*? '\n'     // Comentarios de línea
           | '/*' .*? '*/'  ;   // Comentarios de bloque

// Reglas léxicas
fragment DIGIT: [0-9];
fragment LETRA: [a-zA-Z];
fragment ESC: '\\' .;
