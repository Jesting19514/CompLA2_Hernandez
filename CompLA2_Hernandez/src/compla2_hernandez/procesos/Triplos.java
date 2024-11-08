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
public class Triplos {

    public static void generarTriplo(Stack<String> operadores, Stack<String> operandos, StringBuilder triplosGenerados, int indiceTriplo) {
        String operando2 = operandos.pop();  // Segundo operando (desapilado)
        String operando1 = operandos.pop();  // Primer operando (desapilado)
        String operador = operadores.pop();  // Operador (desapilado)

        String triplo = String.format("Indice %d (%s, %s, %s)", indiceTriplo, operador, operando1, operando2);
        triplosGenerados.append(triplo).append("\n");

        operandos.push("Indice " + indiceTriplo);
    }
}

