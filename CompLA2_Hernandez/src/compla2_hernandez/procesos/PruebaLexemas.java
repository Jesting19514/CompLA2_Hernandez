/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compla2_hernandez.procesos;

import java.util.ArrayList;

/**
 *
 * @author team1
 */
public class PruebaLexemas {
    public static void main(String[] args) {
        ArrayList<Lexema> arrLexemas = new ArrayList();
        //token = buscaToken(String);
        arrLexemas.add(new Lexema(1,"hola",0,"Identificadores") );
        arrLexemas.add(new Lexema (10,";",0,"Separador"));
        for (Lexema Lexema : arrLexemas) {
            System.out.println(Lexema);
        }
    }
}
