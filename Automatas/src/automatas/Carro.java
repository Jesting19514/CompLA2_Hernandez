/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package automatas;

/**
 *
 * @author zzsglzz
 */
public class Carro {
    //Atributos
    String color="Verde";
    byte kilometraje;
    double rendimiento;
    //Metodos
    //Modificador {private-public-protected-default} + Tipo{static-void} + (argumentos)
    public void cambiarColor(String nuevoColor){
     this.color=nuevoColor;
     System.out.println("El color ahora es: " +this.color);
    }
    public void incrementoRendimiento(double cantidadAgrega){
        this.rendimiento+= cantidadAgrega;
        System.out.println("El rendimiento ahora es: " +this.rendimiento);
    }
    public void decrementaRendimiento(double cantidadElimina){
        this.rendimiento-= cantidadElimina;
        System.out.println("El rendimiento ahora es: " +this.rendimiento);
    }
}