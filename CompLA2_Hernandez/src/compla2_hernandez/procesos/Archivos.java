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
        String exp =  "(\\b(begin|end|const|var|procedure|call|if|then|else|while|do|odd|read)\\b)|" +
             "([a-zA-Z]\\w*)|([0-9]+)|(:=)|(>=|<=|==|!=|>|<|=)|([+\\-*/^])|([(),;\\.])";;
        Pattern patron = Pattern.compile(exp);
        Matcher matcher = patron.matcher(contenido);
//if while for
        while (matcher.find()) {
            String parte = matcher.group();
            int tokenNumber = TablaDeTokens.getNumero(parte);
            
            if (parte.matches("\\b(begin|end|const|var|procedure|call|if|then|else|while|do|odd|read|write)\\b")) {
                Lexema lexema = new Lexema(tokenNumber, parte, 0, "Palabra Reservada");
                Lexema.addLexema(lexema);
                continue;
            }

            if (parte.matches(">=|<=|==|!=|>|<|="))
                                                     {
                Lexema lexema = new Lexema(tokenNumber, parte, 0, "Operador Relacional");
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
            if (parte.matches(":=")) {
                Lexema lexema = new Lexema(tokenNumber, parte, 0, "Operador de Asignación");
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
            case "!":
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
    ArrayList<String> triplosGenerados = new ArrayList<>();  // Cambiado a ArrayList
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

    // Generar los triplos restantes
    while (!operadores.isEmpty()) {
        Triplos.generarTriplo(operadores, operandos, triplosGenerados, indiceTriplo++);
    }

    // Ahora triplosGenerados es un ArrayList, así que lo unimos en un solo String
    StringBuilder triplosFinales = new StringBuilder();
    for (String triplo : triplosGenerados) {
        triplosFinales.append(triplo).append("\n");
    }

    // Mostramos los triplos generados en la salida
    v.getTxtSalida().setText(triplosFinales.toString());
}

    public static void cuadruplos(Ventana v) {
    v.getTxtSalida().setText(""); // Limpiamos la salida

    ArrayList<Lexema> lexemas = asociaList(v);  // Obtenemos los lexemas
    Stack<String> operandos = new Stack<>();
    Stack<String> operadores = new Stack<>();
    ArrayList<String> cuadruplosGenerados = new ArrayList<>();  // Usamos ArrayList en lugar de StringBuilder
    int indiceCuadruplo = 1;

    for (Lexema lexema : lexemas) {
        String parte = lexema.getCadena();
        String tipo = lexema.getGrupo();

        if (tipo.equals("Número") || tipo.equals("Identificador")) {
            operandos.push(parte);  // Empujamos operandos
        } else if (parte.equals("(")) {
            operadores.push("(");  // Empujamos el paréntesis
        } else if (parte.equals(")")) {
            while (!operadores.isEmpty() && !operadores.peek().equals("(")) {
                Cuadruplos.generarCuadruplo(operadores, operandos, cuadruplosGenerados, indiceCuadruplo++);
            }
            operadores.pop();  // Desapilamos el paréntesis
        } else if (tipo.equals("Operador Aritmético") || tipo.equals("Separador")) {
            while (!operadores.isEmpty() && precedencia(operadores.peek()) >= precedencia(parte)) {
                Cuadruplos.generarCuadruplo(operadores, operandos, cuadruplosGenerados, indiceCuadruplo++);
            }
            operadores.push(parte);  // Empujamos el operador
        }
    }

    // Generamos los cuadruplos restantes
    while (!operadores.isEmpty()) {
        Cuadruplos.generarCuadruplo(operadores, operandos, cuadruplosGenerados, indiceCuadruplo++);
    }

    // Convertimos el ArrayList en un String para mostrarlo en la salida
    StringBuilder cuadruplosFinales = new StringBuilder();
    for (String cuadruplo : cuadruplosGenerados) {
        cuadruplosFinales.append(cuadruplo).append("\n");
    }

    // Mostramos los cuadruplos generados en la salida
    v.getTxtSalida().setText(cuadruplosFinales.toString());
}

    
public static void analizarCodigo(Ventana v) {
    try {
        System.out.println("Iniciando análisis...");
        ArrayList<Lexema> lexemas = Archivos.asociaList(v); // Generar lexemas
        System.out.println("Lexemas obtenidos: " + lexemas);
        Parser parser = new Parser(lexemas);               // Crear instancia del parser
        System.out.println("Parser inicializado.");
        parser.program();                                  // Iniciar análisis desde "program"
        System.out.println("Análisis completado.");
        
        // Obtén e imprime los cuádruplos generados
        ArrayList<String> cuadruplos = parser.getCuadruplos();
        if (!cuadruplos.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Cuádruplos generados:\n");
            for (String cuadruplo : cuadruplos) {
                sb.append(cuadruplo).append("\n");
            }
            System.out.println(sb.toString()); // Imprime en consola
            v.getTxtSalida().setText(sb.toString()); // Muestra en el componente de salida
        } else {
            System.out.println("No se generaron cuádruplos.");
            v.getTxtSalida().setText("No se generaron cuádruplos.");
        }
    } catch (Exception e) {
        System.err.println("Error en el análisis: " + e.getMessage());
        v.getTxtSalida().setText("Error: " + e.getMessage()); // Mostrar errores
    }
   
}

}