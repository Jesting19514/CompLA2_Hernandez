/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compla2_hernandez.procesos;
import static compla2_hernandez.procesos.Archivos.cuadruplos;
import java.util.ArrayList;
/**
 *
 * @author team1
 */
public class Parser {
    private ArrayList<Lexema> lexemas;
    private int index = 0;
    private ArrayList<String> cuadruplos = new ArrayList<>();
    private int tempCounter = 0;

    public Parser(ArrayList<Lexema> lexemas) {
        this.lexemas = lexemas;
    }

    private Lexema currentLexema() {
        return index < lexemas.size() ? lexemas.get(index) : null;
    }

    private void avanzar() {
        if (index < lexemas.size()) {
            index++;
        }
    }

    private String nuevoTemporal() {
        return "t" + (tempCounter++);
    }

    public void program() throws Exception {
        System.out.println("Entra en program");
        block();
        if (!match(25)) {  // Verifica que el programa termine con un punto
        manejarError("Se esperaba un '.' al final del programa");
        }
        imprimirCuadruplos();
    }

    // Cambiaremos el método 'match' para que se compare el número de token en lugar de la cadena

    private boolean match(int expectedToken) {
    Lexema current = currentLexema();  // Obtener el lexema actual
    if (current == null) {
        return false;  // Si no hay más lexemas, retornar false
    }

    // Comparar el token actual con el esperado
    if (current.getToken() == expectedToken) {
        avanzar();  // Avanzar al siguiente lexema
        return true;
    }

    return false;
}



// En el bloque principal donde procesamos las constantes, cambiamos la verificación del número para usar el número de token

private void block() throws Exception {
    // Manejo de constantes
    if (match(102)) {  // Si encontramos 'const'
        while (true) {
            if (match(1)) {  // Si encontramos un identificador
                String identificador = previousLexema().getCadena();
                if (!match(14)) {  // Si no encontramos ':='
                    manejarError("Se esperaba ':=' después de un identificador");
                    break;
                }
                if (!match(2)) {  // Si no encontramos un número
                    manejarError("Se esperaba un número después de ':='");
                    break;
                }
                String numero = previousLexema().getCadena();
                System.out.println("Constante: " + identificador + " = " + numero);
                
                if (match(22)) {  // Si encontramos una coma ','
                    continue;  // Continuamos procesando otras constantes
                }
                if (match(21)) {  // Si encontramos un punto y coma ';'
                    break;  // Terminamos la declaración de constantes
                }
                manejarError("Se esperaba ',' o ';' después de una constante");
                break;
            }
}
    }

    // Manejo de variables
    if (match(103)) {
        do {
            if (!match(1)) {
                manejarError("Se esperaba un identificador en la declaración de variables");
                break;
            }
        } while (!match(22));
        if (!match(21)) {
            throw new Exception("Se esperaba ';' después de la declaración de variables");
        }
    }

    // Manejo de procedimientos
    while (match(104)) {
        if (!match(1)) {
            manejarError("Se esperaba un identificador después de 'procedure'");
            break;
        }
        if (!match(21)) {
            manejarError("Se esperaba ';' después del identificador de procedimiento");
            break;
        }
        block();
        if (!match(21)) {
            manejarError("Se esperaba ';' después del bloque del procedimiento");
            break;
        }
    }

    // Cuerpo principal
    statement();
}


// En el método 'statement', vamos a mejorar el tratamiento de las declaraciones

private void statement() throws Exception {
    System.out.println("DEBUG: Entra en statement");

    if (match(109)) { //while
        String etiquetaInicio = "L" + tempCounter++;
        cuadruplos.add(String.format("(label, , , %s)", etiquetaInicio));
        String condicion = condition();
        String etiquetaFin = "L" + tempCounter++;
        cuadruplos.add(String.format("(jf, %s, , %s)", condicion, etiquetaFin));
        if (!match(110)) { //do
            manejarError("Se esperaba 'do' después de la condición en 'while'");
        } else {
            statement();
        }
        cuadruplos.add(String.format("(jmp, , , %s)", etiquetaInicio));
        cuadruplos.add(String.format("(label, , , %s)", etiquetaFin));
    } else if (match(1)) {
        String identificador = previousLexema().getCadena();
        if (!match(14)) {  //:=
            manejarError("Se esperaba ':=' después del identificador");
            return;
        }
        String resultadoDeExpresion = expression();
        cuadruplos.add(String.format("(:=, %s, , %s)", resultadoDeExpresion, identificador));
    } else if (match(105)) { //call
        if (!match(1)) {
            manejarError("Se esperaba un identificador después de 'call'");
        }
    } else if (match(112)) { //read
        if (!match(1)) {
            manejarError("Se esperaba un identificador después de 'read'");
        } else {
            String identificador = previousLexema().getCadena();
            cuadruplos.add(String.format("(read, , , %s)", identificador));
        }
    } else if (match(113)) { //write
        String resultadoDeExpresion = expression();
        cuadruplos.add(String.format("(write, %s, , )", resultadoDeExpresion));
    } else if (match(100)) { //begin
        System.out.println("DEBUG: Procesando bloque 'begin'");
        do {
            statement(); // Procesa cada statement dentro del bloque
            if (currentLexema() != null && (match(21))) { //;
                continue;
            }
        } while (currentLexema() != null && (!match(101))); //end
        if (!match(101)) { //end
            manejarError("Falta 'end' al final del bloque 'begin'");
        }
    } else if (match(106)) { //if
        String condicion = condition(); // Evalúa la condición
        if (!match(107))  { //then
            manejarError("Se esperaba 'then' después de la condición en 'if'");
            return;
        }
        String etiquetaFalsa = "L" + nuevoTemporal();
        cuadruplos.add(String.format("(jf, %s, , %s)", condicion, etiquetaFalsa));
        statement(); // Procesa el bloque dentro del `if`
        cuadruplos.add(String.format("(label, , , %s)", etiquetaFalsa)); // Etiqueta falsa
    } else {
        manejarError("Statement no reconocido");
    }
    debug("statement");
}


private void manejarError(String mensaje) {
    Lexema actual = currentLexema();
    System.out.println("Error: " + mensaje + " en token: " +
            (actual != null ? actual.getCadena() : "null") +
            " (índice: " + index + ")");
    avanzar();  // Avanzar para evitar ciclos infinitos
}

private void imprimirCuadruplos() {
    for (String cuad : cuadruplos) {
        System.out.println(cuad);
    }
}

   private String condition() throws Exception {
    String ladoIzquierdo = expression();
    if (match(57) || match(51) || match(54) || match(55) || match(52) || match(53)) { //= <> < <= > >=
        String operador = previousLexema().getCadena();
        String ladoDerecho = expression();
        String temporal = nuevoTemporal();
        cuadruplos.add(String.format("(%s, %s, %s, %s)", operador, ladoIzquierdo, ladoDerecho, temporal));
        return temporal;
    } else {
        throw new Exception("Condición inválida: operador relacional esperado");
    }
    
}



private String expression() throws Exception {
    String resultado = term();
    while (currentLexema() != null && (match(10) || match(11))) { //+ -
        String operador = previousLexema().getCadena();
        String siguienteTermino = term();
        cuadruplos.add(String.format("(%s, %s, %s, temp%d)", operador, resultado, siguienteTermino, tempCounter));
        resultado = "temp" + tempCounter++;
    }
    return resultado;
}


private String term() throws Exception {
    String resultado = factor();
    while (match(12) || match(13)) { //  *  /
        String operador = previousLexema().getCadena();
        String siguienteFactor = factor();
        cuadruplos.add(String.format("(%s, %s, %s, temp%d)", operador, resultado, siguienteFactor, tempCounter));
        resultado = "temp" + tempCounter++;
    }
    return resultado;
}


 private String factor() throws Exception {
    if (match(2)) {
        return previousLexema().getCadena();
    } else if (match(1)) {
        return previousLexema().getCadena();
    } else if (match(23)) { // (
        String resultado = expression();
        if (!match(24)) { //)
            throw new Exception("Falta ')' para cerrar la expresión");
        }
        return resultado;
    } else {
        throw new Exception("Factor inválido: se esperaba un número, identificador o expresión entre paréntesis.");
    }
}

    private Lexema previousLexema() {
        return lexemas.get(index - 1);
    }

    public ArrayList<String> getCuadruplos() {
        return cuadruplos;
    }
private void debug(String mensaje) {
    System.out.println("DEBUG: " + mensaje + " en índice " + index + " con token " + (currentLexema() != null ? currentLexema().getCadena() : "null"));
}

}

