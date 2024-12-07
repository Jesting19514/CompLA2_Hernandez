/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compla2_hernandez.procesos;

/**
 *
 * @author team1
 */
public class TablaDeTokens {

    private TablaDeTokens() {
    }

    public static int getNumero(String dato) {
        switch (dato) {

            case "+":
                return 10;
            case "-":
                return 11;
            case "*":
                return 12;
            case "/":
                return 13;
            case "^":
                return 13;
            case ":=":
                return 14;
            case "_":
                return 20;
            case ";":
                return 21;
            case ",":
                return 22;
            case "(":
                return 23;
            case ")":
                return 24;
            case ".":
                return 25;
            case "==":
                return 50;
            case "<>":
                return 51;
            case ">":
                return 52;
            case ">=":
                return 53;
            case "<":
                return 54;
            case "<=":
                return 55;
            case "!=":
                return 56;
             case "=":
                return 57;
            case "begin": 
                return 100;
            case "end": 
                return 101;
            case "const": 
                return 102;
            case "var": 
                return 103;
            case "procedure": 
                return 104;
            case "call": 
                return 105;
            case "if": 
                return 106;
            case "then": 
                return 107;
            case "else": 
                return 108;
            case "while": 
                return 109;
            case "do": 
                return 110;
            case "odd": 
                return 111;
            case "read": 
                return 111;
            default:
                if (dato.matches("[1-9]\\d*")) {   //digitos
                    return 2;
                } else if (dato.matches("[a-zA-Z][a-zA-Z0-9_]*")) { //identificadores
                    return 1;
                } else {
                    return -1;  //desconocido
                }
        }
    }
}
