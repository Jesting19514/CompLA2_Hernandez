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
import java.util.Stack;

public class Archivos {

    private static ArrayList<String> al = new ArrayList<String>();
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

    public static ArrayList<Lexema> getList(Ventana v) {
        v.getTxtSalida().setText("");
        String contenido = v.getTxtContenido().getText().trim();
        String exp = "([a-zA-Z]\\w*)|([1-9]\\d*|0)|\\>=|\\<=|==|!=|>|<|\\+|\\-|\\*|\\/|\\(|\\)|;|,|\\.|\\^|\\=";
        Pattern patron = Pattern.compile(exp);
        Matcher matcher = patron.matcher(contenido);

        while (matcher.find()) {
            String parte = matcher.group();
            int tokenNumber = TablaDeTokens.getNumero(parte);

            if (parte.matches("[>=\\<=\\==\\!=\\>\\<\\=]+")) {
                Lexema lexema = new Lexema(tokenNumber, parte, 0, "Separador");
                Lexema.addLexema(lexema);
                continue;
            }
            if (parte.matches("[+\\-\\*\\/\\^]+")) {
                Lexema lexema = new Lexema(tokenNumber, parte, 0, "Operador Aritmético");
                Lexema.addLexema(lexema);
                continue;
            }
            if (parte.matches("[(\\)\\;\\,\\.]+")) {
                Lexema lexema = new Lexema(tokenNumber, parte, 0, "Agrupador");
                Lexema.addLexema(lexema);
                continue;
            }
            if (parte.matches("\\d+")) {
                Lexema lexema = new Lexema(tokenNumber, parte, 0, "Número");
                Lexema.addLexema(lexema);
            } else if (parte.matches("[a-zA-Z]\\w*")) {
                Lexema lexema = new Lexema(tokenNumber, parte, 0, "Identificador");
                Lexema.addLexema(lexema);
            }
        }

        return Lexema.getArrLexema();
    }

    public static ArrayList<Lexema> asociaList(Ventana v) {
        Lexema.limpiarLista();
        ArrayList<Lexema> list = getList(v);
        for (Lexema lexema : list) {
            v.getTxtSalida().append(lexema.toString() + "\n");
        }
        return list;
    }

    private static int precedencia(String operador) {
        switch (operador) {
            case "^":
                return 4;
            case "*":
            case "/":
                return 3;
            case "+":
            case "-":
                return 2;
            case "=":
            case "<":
            case "<=":
            case ">":
            case ">=":
            case "!=":
            case "==":
                return 1;
            default:
                return -1;
        }
    }

    public static void triplos(Ventana v) {
        v.getTxtSalida().setText("");
        ArrayList<Lexema> lexemas = asociaList(v);  
        Stack<String> operandos = new Stack<>();
        Stack<String> operadores = new Stack<>();
        StringBuilder triplosGenerados = new StringBuilder();
        int indiceTriplo = 1;

        for (Lexema lexema : lexemas) {
            String parte = lexema.getCadena();
            String tipo = lexema.getGrupo();

            if (tipo.equals("Número") || tipo.equals("Identificador")) {
                operandos.push(parte);
            } else if (parte.equals("(")) {
                operadores.push("(");
            } else if (parte.equals(")")) {

                while (!operadores.isEmpty() && !operadores.peek().equals("(")) {
                    Triplos.generarTriplo(operadores, operandos, triplosGenerados, indiceTriplo++);
                }
                operadores.pop();
            } else if (tipo.equals("Operador Aritmético") || tipo.equals("Separador")) {

                while (!operadores.isEmpty() && precedencia(operadores.peek()) >= precedencia(parte)) {
                    Triplos.generarTriplo(operadores, operandos, triplosGenerados, indiceTriplo++);
                }
                operadores.push(parte);
            }
        }

        while (!operadores.isEmpty()) {
            Triplos.generarTriplo(operadores, operandos, triplosGenerados, indiceTriplo++);
        }

        v.getTxtSalida().setText(triplosGenerados.toString());
    }

    public static void cuadruplos(Ventana v) {

        v.getTxtSalida().setText("");

        ArrayList<Lexema> lexemas = asociaList(v);
        Stack<String> operandos = new Stack<>();
        Stack<String> operadores = new Stack<>();
        StringBuilder cuadruplosGenerados = new StringBuilder();
        int indiceCuadruplo = 1;

        for (Lexema lexema : lexemas) {
            String parte = lexema.getCadena();
            String tipo = lexema.getGrupo();

            if (tipo.equals("Número") || tipo.equals("Identificador")) {
                operandos.push(parte);
            } else if (parte.equals("(")) {
                operadores.push("(");
            } else if (parte.equals(")")) {

                while (!operadores.isEmpty() && !operadores.peek().equals("(")) {
                    Cuadruplos.generarCuadruplo(operadores, operandos, cuadruplosGenerados, indiceCuadruplo++);
                }
                operadores.pop();
            } else if (tipo.equals("Operador Aritmético") || tipo.equals("Separador")) {

                while (!operadores.isEmpty() && precedencia(operadores.peek()) >= precedencia(parte)) {
                    Cuadruplos.generarCuadruplo(operadores, operandos, cuadruplosGenerados, indiceCuadruplo++);
                }
                operadores.push(parte);
            }
        }

        while (!operadores.isEmpty()) {
            Cuadruplos.generarCuadruplo(operadores, operandos, cuadruplosGenerados, indiceCuadruplo++);
        }

        v.getTxtSalida().setText(cuadruplosGenerados.toString());
    }

}
