/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compla2_hernandez;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Archivos {

    public void openArchivo(String ruta) {
        try (FileReader fr = new FileReader(ruta);
             BufferedReader br = new BufferedReader(fr)) {
            String documento;
            while ((documento = br.readLine()) != null) {
                System.out.println(documento);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo");
        }
    }
}