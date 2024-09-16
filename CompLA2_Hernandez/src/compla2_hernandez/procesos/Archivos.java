package compla2_hernandez.procesos;

import compla2_hernandez.ventanas.Ventana;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;

public class Archivos {

    public static String procesaContenido() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    Ventana v = new Ventana();

    public static String getContenido(String ruta) {
        String contenido = ""; // Reinicia contenido antes de leer
        try (FileReader fr = new FileReader(ruta); BufferedReader br = new BufferedReader(fr)) {
            String documento;
            while ((documento = br.readLine()) != null) {
                contenido += documento + "\n";
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo");
        }
        return contenido;
    }

    public static File getFile(Ventana v) {
        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showOpenDialog(v);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }
     
    /*public static void procesaContenido(Ventana v) {
        String contenido = v.getTxtContenido().getText().trim();
        String[] partes = contenido.split("");
        int caracteres = 0, numpalabras = 0, lineas = 0;
        for (String parte : partes) {
            for (char c : contenido.toCharArray()) {
                if (Character.isLetter(c)) {
                    caracteres++;
                }
            }
            String[] nlineas = contenido.split("\n");
            lineas = nlineas.length;
            for (String linea : nlineas) {
                String[] palabrasLinea = linea.trim().split("\\s+");
               if (!linea.trim().isEmpty()) {
                numpalabras += palabrasLinea.length;
        }
            }

        }
        v.getTxtSalida().append("Caracteres: " + caracteres + "\nPalabras: " + numpalabras + "\nLineas: " + lineas);
    */
    public static void procesaContenido(Ventana v) {
        String contenido = v.getTxtContenido().getText().trim();
        String exp = "([a-zA-Z]\\w*)|([1-9]\\d*|[0])|\\+|\\-|\\*|\\/|:=|_|;|,|\\(|\\)|==|<>|>=|>|<=|<|(\\.)";
        Pattern patron = Pattern.compile(exp);
        Matcher matcher = patron.matcher(contenido);
         while (matcher.find()) { 
            String parte = matcher.group(); 
            if (parte.matches("[\\+\\-\\*/:=_;,\\(\\)<>\\.]+")) {
                continue;
            }
            if (parte.matches("\\d+")) {               
                v.getTxtSalida().append("num " + parte + "\n");
            } 
            else if (parte.matches("[a-zA-Z]\\w*")) {
                v.getTxtSalida().append("ident " + parte + "\n");
}
         }
        /*int suma=0;   
        String[] partes = contenido.split("");
        for (String parte : partes) {
           try {
                int valor = Integer.parseInt(parte); 
                suma += valor; 
            } catch (NumberFormatException e) {
              System.err.println(e);
            }
        } 
        String cadena = Integer.toString(suma);
        v.getTxtSalida().setText(cadena);
        */
    }
}


