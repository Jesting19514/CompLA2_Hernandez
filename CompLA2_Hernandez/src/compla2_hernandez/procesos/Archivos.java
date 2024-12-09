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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
       String exp = "(\\b(begin|end|const|var|procedure|call|if|then|else|while|do|odd|read)\\b)|" +
             "([a-zA-Z]\\w*)|([0-9]+)|(>=|<=|==|!=|>|<|=)|([+\\-*/^])|([(),;\\.])";
        Pattern patron = Pattern.compile(exp);
        Matcher matcher = patron.matcher(contenido);
//if while for
        while (matcher.find()) {
            String parte = matcher.group();
            int tokenNumber = TablaDeTokens.getNumero(parte);
            if (parte.matches("\\b(begin|end|const|var|procedure|call|if|then|else|while|do|odd|read)\\b")) {
                Lexema lexema = new Lexema(tokenNumber, parte, 0, "Palabra Reservada");
                Lexema.addLexema(lexema);
                continue;
            }
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
    v.getTxtSalida().setText("");  // Limpiamos la salida

    // Obtenemos los lexemas desde la ventana
    ArrayList<Lexema> lexemas = asociaList(v);
    Stack<String> operandos = new Stack<>();  // Pila de operandos
    Stack<String> operadores = new Stack<>();  // Pila de operadores
    Stack<Integer> incompletos = new Stack<>();  // Pila de saltos incompletos
    Stack<Integer> ciclos = new Stack<>();  // Pila de saltos para ciclos
    ArrayList<String> cuadruplosGenerados = new ArrayList<>();
    int indiceCuadruplo = 1;

    // Recorrer los lexemas para generar cuadruplos
    for (Lexema lexema : lexemas) {
        String parte = lexema.getCadena();
        String tipo = lexema.getGrupo();

        if (tipo.equals("Número") || tipo.equals("Identificador")) {
            operandos.push(parte);  // Empujamos operandos
        } else if (parte.equals("(")) {
            operadores.push("(");  // Empujamos el paréntesis
        } else if (parte.equals(")")) {
            // Se procesan los operadores hasta encontrar el paréntesis de apertura
            while (!operadores.isEmpty() && !operadores.peek().equals("(")) {
                Cuadruplos.generarCuadruplo(operadores, operandos, cuadruplosGenerados, indiceCuadruplo++, ciclos, incompletos);
            }
            operadores.pop();  // Desapilamos el paréntesis
        } else if (tipo.equals("Operador Aritmético") || tipo.equals("Operador Relacional")) {
            // Generamos cuadruplos mientras la precedencia lo permita
            while (!operadores.isEmpty() && precedencia(operadores.peek()) >= precedencia(parte)) {
                Cuadruplos.generarCuadruplo(operadores, operandos, cuadruplosGenerados, indiceCuadruplo++, ciclos, incompletos);
            }
            operadores.push(parte);  // Empujamos el operador
        } else if (parte.equals("if") || parte.equals("while")) {
            // Procesamos las estructuras de control
            procesarEstructuraControl(parte, operadores, operandos, cuadruplosGenerados, indiceCuadruplo++, ciclos, incompletos);
        } else if (parte.equals("then") || parte.equals("do")) {
            // En el caso de que sea "then" o "do", tenemos que generar el salto correspondiente
            if (parte.equals("then") && !incompletos.isEmpty()) {
                // Completar salto para "if"
                int salto = incompletos.pop();
                cuadruplosGenerados.set(salto - 1, String.format("(<=, %s, %s, Índice %d)", operandos.peek(), operandos.peek(), indiceCuadruplo));
            }
            if (parte.equals("do") && !ciclos.isEmpty()) {
                // Completar salto para "while"
                int salto = ciclos.pop();
                cuadruplosGenerados.set(salto - 1, String.format("(jmp, , , Índice %d)", indiceCuadruplo));
            }
        } else if (parte.equals(".")) {
            // Cuando encontramos el ".", agregamos el cuadruplo de retorno (ret)
            cuadruplosGenerados.add(String.format("(%s, , , )", "ret"));
        }
    }

    // Generamos los cuadruplos restantes
    while (!operadores.isEmpty()) {
        Cuadruplos.generarCuadruplo(operadores, operandos, cuadruplosGenerados, indiceCuadruplo++, ciclos, incompletos);
    }

    // Convertimos el ArrayList en un String para mostrarlo en la salida
    StringBuilder cuadruplosFinales = new StringBuilder();
    for (String cuadruplo : cuadruplosGenerados) {
        cuadruplosFinales.append(cuadruplo).append("\n");
    }

    // Mostramos los cuadruplos generados en la salida
    v.getTxtSalida().setText(cuadruplosFinales.toString());
}


private static void procesarEstructuraControl(String parte, Stack<String> operadores, Stack<String> operandos,
        ArrayList<String> cuadruplosGenerados, int indiceCuadruplo, Stack<Integer> ciclos, Stack<Integer> incompletos) {
    if (parte.equals("if")) {
        // Generamos el cuadruplo de comparación
        String resultadoCondicion = "R" + indiceCuadruplo;
        operandos.push(resultadoCondicion);
        cuadruplosGenerados.add(String.format("(CMP, %s, %s, %s)", operandos.peek(), operadores.peek(), resultadoCondicion));
        operadores.pop();
        incompletos.push(indiceCuadruplo);
    } else if (parte.equals("while")) {
        // Generamos el cuadruplo de salto a la condición del ciclo
        ciclos.push(indiceCuadruplo);
        incompletos.push(indiceCuadruplo);
        cuadruplosGenerados.add(String.format("(JMP, , , %s)", ciclos.peek()));
    }
}
public static void generarEnsambladorDesdeCuadruplos(Ventana v) {
    // Limpiamos la salida
    v.getTxtSalida().setText("");

    // Obtenemos los cuadruplos generados
    ArrayList<Lexema> lexemas = asociaList(v);
    Stack<String> operandos = new Stack<>();
    Stack<String> operadores = new Stack<>();
    Stack<Integer> incompletos = new Stack<>();
    Stack<Integer> ciclos = new Stack<>();
    ArrayList<String> cuadruplosGenerados = new ArrayList<>();
    int indiceCuadruplo = 1;

    // Generar los cuadruplos usando el método existente
    cuadruplos(v);

    // Aquí se transforma el ArrayList cuadruplosGenerados en ensamblador
    StringBuilder ensamblador = new StringBuilder();

    for (String cuadruplo : cuadruplosGenerados) {
        String[] partes = cuadruplo.replace("(", "").replace(")", "").split(",");
        String operacion = partes[0].trim();
        String op1 = partes.length > 1 ? partes[1].trim() : "";
        String op2 = partes.length > 2 ? partes[2].trim() : "";
        String resultado = partes.length > 3 ? partes[3].trim() : "";

        switch (operacion) {
            case "+":
                ensamblador.append(String.format("MOV AX, %s\nADD AX, %s\nMOV %s, AX\n", op1, op2, resultado));
                break;
            case "-":
                ensamblador.append(String.format("MOV AX, %s\nSUB AX, %s\nMOV %s, AX\n", op1, op2, resultado));
                break;
            case "*":
                ensamblador.append(String.format("MOV AX, %s\nMUL %s\nMOV %s, AX\n", op1, op2, resultado));
                break;
            case "/":
                ensamblador.append(String.format("MOV AX, %s\nMOV DX, 0\nDIV %s\nMOV %s, AX\n", op1, op2, resultado));
                break;
            case "CMP":
                ensamblador.append(String.format("CMP %s, %s\n", op1, op2));
                break;
            case "JMP":
                ensamblador.append(String.format("JMP %s\n", resultado));
                break;
            case "JE":
                ensamblador.append(String.format("JE %s\n", resultado));
                break;
            case "RET":
                ensamblador.append("RET\n");
                break;
            default:
                ensamblador.append(String.format("; Operación no reconocida: %s\n", cuadruplo));
        }
    }

    // Guardar el código ensamblador en un archivo .asm
    guardarEnsambladorEnArchivo(ensamblador.toString(), v);
}

public static void guardarEnsambladorEnArchivo(String ensamblador, Ventana v) {
    try {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar archivo ensamblador");
        int userSelection = chooser.showSaveDialog(v);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
                writer.write(ensamblador);
            }
            v.getTxtSalida().setText("Archivo ensamblador guardado en: " + archivo.getAbsolutePath());
        }
    } catch (IOException e) {
        v.getTxtSalida().setText("Error al guardar el archivo ensamblador.");
    }
}


}



    