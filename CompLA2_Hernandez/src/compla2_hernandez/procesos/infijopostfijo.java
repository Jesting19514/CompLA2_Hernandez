/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compla2_hernandez.procesos;

/**
 *
 * @author team1
 */
import java.util.Stack;

public class infijopostfijo {
    public static int getPrecedence(char ch) {
        switch (ch) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
        }
        return -1;
    }

    public static String infixToPostfix(String expression) {
        StringBuilder result = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            // Si el carácter es un operando, agrégalo al resultado
            if (Character.isLetterOrDigit(c)) {
                result.append(c);
            } 
            // Si el carácter es '(', agrégalo al stack
            else if (c == '(') {
                stack.push(c);
            } 
            // Si el carácter es ')', desempila y agrégalo al resultado hasta encontrar '('
            else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    result.append(stack.pop());
                }
                stack.pop();
            } 
            // Si es un operador
            else {
                while (!stack.isEmpty() && getPrecedence(c) <= getPrecedence(stack.peek())) {
                    result.append(stack.pop());
                }
                stack.push(c);
            }
        }

        // Desempila todos los operadores restantes
        while (!stack.isEmpty()) {
            result.append(stack.pop());
        }

        return result.toString();
    }

    public static void main(String[] args) {
        String expression = "r-(m+3*5)*20=x/m";
        System.out.println("Postfijo: " + infixToPostfix(expression));
    }
}
