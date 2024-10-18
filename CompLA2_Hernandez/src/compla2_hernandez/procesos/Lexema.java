package compla2_hernandez.procesos;

import compla2_hernandez.ventanas.Ventana;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexema {
   private int token;
   private String cadena;
   private int valor;
   private String grupo;

 
   private static ArrayList<Lexema> arrLexema = new ArrayList<Lexema>();

   public Lexema(int token, String cadena, int valor, String grupo) {
       this.token = token;
       this.cadena = cadena;
       this.valor = valor;
       this.grupo = grupo;
   }

   
   public int getToken() {
       return token;
   }

   public void setToken(int token) {
       this.token = token;
   }

   public String getCadena() {
       return cadena;
   }

   public void setCadena(String cadena) {
       this.cadena = cadena;
   }

   public int getValor() {
       return valor;
   }

   public void setValor(int valor) {
       this.valor = valor;
   }

   public String getGrupo() {
       return grupo;
   }

   public void setGrupo(String grupo) {
       this.grupo = grupo;
   }

   public static void addLexema(Lexema lexema) {
       arrLexema.add(lexema);
   }

   public static ArrayList<Lexema> getArrLexema() {
       return arrLexema;
   }

   @Override
   public String toString() {
       return "Token: " + token + ", Cadena: " + cadena + ", Valor: " + valor + ", Grupo: " + grupo;
   }
}
