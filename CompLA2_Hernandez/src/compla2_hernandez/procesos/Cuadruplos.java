/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compla2_hernandez.procesos;

import java.util.Stack;

/**
 *
 * @author team1
 */
public class Cuadruplos {

    public static void generarCuadruplo(Stack<String> operadores, Stack<String> operandos, StringBuilder cuadruplosGenerados, int indiceCuadruplo) {
        String operando2 = operandos.pop();  // Segundo operando (desapilado)
        String operando1 = operandos.pop();  // Primer operando (desapilado)
        String operador = operadores.pop();  // Operador (desapilado)

        // Generar cuadruplo en el formato: (operador, operando1, operando2, resultado)
        String cuadruplo = String.format("Cuadruplo %d (%s, %s, %s, Indice %d)", indiceCuadruplo, operador, operando1, operando2, indiceCuadruplo);
        cuadruplosGenerados.append(cuadruplo).append("\n");

        // Apilamos el índice resultante como operando para la siguiente operación
        operandos.push("Indice " + indiceCuadruplo);
    }
}

