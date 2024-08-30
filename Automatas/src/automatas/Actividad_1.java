/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package automatas;

import automatas.vehiculos.*;
import java.math.BigInteger;

/**
 *
 * @author team1
 */
public class Actividad_1 {
    public static void main(String[] args) {
        System.out.println("Hola Mundo");
        int numeroEntero = 10;
        float numeroDecimal = 10.5f ;
        BigInteger numeroGrande = new BigInteger("12345678910111213141516");
        String cadena = "Edgar Essaul";
        System.out.println(cadena+(" ")+numeroEntero+(" ")+numeroDecimal+(" ")+numeroGrande);
        Carro bocho= new Carro("morado");
        Carro tesla = new Carro(5);
    } 
}
