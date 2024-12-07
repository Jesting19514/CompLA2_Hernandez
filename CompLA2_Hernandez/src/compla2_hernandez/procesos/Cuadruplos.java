/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compla2_hernandez.procesos;


import java.util.ArrayList;
import java.util.Stack;

public class Cuadruplos {

   public static void generarCuadruplo(Stack<String> operadores, Stack<String> operandos, ArrayList<String> cuadruplosGenerados,
            int indiceCuadruplo, Stack<Integer> ciclos, Stack<Integer> incompletos) {

    // Desapilamos los operandos y el operador
    String operando2 = operandos.pop();  // Segundo operando (desapilado)
    String operando1 = operandos.pop();  // Primer operando (desapilado)
    String operador = operadores.pop();  // Operador (desapilado)

    String resultado = "R" + indiceCuadruplo;  // Resultado temporal para almacenar el resultado

    // Generamos el cuadruplo según el tipo de operador
    if (operador.equals("=")) {
        // Asignación
        cuadruplosGenerados.add(String.format("(%s, %s, , %s)", operador, operando1, resultado));
    } else if (operador.equals("+") || operador.equals("-") || operador.equals("*") || operador.equals("/")) {
        // Operación aritmética
        cuadruplosGenerados.add(String.format("(%s, %s, %s, %s)", operador, operando1, operando2, resultado));
    } else if (operador.equals("<") || operador.equals("<=") || operador.equals(">") || operador.equals(">=") || operador.equals("==") || operador.equals("!=")) {
        // Operación relacional
        cuadruplosGenerados.add(String.format("(%s, %s, %s, %s)", operador, operando1, operando2, resultado));
    }

    // Gestión de saltos para estructuras de control
    if (!ciclos.isEmpty()) {
        // En un ciclo, generamos el salto de vuelta al inicio del ciclo
        String saltoCiclo = String.format("(JMP, , , Índice %d)", ciclos.peek());
        cuadruplosGenerados.add(saltoCiclo);
        ciclos.pop();  // El ciclo está completado
    }

    if (!incompletos.isEmpty()) {
        // Completar el salto para una condición 'if'
        int saltoCondicion = incompletos.pop();
        cuadruplosGenerados.set(saltoCondicion - 1, String.format("(%s, %s, %s, Índice %d)", 
            operadores.peek(), operando1, operando2, indiceCuadruplo));
    }

    // Apilamos el resultado para futuras operaciones
    operandos.push(resultado);
}


}

