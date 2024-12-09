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
    // Manejo de constantes
    
if (match("const")) {
    do {
        match("Identificador");
        match("=");
        match("Número");
    } while (match(",")); // Acepta múltiples declaraciones separadas por coma
    if (!match(";")) {    // Pero debe terminar con un punto y coma
        throw new Exception("Falta ';' después de declaración de constantes");
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
    } else if (match("begin")) { // Manejo de bloques 'begin ... end'
    do {
        statement(); // Analiza cada statement dentro del bloque
    } while (match(";")); // Acepta múltiples statements separados por ';'
    if (!match("end")) { // Verifica que el bloque cierre con 'end'
        throw new Exception("Falta 'end' al final del bloque 'begin'");
    }
    } else if (match("if")) {
    String condicion = condition(); // Evalúa la condición
    match("then");
    String etiquetaFalsa = "L" + nuevoTemporal();
    cuadruplos.add(String.format("(jf, %s, , %s)", condicion, etiquetaFalsa));
    statement(); // Procesa el bloque dentro del `if`
    cuadruplos.add(String.format("(label, , , %s)", etiquetaFalsa)); // Etiqueta falsa
    } else if (match("while")) {
        int inicioCondicion = cuadruplos.size();
        String condicion = condition();
        match("do");
        cuadruplos.add(String.format("(jf, %s, , L%d)", condicion, tempCounter));
        int saltoCondicion = cuadruplos.size() - 1;
        statement();
        cuadruplos.add(String.format("(jmp, , , L%d)", inicioCondicion));
        cuadruplos.set(saltoCondicion, cuadruplos.get(saltoCondicion).replace("L" + tempCounter, "L" + (cuadruplos.size())));
    } else {
        throw new Exception("Estructura no reconocida en 'statement'");
    }
}



    private String condition() throws Exception {
    String ladoIzquierdo = expression();
    if (match("=") || match("<>") || match("<") || match("<=") || match(">") || match(">=")) {
        String operador = previousLexema().getCadena();
        String ladoDerecho = expression();
        cuadruplos.add(String.format("(%s, %s, %s, temp%d)", operador, ladoIzquierdo, ladoDerecho, tempCounter));
        return "temp" + tempCounter++;
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
        return previousLexema().getCadena(); // Devuelve el número
    } else if (match("Identificador")) {
        return previousLexema().getCadena(); // Devuelve el identificador
    } else if (match("(")) {
        String resultado = expression(); // Procesa una subexpresión entre paréntesis
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

    if (current != null) {
        // Manejo para operadores relacionales
        if (expected.equals("=") && current.getGrupo().equals("Operador Relacional")) {
            avanzar();
            return true;
        }
        // Manejo para identificadores
        if (expected.equals("Identificador") && current.getGrupo().equals("Identificador")) {
            avanzar();
            return true;
        }
        // Manejo para números
        if (expected.equals("Número") && current.getGrupo().equals("Número")) {
            avanzar();
            return true;
        }
        // Manejo para palabras reservadas
        if (expected.equals(current.getCadena()) && current.getGrupo().equals("Palabra Reservada")) {
            avanzar();
            return true;
        }
        // Manejo para agrupadores (por ejemplo, ',' o ';')
        if (expected.equals(current.getCadena()) && current.getGrupo().equals("Agrupador")) {
            avanzar();
            return true;
        }
        if (expected.equals(":=") && current.getGrupo().equals("Operador de Asignación")) {
        avanzar();
        return true;
}

    }

    // Si no se encuentra lo esperado
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
}
