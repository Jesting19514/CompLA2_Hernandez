/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compla2_hernandez.procesos;

import java.util.ArrayList;
import java.util.Stack;

public class Cuadruplos {

    public static void generarCuadruplo(Stack<String> operadores, Stack<String> operandos, ArrayList<String> cuadruplosGenerados, int indiceCuadruplo) {
       
        String operando2 = operandos.pop();  
        String operando1 = operandos.pop();  
        String operador = operadores.pop();  

        String excepcion = " ";
        if (operador.equals("=") || operador.equals("<") || operador.equals("<=") || operador.equals(">") 
                || operador.equals(">=") || operador.equals("!=") || operador.equals("==")) {
            excepcion = " ";
        }
        
        
        String cuadruplo = String.format("Indice %d (%s, %s, %s, %s)", indiceCuadruplo, operador, operando1, operando2, excepcion);
        
        
        cuadruplosGenerados.add(cuadruplo);

        operandos.push("R" + indiceCuadruplo);
    }
}
