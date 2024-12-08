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
        if (match("const")) {
            do {
                match("Identificador");
                match("=");
                match("Número");
            } while (match(","));
            match(";");
        }
        if (match("var")) {
            do {
                match("Identificador");
            } while (match(","));
            match(";");
        }
        while (match("procedure")) {
            match("Identificador");
            match(";");
            block();
            match(";");
        }
        statement();
    }

   private void statement() throws Exception {
    System.out.println("Entra en statement");
    if (match("Identificador")) {
        System.out.println("Encontrado Identificador");
        String Identificadorificador = previousLexema().getCadena();
        match(":=");
        String resultadoDeExpresion = expression();
        cuadruplos.add(String.format("(:=, %s, , %s)", resultadoDeExpresion, Identificadorificador));
    } else if (match("call")) {
            match("Identificador");
        } else if (match("?")) {
            String Identificadorificador = previousLexema().getCadena();
            match("Identificador");
            cuadruplos.add(String.format("(read, , , %s)", Identificadorificador));
        } else if (match("!")) {
            String resultadoDeExpresion = expression();
            cuadruplos.add(String.format("(write, %s, , )", resultadoDeExpresion));
        } else if (match("begin")) {
            do {
                statement();
            } while (match(";"));
            match("end");
        } else if (match("if")) {
            String condicion = condition();
            match("then");
            cuadruplos.add(String.format("(jf, %s, , L%d)", condicion, tempCounter));
            int saltoIndex = cuadruplos.size() - 1;
            statement();
            cuadruplos.set(saltoIndex, cuadruplos.get(saltoIndex).replace("L" + tempCounter, "L" + tempCounter));
        } else if (match("while")) {
            int inicioCondicion = cuadruplos.size();
            String condicion = condition();
            match("do");
            cuadruplos.add(String.format("(jf, %s, , L%d)", condicion, tempCounter));
            int saltoCondicion = cuadruplos.size() - 1;
            statement();
            cuadruplos.add(String.format("(jmp, , , L%d)", inicioCondicion));
            cuadruplos.set(saltoCondicion, cuadruplos.get(saltoCondicion).replace("L" + tempCounter, "L" + (cuadruplos.size())));
        }
    }

    private String condition() throws Exception {
        if (match("odd")) {
            String exp = expression();
            return String.format("(odd, %s, , )", exp);
        } else {
            String left = expression();
            String operador = "";
            if (match("=")) operador = "=";
            else if (match("#")) operador = "!=";
            else if (match("<")) operador = "<";
            else if (match("<=")) operador = "<=";
            else if (match(">")) operador = ">";
            else if (match(">=")) operador = ">=";
            else throw new Exception("Operador condicional no válido");

            String right = expression();
            String temp = nuevoTemporal();
            cuadruplos.add(String.format("(%s, %s, %s, %s)", operador, left, right, temp));
            return temp;
        }
    }

    private String expression() throws Exception {
        boolean hasSign = match("+") || match("-");
        String resultado = term();
        if (hasSign) {
            String operador = previousLexema().getCadena();
            String nuevoTemp = nuevoTemporal();
            cuadruplos.add(String.format("(%s, 0, %s, %s)", operador, resultado, nuevoTemp));
            resultado = nuevoTemp;
        }
        while (match("+") || match("-")) {
            String operador = previousLexema().getCadena();
            String termResult = term();
            String temp = nuevoTemporal();
            cuadruplos.add(String.format("(%s, %s, %s, %s)", operador, resultado, termResult, temp));
            resultado = temp;
        }
        return resultado;
    }

    private String term() throws Exception {
        String resultado = factor();
        while (match("*") || match("/")) {
            String operador = previousLexema().getCadena();
            String factorResult = factor();
            String temp = nuevoTemporal();
            cuadruplos.add(String.format("(%s, %s, %s, %s)", operador, resultado, factorResult, temp));
            resultado = temp;
        }
        return resultado;
    }

    private String factor() throws Exception {
        if (match("Identificadorificador")) {
            return previousLexema().getCadena();
        } else if (match("Número")) {
            return previousLexema().getCadena();
        } else if (match("(")) {
            String resultado = expression();
            match(")");
            return resultado;
        } else {
            throw new Exception("Factor inválido");
        }
    }

    private Lexema previousLexema() {
        return lexemas.get(index - 1);
    }

private boolean match(String expected) {
    Lexema current = currentLexema();

    // Primero, verifica si se está buscando una palabra clave o un identificador/número
    if (current != null) {
        // Si se espera una palabra clave (por ejemplo: read, if, etc.)
        if (expected.equals(current.getCadena()) && current.getGrupo().equals("Palabra Reservada")) {
            avanzar();
            return true;
        }
        // Si esperamos un Identificador o un Número
        if (expected.equals("Identificador") && current.getGrupo().equals("Identificador")) {
            avanzar();
            return true;
        } else if (expected.equals("Número") && current.getGrupo().equals("Número")) {
            avanzar();
            return true;
        }
    }

    // Si no encuentra lo esperado, imprime el error
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
