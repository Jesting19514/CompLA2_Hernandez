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
    block();  // Llama a block
    match(".");  // Asegúrate de que el programa termine con un punto
    imprimirCuadruplos();  // Imprime los cuadruplos
}


    private void block() throws Exception {
    
/// Manejo de constantes
if (match("const")) {
    System.out.println("Procesando constantes...");
    while (true) {
        // Verificamos que el primer token sea un "Identificador"
        if (match("Identificador")) {
            System.out.println("Identificador encontrado: " + previousLexema().getCadena());
        } else {
            throw new Exception("Se esperaba un identificador después de 'const'");
        }
        
        // Verificamos que el siguiente token sea "="
        if (!match("=")) {
            throw new Exception("Se esperaba '=' después de identificador");
        }
        System.out.println("Operador '=' encontrado");
        
        // Verificamos que el siguiente token sea un "Número"
        if (!match("Número")) {
            throw new Exception("Se esperaba un número después de '='");
        }
        System.out.println("Número encontrado: " + previousLexema().getCadena());
        
        // Si encontramos una coma, continuamos procesando la siguiente constante
        if (match(",")) {
            System.out.println("Se encontró ',' continuando con la siguiente constante...");
            continue;
        }
        
        // Si no hay una coma, esperamos un punto y coma ";" para terminar la declaración
        if (match(";")) {
            System.out.println("Constantes procesadas correctamente.");
            break;
        }
        
        // Si no encontramos coma ni punto y coma, hay un error
        throw new Exception("Se esperaba ',' o ';' después de una constante");
    }
}








if (match("var")) {
    do {
        match("Identificador");
    } while (match(",")); // Acepta múltiples variables separadas por coma
    if (!match(";")) {    // Pero debe terminar con un punto y coma
        throw new Exception("Falta ';' después de declaración de variables");
    }
}


    // Manejo de procedimientos
    // En block()
while (match("procedure")) {
    match("Identificador");
    if (!match(";")) {
        throw new Exception("Falta ';' después de nombre de procedimiento");
    }
    block(); // Procedimiento tiene su propio bloque
    if (!match(";")) {
        throw new Exception("Falta ';' después de bloque de procedimiento");
    }
}

// Luego continúa con el cuerpo principal
statement();
}


  private void statement() throws Exception {
    System.out.println("Entra en statement");

   if (match("while")) {
    String etiquetaInicio = "L" + tempCounter++;
    cuadruplos.add(String.format("(label, , , %s)", etiquetaInicio));
    String condicion = condition();
    String etiquetaFin = "L" + tempCounter++;
    cuadruplos.add(String.format("(jf, %s, , %s)", condicion, etiquetaFin));
    if (!match("do")) {
        manejarError("Se esperaba 'do' después de condición en while.");
    } else {
        statement(); // Procesa el cuerpo del while
    }
    cuadruplos.add(String.format("(jmp, , , %s)", etiquetaInicio));
    cuadruplos.add(String.format("(label, , , %s)", etiquetaFin));
    } else {
    if (match("Identificador")) {
        String Identificador = previousLexema().getCadena();
        if (!match(":=")) { // Verifica si se encuentra el operador de asignación correcto
            throw new Exception("Esperado ':=' pero se encontró: " + currentLexema().getCadena());
        }
        String resultadoDeExpresion = expression();
        cuadruplos.add(String.format("(:=, %s, , %s)", resultadoDeExpresion, Identificador));
    } else if (match("call")) {
        match("Identificador");
    } else if (match("read")) {
    if (!match("Identificador")) { // Verifica que `read` sea seguido de un identificador
        throw new Exception("Se esperaba un identificador después de 'read'");
    }
    String identificador = previousLexema().getCadena();
    cuadruplos.add(String.format("(read, , , %s)", identificador));
    } else if (match("write")) {
        String resultadoDeExpresion = expression();
        cuadruplos.add(String.format("(write, %s, , )", resultadoDeExpresion));
    } else if (match("begin")) {
    do {
        statement();  // Analiza cada statement dentro del bloque
        if (currentLexema() != null && match(";")) {
            continue;
        }
    } while (currentLexema() != null && !match("end"));
    if (!match("end")) {
        throw new Exception("Falta 'end' al final del bloque 'begin'");
    }

    } else if (match("if")) {
    String condicion = condition();  // Evalúa la condición
    match("then");
    String etiquetaFalsa = "L" + nuevoTemporal();
    cuadruplos.add(String.format("(jf, %s, , %s)", condicion, etiquetaFalsa));
    statement();  // Procesa el bloque dentro del `if`
    cuadruplos.add(String.format("(label, , , %s)", etiquetaFalsa));  // Etiqueta falsa
} 
    }
    debug("statement");
    
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





    private void imprimirCuadruplos() {
        for (String cuad : cuadruplos) {
            System.out.println(cuad);
        }
    }

    public ArrayList<String> getCuadruplos() {
        return cuadruplos;
    }
    
    
    private void debug(String mensaje) {
    System.out.println("DEBUG: " + mensaje + " en índice " + index + " con token " + (currentLexema() != null ? currentLexema().getCadena() : "null"));
}

    private void manejarError(String mensaje) {
    System.out.println("Error: " + mensaje);
    avanzar(); // Avanza al siguiente token para evitar ciclos infinitos
}

}

