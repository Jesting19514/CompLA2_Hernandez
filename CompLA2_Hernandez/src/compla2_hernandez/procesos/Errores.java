/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compla2_hernandez.procesos;

/**
 *
 * @author team1
 */
public class Errores {
    private void Error(int ErrorCode) {
        boolean hasError = true;
        switch (ErrorCode) {
            case 1:
                System.out.println("Se esperaba start");
                break;
            case 2:
                System.out.println("Se esperaba finish");
                break;
            case 3:
                System.out.println("Se esperaba ;");
                break;
            case 4:
                System.out.println("Se esperaba int o str");
                break;
            case 5:
                System.out.println("Se esperaba )");
                break;
            case 6:
                System.out.println("Se esperaba + ");
                break;
            case 7:
                System.out.println("Se esperaba - ");
                break;
            case 8:
                System.out.println("Se esperaba Identificador");
                break;
            case 9:
                System.out.println("Se esperaba Identificador o numero");
                break;
            case 10:
                System.out.println("Se esperaba * ");
                break;
            case 11:
                System.out.println("Se esperaba / ");
                break;
            case 12:
                System.out.println("Se esperaba begin ");
                break;
            case 13:
                System.out.println("Se esperaba end ");
                break;
            case 14:
                System.out.println("Se esperaba := ");
                break;
            case 15:
                System.out.println("Se esperaba input ");
                break;
            case 16:
                System.out.println("Se esperaba output");
                break;
            case 17:
                System.out.println("Se esperaba if");
                break;
            case 18:
                System.out.println("Se esperaba then");
                break;
            case 19:
                System.out.println("Se esperaba loop");
                break;
            case 20:
                System.out.println("Se esperaba repeat");
                break;
            case 21:
                System.out.println("Se esperaba else ");
                break;
            case 22:
                System.out.println("Se esperaba , ");
                break;
            case 23:
                System.out.println("Se esperaba ==");
                break;
            case 24:
                System.out.println("Se esperaba <>");
                break;
            case 25:
                System.out.println("Se esperaba >");
                break;
            case 26:
                System.out.println("Se esperaba >=");
                break;
            case 27:
                System.out.println("Se esperaba <");
                break;
            case 28:
                System.out.println("Se esperaba <=");
                break;
            default:
                System.out.println("Error desconocido");
                break;
        }
    }

}
