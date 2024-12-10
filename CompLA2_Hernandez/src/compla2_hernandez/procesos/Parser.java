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
        if (!match(TablaDeTokens.getNumero("."))) {
            throw new Exception("Se esperaba '.' al final del programa.");
        }
        imprimirCuadruplos();
    }

     private void statement() throws Exception {
        if (match(TablaDeTokens.getNumero("while"))) {
            String etiquetaInicio = "L" + tempCounter++;
            cuadruplos.add(String.format("(label, , , %s)", etiquetaInicio));
            String condicion = condition();
            String etiquetaFin = "L" + tempCounter++;
            cuadruplos.add(String.format("(jf, %s, , %s)", condicion, etiquetaFin));
            if (!match(TablaDeTokens.getNumero("do"))) {
                manejarError("Se esperaba 'do' después de la condición en 'while'");
            } else {
                statement();
            }
            cuadruplos.add(String.format("(jmp, , , %s)", etiquetaInicio));
            cuadruplos.add(String.format("(label, , , %s)", etiquetaFin));
        } else if (match(TablaDeTokens.getNumero("Identificador"))) {
            String identificador = previousLexema().getCadena();
            if (!match(TablaDeTokens.getNumero(":="))) {
                manejarError("Se esperaba ':=' después del identificador");
                return;
            }
            String resultadoDeExpresion = expression();
            cuadruplos.add(String.format("(:=, %s, , %s)", resultadoDeExpresion, identificador));
        } else if (match(TablaDeTokens.getNumero("call"))) {
            if (!match(TablaDeTokens.getNumero("Identificador"))) {
                manejarError("Se esperaba un identificador después de 'call'");
            }
        } else if (match(TablaDeTokens.getNumero("read"))) {
            if (!match(TablaDeTokens.getNumero("Identificador"))) {
                manejarError("Se esperaba un identificador después de 'read'");
            } else {
                String identificador = previousLexema().getCadena();
                cuadruplos.add(String.format("(read, , , %s)", identificador));
            }
        } else if (match(TablaDeTokens.getNumero("write"))) {
            String resultadoDeExpresion = expression();
            cuadruplos.add(String.format("(write, %s, , )", resultadoDeExpresion));
        } else if (match(TablaDeTokens.getNumero("begin"))) {
            do {
                statement();
            } while (match(TablaDeTokens.getNumero(";")));
            if (!match(TablaDeTokens.getNumero("end"))) {
                manejarError("Falta 'end' al final del bloque 'begin'");
            }
        } else if (match(TablaDeTokens.getNumero("if"))) {
            String condicion = condition();
            if (!match(TablaDeTokens.getNumero("then"))) {
                manejarError("Se esperaba 'then' después de la condición en 'if'");
                return;
            }
            String etiquetaFalsa = "L" + nuevoTemporal();
            cuadruplos.add(String.format("(jf, %s, , %s)", condicion, etiquetaFalsa));
            statement();
            cuadruplos.add(String.format("(label, , , %s)", etiquetaFalsa));
        } else {
            manejarError("Statement no reconocido");
        }
    }
     
     
     
   private void block() throws Exception {
    // Manejo de constantes
    if (match(TablaDeTokens.getNumero("const"))) {
        System.out.println("Procesando constantes...");
        while (true) {
            if (!match(TablaDeTokens.getNumero("Identificador"))) {
                manejarError("Se esperaba un identificador después de 'const'");
                break;
            }
            if (!match(TablaDeTokens.getNumero("="))) {
                manejarError("Se esperaba '=' después del identificador");
                break;
            }
            // Aseguramos que se lee un número después del "="
            if (!match(TablaDeTokens.getNumero("Número"))) {
                manejarError("Se esperaba un número después de '='");
                break;
            }
            // Si hay una coma, se continúa procesando más constantes
            if (match(TablaDeTokens.getNumero(","))) {
                continue;
            }
            // Si hay un punto y coma, finalizamos las constantes
            if (match(TablaDeTokens.getNumero(";"))) {
                System.out.println("Constantes procesadas correctamente.");
                break;
            }
            manejarError("Se esperaba ',' o ';' después de una constante");
            break;
        }
    }

    // Manejo de variables
    if (match(TablaDeTokens.getNumero("var"))) {
        do {
            if (!match(TablaDeTokens.getNumero("Identificador"))) {
                manejarError("Se esperaba un identificador en la declaración de variables");
                break;
            }
        } while (match(TablaDeTokens.getNumero(",")));
        if (!match(TablaDeTokens.getNumero(";"))) {
            throw new Exception("Se esperaba ';' después de la declaración de variables");
        }
    }

    // Manejo de procedimientos
    while (match(TablaDeTokens.getNumero("procedure"))) {
        if (!match(TablaDeTokens.getNumero("Identificador"))) {
            manejarError("Se esperaba un identificador después de 'procedure'");
            break;
        }
        if (!match(TablaDeTokens.getNumero(";"))) {
            manejarError("Se esperaba ';' después del identificador de procedimiento");
            break;
        }
        block();
        if (!match(TablaDeTokens.getNumero(";"))) {
            manejarError("Se esperaba ';' después del bloque del procedimiento");
            break;
        }
    }

    // Cuerpo principal
    statement();
}

private boolean match(int expectedToken) {
    Lexema current = currentLexema();
    if (current != null && current.getToken() == expectedToken) {
        avanzar();
        return true;
    }
    return false;
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
    if (match("=") || match("<>") || match("<") || match("<=") || match(">") || match(">=")) {
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
    while (currentLexema() != null && (match("+") || match("-"))) {
        String operador = previousLexema().getCadena();
        String siguienteTermino = term();
        cuadruplos.add(String.format("(%s, %s, %s, temp%d)", operador, resultado, siguienteTermino, tempCounter));
        resultado = "temp" + tempCounter++;
    }
    return resultado;
}


private String term() throws Exception {
    String resultado = factor();
    while (match("*") || match("/")) {
        String operador = previousLexema().getCadena();
        String siguienteFactor = factor();
        cuadruplos.add(String.format("(%s, %s, %s, temp%d)", operador, resultado, siguienteFactor, tempCounter));
        resultado = "temp" + tempCounter++;
    }
    return resultado;
}


 private String factor() throws Exception {
    if (match("Número")) {
        return previousLexema().getCadena();
    } else if (match("Identificador")) {
        return previousLexema().getCadena();
    } else if (match("(")) {
        String resultado = expression();
        if (!match(")")) {
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

private boolean match(String expected) {
    Lexema current = currentLexema();
    if (current == null) {
        return false;
    }

    switch (expected) {
        case "=":
            if (current.getGrupo().equals("Operador Relacional")) {
                avanzar();
                return true;
            }
            break;
        case "Identificador":
            if (current.getGrupo().equals("Identificador")) {
                avanzar();
                return true;
            }
            break;
        case "Número":
            if (current.getGrupo().equals("Número")) {
                avanzar();
                return true;
            }
            break;
        case ":=":
            if (current.getGrupo().equals("Operador de Asignación")) {
                avanzar();
                return true;
            }
            break;
        case ",":
            if (current.getGrupo().equals("Agrupador") && current.getCadena().equals(",")) {
                avanzar();
                return true;
            }
            break;
        case ";":
            if (current.getGrupo().equals("Agrupador") && current.getCadena().equals(";")) {
                avanzar();
                return true;
            }
            break;
        default:
            if (expected.equals(current.getCadena()) && current.getGrupo().equals("Palabra Reservada")) {
                avanzar();
                return true;
            }
            break;
    }
    System.out.println("Esperado: " + expected + " pero se encontró: " + (current != null ? current.getCadena() : "null"));
    return false;
}


    public ArrayList<String> getCuadruplos() {
        return cuadruplos;
    }

}

