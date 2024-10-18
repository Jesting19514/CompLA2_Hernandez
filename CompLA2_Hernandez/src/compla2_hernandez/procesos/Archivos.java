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

  public static ArrayList<Lexema> getList(Ventana v) {
    v.getTxtSalida().setText(""); 
    String contenido = v.getTxtContenido().getText().trim();
    String exp = "([a-zA-Z]\\w*)|([1-9]\\d*|0)|\\>=|\\<=|==|!=|>|<|\\+|\\-|\\*|\\/|\\(|\\)|;|,|\\.";
    Pattern patron = Pattern.compile(exp);
    Matcher matcher = patron.matcher(contenido);

    while (matcher.find()) {
        String parte = matcher.group();
        int tokenNumber = TablaDeTokens.getNumero(parte);

        if (parte.matches("[>=\\<=\\==\\!=\\>\\<]+")) {
            Lexema lexema = new Lexema(tokenNumber, parte, 0, "Separador");
            Lexema.addLexema(lexema);
            continue;
        }
        if (parte.matches("[+\\-\\*\\/]+")) {
            Lexema lexema = new Lexema(tokenNumber, parte, 0, "Operador Aritmético");
            Lexema.addLexema(lexema);
            continue;
        }
        if (parte.matches("[(\\)\\;\\,\\.]+")) {
            Lexema lexema = new Lexema(tokenNumber, parte, 0, "Separador o Agrupador");
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


public static void asociaList(Ventana v) {
    ArrayList<Lexema> list = getList(v);
    for (Lexema lexema : list) {
        v.getTxtSalida().append(lexema.toString() + "\n");  
    }
}   

 private static int precedencia(char operador) {
        switch (operador) {
            case '^':
                return 4;
            case '*':
            case '/':
                return 3;
            case '+':
            case '-':
                return 2;
            case '=':
                return 1;
            case '(':
            case ')':
                return 0; // Los paréntesis no afectan la precedencia
            default:
                return -1; // Cualquier otro carácter no reconocido
        }
}

    private static String convertirInfijoAPostfijo(String expresion) {
    StringBuilder resultado = new StringBuilder();
    Stack<Character> pila = new Stack<>();
    
    for (int i = 0; i < expresion.length(); i++) {
        char caracter = expresion.charAt(i);
        
        // Si el carácter es un operando (letras o números)
        if (Character.isLetterOrDigit(caracter)) {
            resultado.append(caracter);
        } 
        // Si es un paréntesis de apertura
        else if (caracter == '(') {
            pila.push(caracter);
        } 
        // Si es un paréntesis de cierre
        else if (caracter == ')') {
            while (!pila.isEmpty() && pila.peek() != '(') {
                resultado.append(pila.pop());
            }
            if (!pila.isEmpty() && pila.peek() == '(') {
                pila.pop(); // Eliminar el '(' de la pila
            } else {
                return "Error: falta paréntesis de apertura.";
            }
        } 
        // Si es un operador
        else {
            // Manejar el operador '=' de manera especial: debería ser procesado al final.
            if (caracter == '=') {
                // Apilamos el '=' al final, ya que tiene la precedencia más baja.
                while (!pila.isEmpty() && pila.peek() != '(') {
                    resultado.append(pila.pop());
                }
                pila.push(caracter);
            } else {
                // Para otros operadores, respetamos la precedencia
                while (!pila.isEmpty() && precedencia(caracter) <= precedencia(pila.peek())) {
                    resultado.append(pila.pop());
                }
                pila.push(caracter);
            }
        }
    }
    
    // Desapilar cualquier operador restante
    while (!pila.isEmpty()) {
        if (pila.peek() == '(') {
            return "Error: falta paréntesis de cierre.";
        }
        resultado.append(pila.pop());
    }
    
    return resultado.toString();
}

    
    
    public static void infijoPostfijo(Ventana v) {
        v.getTxtSalida().setText(""); 
        String contenido = v.getTxtContenido().getText().trim();
        
        if (!contenido.isEmpty()) {
            String postfijo = convertirInfijoAPostfijo(contenido);
            v.getTxtSalida().setText(postfijo); // Mostrar la expresión postfija en el campo de salida
        }
    }

    public static void triplos(Ventana v) {
        v.getTxtSalida().setText(""); 
        String contenido = v.getTxtContenido().getText().trim();
        
        if (contenido.isEmpty()) {
            v.getTxtSalida().setText("Error: La expresión está vacía.");
            return;
        }

        Stack<String> operandos = new Stack<>();  // Pila de operandos
        Stack<Character> operadores = new Stack<>();  // Pila de operadores
        StringBuilder triplosGenerados = new StringBuilder(); // Para almacenar los triplos generados
        int indiceTriplo = 1; // Contador para los triplos
        int i = 0;

        while (i < contenido.length()) {
            char caracter = contenido.charAt(i);

            // Si el carácter es un número o una letra (operando), lo añadimos a la pila de operandos
            if (Character.isLetterOrDigit(caracter)) {
                StringBuilder numeroCompleto = new StringBuilder();
                
                // Agrupamos dígitos consecutivos (para manejar números de más de un dígito como "100")
                while (i < contenido.length() && Character.isDigit(contenido.charAt(i))) {
                    numeroCompleto.append(contenido.charAt(i));
                    i++;
                }
                
                // Si es una letra (como variables), la añadimos individualmente
                if (Character.isLetter(caracter)) {
                    operandos.push(String.valueOf(caracter));
                    i++;
                } else {
                    // Para números completos, añadimos la cadena completa
                    operandos.push(numeroCompleto.toString());
                }
                continue; // Evitamos incrementar `i` aquí ya que ya lo hemos hecho en el bucle interno
            } 
            // Si es un paréntesis de apertura
            else if (caracter == '(') {
                operadores.push(caracter);
            } 
            // Si es un paréntesis de cierre
            else if (caracter == ')') {
                while (!operadores.isEmpty() && operadores.peek() != '(') {
                    generarTriplo(operadores, operandos, triplosGenerados, indiceTriplo++);
                }
                operadores.pop(); // Eliminamos el '('
            } 
            // Si es un operador
            else {
                // Procesamos los operadores con mayor o igual precedencia antes de apilar el nuevo operador
                while (!operadores.isEmpty() && precedencia(operadores.peek()) >= precedencia(caracter)) {
                    generarTriplo(operadores, operandos, triplosGenerados, indiceTriplo++);
                }
                operadores.push(caracter);
            }
            i++;
        }

        // Procesamos los operadores restantes en la pila
        while (!operadores.isEmpty()) {
            generarTriplo(operadores, operandos, triplosGenerados, indiceTriplo++);
        }

        // Mostrar el resultado de los triplos en el campo de salida
        v.getTxtSalida().setText(triplosGenerados.toString());
    }

    // Método para generar un triplo con dos operandos y un operador
    private static void generarTriplo(Stack<Character> operadores, Stack<String> operandos, StringBuilder triplosGenerados, int indiceTriplo) {
        if (operandos.size() < 2) {
            throw new IllegalStateException("Error: No hay suficientes operandos para generar un triplo.");
        }

        String operando2 = operandos.pop();  // Segundo operando (desapilado)
        String operando1 = operandos.pop();  // Primer operando (desapilado)
        char operador = operadores.pop();    // Operador (desapilado)

        // Generar triplo en el formato: Índice (operador, operando1, operando2)
        String triplo = String.format("Indice %d (%c, %s, %s)", indiceTriplo, operador, operando1, operando2);
        triplosGenerados.append(triplo).append("\n");

        // Apilamos el índice resultante como operando
        operandos.push("Indice " + indiceTriplo);
    }

    // Método que devuelve la precedencia de los operadores
}




