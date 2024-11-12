/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compla2_hernandez.procesos;

import java.util.Stack;
import java.util.ArrayList;

public class Triplos {

    public static void generarTriplo(Stack<String> operadores, Stack<String> operandos, ArrayList<String> triplosGenerados, int indiceTriplo) {
        // Desapilamos los operandos y el operador
        String operando2 = operandos.pop();  // Segundo operando (desapilado)
        String operando1 = operandos.pop();  // Primer operando (desapilado)
        String operador = operadores.pop();  // Operador (desapilado)

        // Creamos el triplo como una cadena
        String triplo = String.format("Indice %d (%s, %s, %s)", indiceTriplo, operador, operando1, operando2);
        
        // Añadimos el triplo al ArrayList
        triplosGenerados.add(triplo);

        // Volvemos a apilar el resultado del triplo
        operandos.push("Indice " + indiceTriplo);
    }
}
