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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    Ventana v = new Ventana();

    public static String getContenido(String ruta) {
        String contenido = "";
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

    public static ArrayList<String> getList(Ventana v) {
        v.getTxtSalida().setText("");
        String contenido = v.getTxtContenido().getText().trim();
        String exp = "([a-zA-Z]\\w*)|([1-9]\\d*|0)|\\>=|\\<=|==|!=|>|<|\\+|\\-|\\*|\\/|\\(|\\)|;|,|\\.";
        Pattern patron = Pattern.compile(exp);
        Matcher matcher = patron.matcher(contenido);

        ArrayList<String> al = new ArrayList<String>();

        while (matcher.find()) {
            String parte = matcher.group();
            int tokenNumber = TablaDeTokens.getNumero(parte);

            if (parte.matches("[>=\\<=\\==\\!=\\>\\<]+")) {
                al.add(parte + " Operadores lógicos - Token: " + tokenNumber + "\n");
                continue;
            }
            if (parte.matches("[+\\-\\*\\/]+")) {
                al.add(parte + " Operadores aritméticos - Token: " + tokenNumber + "\n");
                continue;
            }
            if (parte.matches("[(\\)\\;\\,\\.]+")) {
                al.add(parte + " Separadores o agrupadores - Token: " + tokenNumber + "\n");
                continue;
            }
            if (parte.matches("\\d+")) {
                al.add(parte + " Números - Token: " + tokenNumber + "\n");
            } else if (parte.matches("[a-zA-Z]\\w*")) {
                al.add(parte + " Identificadores - Token: " + tokenNumber + "\n");
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
}
