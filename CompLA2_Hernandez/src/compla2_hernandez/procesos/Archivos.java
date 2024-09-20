package compla2_hernandez.procesos;

import compla2_hernandez.ventanas.Ventana;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import java.util.ArrayList;

public class Archivos {

    private static ArrayList<String> al = new ArrayList<String>();

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
    public static ArrayList<String> getList(Ventana v) {
        v.getTxtSalida().setText("");
        String contenido = v.getTxtContenido().getText().trim();
        String exp = "([a-zA-Z]\\w*)|([1-9]\\d*|0)|\\>=|\\<=|==|!=|>|<|\\+|\\-|\\*|\\/|\\(|\\)|;|,|\\.";
        Pattern patron = Pattern.compile(exp);
        Matcher matcher = patron.matcher(contenido);

        ArrayList<String> al = new ArrayList<String>();

        while (matcher.find()) {
            String parte = matcher.group();

            if (parte.matches("[>=\\<=\\==\\!=\\>\\<]+")) {
                al.add(parte + " Operadores logicos " + "\n");
                continue;
            }
            if (parte.matches("[+\\-\\*\\/]+")) {
                al.add(parte + " Operadores aritmeticos " + "\n");
                continue;
            }
            if (parte.matches("[(\\)\\;\\,\\.\\:]+")) {
                al.add(parte + " Separadores o agrupadores " + "\n");
                continue;
            }
            if (parte.matches("\\d+")) {
                al.add(parte + " numeros " + "\n");
            } else if (parte.matches("[a-zA-Z]\\w*")) {
                al.add(parte + " identificadores " + "\n");
            }
        }

        return al;
    }

    public static void asociaList(Ventana v) {
        ArrayList<String> list = getList(v);
        for (String item : list) {
            v.getTxtSalida().append(item);
        }
    }

   /* public static void calculaConejos(Ventana v) {
        String generacion = v.getTxtContenido().getText();
        int gen = Integer.parseInt(generacion);
        int conejos = 2;
        int total = 0;
        int muertos = 2;
        int muertost;
        int m = 0;
        if (gen < 5) {
            for (int i = 0; i < gen; i++) {
                total = conejos * 3;
                conejos = total;
            }
        } else {
            for (int i = 0; i < gen; i++) {
                total = (conejos * 3);
                conejos = total;
            }
            muertos = muertos * 3;
            conejos = conejos - muertos;
        }
        v.getTxtSalida().setText(conejos + "");
    }*/
}
