/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compla2_hernandez.procesos;

import java.util.ArrayList;
import java.util.Stack;

public class Cuadruplos {

    public static void generarCuadruplo(Stack<String> operadores, Stack<String> operandos, ArrayList<String> cuadruplosGenerados, int indiceCuadruplo) {
        // Desapilamos los operandos y el operador
        String operando2 = operandos.pop();  // Segundo operando (desapilado)
        String operando1 = operandos.pop();  // Primer operando (desapilado)
        String operador = operadores.pop();  // Operador (desapilado)

        String excepcion = " ";
        if (operador.equals("=") || operador.equals("<") || operador.equals("<=") || operador.equals(">") 
                || operador.equals(">=") || operador.equals("!=") || operador.equals("==")) {
            excepcion = " ";
        }
        
        // Creamos el cuadruplo como una cadena
        String cuadruplo = String.format("Indice %d (%s, %s, %s, %s)", indiceCuadruplo, operador, operando1, operando2, excepcion);
        
        // Añadimos el cuadruplo al ArrayList
        cuadruplosGenerados.add(cuadruplo);

        // Volvemos a apilar el resultado del cuadruplo
        operandos.push("R" + indiceCuadruplo);
    }
}
