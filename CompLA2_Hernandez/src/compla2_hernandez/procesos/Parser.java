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
        if (!match(".")) { // Verifica que el programa termine con un punto
            throw new Exception("Se esperaba '.' al final del programa.");
        }
        imprimirCuadruplos();  // Imprime los cuadruplos
    }

    private void block() throws Exception {
        // Manejo de constantes
        if (match("const")) {
            System.out.println("Procesando constantes...");
            while (true) {
                if (!match("Identificador")) {
                    manejarError("Se esperaba un identificador después de 'const'");
                    break;
                }
                if (!match("=")) {
                    manejarError("Se esperaba '=' después del identificador");
                    break;
                }
                if (!match("Número")) {
                    manejarError("Se esperaba un número después de '='");
                    break;
                }
                if (match(",")) {
                    continue;
                }
                if (match(";")) {
                    System.out.println("Constantes procesadas correctamente.");
                    break;
                }
                manejarError("Se esperaba ',' o ';' después de una constante");
                break;
            }
        }

        // Manejo de variables
        if (match("var")) {
            do {
                if (!match("Identificador")) {
                    manejarError("Se esperaba un identificador en la declaración de variables");
                    break;
                }
            } while (match(","));
            if (!match(";")) {
                throw new Exception("Se esperaba ';' después de la declaración de variables");
            }
        }

        // Manejo de procedimientos
        while (match("procedure")) {
            if (!match("Identificador")) {
                manejarError("Se esperaba un identificador después de 'procedure'");
                break;
            }
            if (!match(";")) {
                manejarError("Se esperaba ';' después del identificador de procedimiento");
                break;
            }
            block();
            if (!match(";")) {
                manejarError("Se esperaba ';' después del bloque del procedimiento");
                break;
            }
        }

        // Cuerpo principal
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
            manejarError("Se esperaba 'do' después de la condición en 'while'");
        } else {
            statement();
        }
        cuadruplos.add(String.format("(jmp, , , %s)", etiquetaInicio));
        cuadruplos.add(String.format("(label, , , %s)", etiquetaFin));
    } else if (match("Identificador")) {
        String identificador = previousLexema().getCadena();
        if (!match(":=")) {
            manejarError("Se esperaba ':=' después del identificador");
            return;
        }
        String resultadoDeExpresion = expression();
        cuadruplos.add(String.format("(:=, %s, , %s)", resultadoDeExpresion, identificador));
    } else if (match("call")) {
        if (!match("Identificador")) {
            manejarError("Se esperaba un identificador después de 'call'");
        }
    } else if (match("read")) {
        if (!match("Identificador")) {
            manejarError("Se esperaba un identificador después de 'read'");
        } else {
            String identificador = previousLexema().getCadena();
            cuadruplos.add(String.format("(read, , , %s)", identificador));
        }
    } else if (match("write")) {
        String resultadoDeExpresion = expression();
        cuadruplos.add(String.format("(write, %s, , )", resultadoDeExpresion));
    } else if (match("begin")) {
        System.out.println("Procesando bloque 'begin'");
        do {
            statement(); // Procesa cada statement dentro del bloque
            if (currentLexema() != null && match(";")) {
                continue;
            }
        } while (currentLexema() != null && !match("end"));
        if (!match("end")) {
            manejarError("Falta 'end' al final del bloque 'begin'");
        }
    } else if (match("if")) {
        String condicion = condition(); // Evalúa la condición
        if (!match("then")) {
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
        System.out.println("Error: " + mensaje + " en índice " + index);
        avanzar(); // Avanza para evitar ciclos infinitos
    }

    private void debug(String mensaje) {
        System.out.println("DEBUG: " + mensaje + " en índice " + index + " con token " + (currentLexema() != null ? currentLexema().getCadena() : "null"));
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

