/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package compla2_hernandez;

import compla2_hernandez.procesos.Archivos;
import compla2_hernandez.ventanas.Ventana;

/**
 *
 * @author team1
 */
public class Principal {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Ventana v = new Ventana();
        v.setVisible(true);
        Archivos a = new Archivos();
        String x = (a.openArchivo("C:\\Users\\team1\\Desktop\\pruebas.txt")); 
        System.out.print(x);
    }
}
