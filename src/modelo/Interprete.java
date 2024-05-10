/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/**
 *
 * @author Usuario
 */
public class Interprete {

    //Atributos
    private Hashtable<String, Value> dicc;
    private String arr[][];
    private Stack<String> aritmeticos;
    private Stack<String> logicos;

    //Constructor
    public Interprete() {
        dicc = new Hashtable<>();
        arr = leerTXT();
        aritmeticos = new Stack<>();
        logicos = new Stack<>();
    }
    
    public String[][] leerTXT() {
        String nombreArchivo = "C:\\Users\\57300\\Documents\\JULIAN\\U\\V\\TIC\\ArchivosComp-Inter\\DirectionChangeSalida.out";
        List<String> lineas = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.length() > 0) {
                    lineas.add(limpiarTxt(linea));
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        
        return inicializarArr(lineas);
    }
    
    private String[][] inicializarArr(List<String> lineas) {
        String arreglo[][] = new String[lineas.size()][3];
        
        for (int i = 0; i < lineas.size(); i++) {
            String arr[] = lineas.get(i).split("  ");
            arreglo[i][0] = arr[0];
            arreglo[i][1] = arr[1];
            arreglo[i][2] = arr[2];
        }
        return arreglo;
    }
    
    private String limpiarTxt(String linea) {
        return linea.replaceAll("\\[", "").replaceAll("\\]", "");
    }
    
    public void interpretar() {
        int i = 1;
        while (!arr[i][1].equals("end")) {
            if (arr[i][1].contains("int ") || arr[i][1].contains("double ") || arr[i][1].contains("String ") || arr[i][1].contains("boolean ") || arr[i][1].contains("char ")) {
                declararVariables(arr[i][1]);
                i++;
            } else if (arr[i][1].equals("jump")) {
                i = Integer.parseInt(arr[i][2]);
            } else if (arr[i][1].contains("read")) {
                read(arr[i][1]);
                i++;
            } else if (arr[i][1].contains("print")) {
                print(arr[i][1]);
                i++;
            } else if ((esOperacionA(arr[i][1])) && !esOperacioB(arr[i][1])) {
                if (arr[i][1].contains(" = ") && !arr[i][2].equals("-")) {
                    String auxi[] = arr[i][1].split(" = ");
                    Value value = new Value(dicc.get(auxi[0]).getType(), evaluador(auxi[1]));
                    dicc.put(auxi[0], value);
                    i = Integer.parseInt(arr[i][2]);
                } else if (arr[i][1].contains(" = ") && arr[i][2].equals("-")) {
                    String auxi[] = arr[i][1].split(" = ");
                    Value value = new Value(dicc.get(auxi[0]).getType(), evaluador(auxi[1]));
                    dicc.put(auxi[0], value);
                    i++;
                }
            } else if (esOperacioB(arr[i][1])) {
                if (arr[i][1].contains(" = ")) {
                    String auxi[] = arr[i][1].split(" = ");
                    Value value = new Value(dicc.get(auxi[0]).getType(), evaluador(auxi[1]));
                    dicc.put(auxi[0], value);
                    i++;
                } else {
                    //System.out.println(arr[i][1]);
                    String resultado = evaluador(arr[i][1]);
                    if (resultado.equals("true")) {
                        i++;
                    } else if (resultado.equals("false")) {
                        i = Integer.parseInt(arr[i][2]);
                    }
                }
            } else if (!esOperacionA(arr[i][1]) && !esOperacioB(arr[i][1])) {
                String chr[] = arr[i][1].split(" = ");
                if (dicc.get(chr[0]).getType().equals("char")) {
                    String index;
                    index = chr[1].replaceAll("'", "");
                    if (index.length() == 1) {
                        Value value = new Value(dicc.get(chr[0]).getType(), index);
                        dicc.put(chr[0], value);
                        i++;
                    } else {
                        System.err.println("Dato incompatible");
                        System.exit(1);
                    }
                } else if (dicc.get(chr[0]).getType().equals("String")) {
                    String index;
                    index = chr[1].replaceAll("\"", "");
                    Value value = new Value(dicc.get(chr[0]).getType(), index);
                    dicc.put(chr[0], value);
                    i++;
                    
                } else if (arr[i][1].contains(" = ")) {
                    String auxi[] = arr[i][1].split(" = ");
                    if (esVariable(auxi[1])) {
                        Value value = new Value(dicc.get(auxi[0]).getType(), dicc.get(auxi[1]).getValue());
                        dicc.put(auxi[0], value);
                    } else {
                        Value value = new Value(dicc.get(auxi[0]).getType(), auxi[1]);
                        dicc.put(auxi[0], value);
                    }
                    i++;
                }
                
            }
            //System.out.println(dicc.toString());
        }
    }
    
    private void declararVariables(String cadena) {
        String declarar[] = cadena.split(" ");
        switch (declarar[0]) {
            case "int" ->
                dicc.put(declarar[1], new Value("int", "0"));
            case "double" ->
                dicc.put(declarar[1], new Value("double", "0.0"));
            case "String" ->
                dicc.put(declarar[1], new Value("String", ""));
            case "boolean" ->
                dicc.put(declarar[1], new Value("boolean", "false"));
            case "char" ->
                dicc.put(declarar[1], new Value("char", "''"));
        }
    }
    
    private void print(String cadena) {
        String cad = cadena.substring(6, cadena.length() - 1);
        if (cad.contains("+")) {
            String arreglo[] = cad.split("\\+");
            String salida = "";
            for (int i = 0; i < arreglo.length; i++) {
                if (!(arreglo[i].contains("\"") || arreglo[i].contains("\\“") || arreglo[i].contains("\\”"))) {
                    if (dicc.get(arreglo[i]).getType().equals("int")) {
                        //System.out.println(arreglo[0].replaceAll("\"", "").replaceAll("\\“", "").replaceAll("\\”", "") + " " + (int)Double.parseDouble(dicc.get(arreglo[i]).getValue()));
                        salida += (int) Double.parseDouble(dicc.get(arreglo[i]).getValue()) + " ";
                    } else {
                        salida += dicc.get(arreglo[i]).getValue() + " ";
                    }
                } else {
                    
                    salida += arreglo[i].replaceAll("\"", "") + " ";
                }
            }
            //salida = salida.substring(0,salida.length()-1);
            System.out.println(salida);
        } else if (dicc.containsKey(cad)) {
            if (dicc.get(cad).getType().equals("int")) {
                System.out.println((int) Double.parseDouble(dicc.get(cad).getValue()));
            } else {
                System.out.println(dicc.get(cad).getValue());
            }
        } else if (cadena.equals("print()")) {
            System.out.println("");
        } else {
            System.out.println(cad.replaceAll("\"", "").replaceAll("\\“", "").replaceAll("\\”", ""));
        }
    }
    
    private void read(String cadena) {
        Scanner t = new Scanner(System.in);
        String index;
        String aux[] = cadena.split("\\(");
        index = aux[1].substring(0, aux[1].length() - 1);
        if (dicc.containsKey(index)) {
            switch (dicc.get(index).getType()) {
                case "int":
                    int a;
                    System.out.println("Ingrese el valor de la variable " + index + "(tipo " + dicc.get(index).getType() + "): ");
                    a = t.nextInt();
                    Value value = new Value(dicc.get(index).getType(), a + "");
                    dicc.put(index, value);
                    break;
                case "double":
                    double b;
                    System.out.println("Ingrese el valor de la variable " + index + "(tipo " + dicc.get(index).getType() + "): ");
                    b = t.nextDouble();
                    Value valued = new Value(dicc.get(index).getType(), b + "");
                    dicc.put(index, valued);
                    break;
                case "String":
                    String c;
                    System.out.println("Ingrese el valor de la variable " + index + "(tipo " + dicc.get(index).getType() + "): ");
                    c = t.nextLine();
                    Value values = new Value(dicc.get(index).getType(), c);
                    dicc.put(index, values);
                    break;
                case "boolean":
                    boolean e;
                    System.out.println("Ingrese el valor de la variable " + index + "(tipo " + dicc.get(index).getType() + "): ");
                    e = t.nextBoolean();
                    Value valueb = new Value(dicc.get(index).getType(), e + "");
                    dicc.put(index, valueb);
                    break;
                case "char":
                    char f;
                    System.out.println("Ingrese el valor de la variable " + index + "(tipo " + dicc.get(index).getType() + "): ");
                    f = t.next().charAt(0);
                    Value valuec = new Value(dicc.get(index).getType(), f + "");
                    dicc.put(index, valuec);
                    break;
            }
        } else {
            System.err.println("La variable no ha sido creada");
            System.exit(1);
        }
    }
    
    private String evaluador(String cadena) {
        String eval[] = cadena.split(" ");
        double a, b, res;
        boolean c, d, resB;
        for (int i = 0; i < eval.length; i++) {
            //System.out.println(eval[i]);
            if (esNum(eval[i])) {
                aritmeticos.push(eval[i]);
            } else if(!esNum(eval[i]) && !esVariable(eval[i]) && !esVarBooleana(eval[i]) && !esOperador(eval[i])){
                System.err.print("Variable '" + eval[i] + "' no encontrada " );
                System.exit(0);
            }
            else if (esVariable(eval[i])) {
                    if (dicc.get(eval[i]).getType().equals("int") || dicc.get(eval[i]).getType().equals("double")) {
                        //System.out.println(eval[i].equals("b"));
                        aritmeticos.push(dicc.get(eval[i]).getValue());
                    } else if (dicc.get(eval[i]).getType().equals("boolean")) {
                        logicos.push(dicc.get(eval[0]).getValue());
                    }
            } else if (esVarBooleana(eval[i])) {
                logicos.push(eval[i]);
            } else if (esOperador(eval[i])) {
                switch (eval[i]) {
                    case "+":
                        a = Double.parseDouble(aritmeticos.pop());
                        b = Double.parseDouble(aritmeticos.pop());
                        res = a + b;
                        aritmeticos.push(res + "");
                        break;
                    case "-":
                        a = Double.parseDouble(aritmeticos.pop());
                        b = Double.parseDouble(aritmeticos.pop());
                        res = b - a;
                        aritmeticos.push(res + "");
                        break;
                    case "*":
                        a = Double.parseDouble(aritmeticos.pop());
                        b = Double.parseDouble(aritmeticos.pop());
                        res = a * b;
                        aritmeticos.push(res + "");
                        break;
                    case "/":
                        a = Double.parseDouble(aritmeticos.pop());
                        b = Double.parseDouble(aritmeticos.pop());
                        res = b / a;
                        aritmeticos.push(res + "");
                        break;
                    case "%":
                        a = Double.parseDouble(aritmeticos.pop());
                        b = Double.parseDouble(aritmeticos.pop());
                        res = b % a;
                        aritmeticos.push(res + "");
                        break;
                    case "<":
                        a = Double.parseDouble(aritmeticos.pop());
                        b = Double.parseDouble(aritmeticos.pop());
                        resB = b < a;
                        logicos.push(resB + "");
                        break;
                    case "<=":
                        a = Double.parseDouble(aritmeticos.pop());
                        b = Double.parseDouble(aritmeticos.pop());
                        resB = b <= a;
                        logicos.push(resB + "");
                        break;
                    case ">":
                        a = Double.parseDouble(aritmeticos.pop());
                        b = Double.parseDouble(aritmeticos.pop());
                        resB = b > a;
                        logicos.push(resB + "");
                        break;
                    case ">=":
                        a = Double.parseDouble(aritmeticos.pop());
                        b = Double.parseDouble(aritmeticos.pop());
                        resB = b >= a;
                        logicos.push(resB + "");
                        break;
                    case "==":
                        a = Double.parseDouble(aritmeticos.pop());
                        b = Double.parseDouble(aritmeticos.pop());
                        resB = b == a;
                        logicos.push(resB + "");
                        break;
                    case "!=":
                        a = Double.parseDouble(aritmeticos.pop());
                        b = Double.parseDouble(aritmeticos.pop());
                        resB = b != a;
                        logicos.push(resB + "");
                        break;
                    case "!":
                        c = Boolean.parseBoolean(logicos.pop());
                        resB = !c;
                        logicos.push(resB + "");
                        break;
                    case "||":
                        c = Boolean.parseBoolean(logicos.pop());
                        d = Boolean.parseBoolean(logicos.pop());
                        resB = d || c;
                        logicos.push(resB + "");
                        break;
                    case "&&":
                        c = Boolean.parseBoolean(logicos.pop());
                        d = Boolean.parseBoolean(logicos.pop());
                        resB = d && c;
                        logicos.push(resB + "");
                        break;
                }
            }
        }
        if (!aritmeticos.empty() && esOperacionA(cadena)) {
            return aritmeticos.pop();
        } else if (!logicos.empty() && esOperacioB(cadena)) {
            return logicos.pop();
        }
        return "";
    }
    
    private boolean esNum(String a) {
        return a.equals("0") || a.equals("1") || a.equals("2") || a.equals("3") || a.equals("4") || a.equals("5") || a.equals("6") || a.equals("7") || a.equals("8") || a.equals("9");
    }
    
    private boolean esOperador(String a) {
        return a.equals("+") || a.equals("-") || a.equals("*") || a.equals("/") || a.equals("%") || a.equals("<")
                || a.equals("<=") || a.equals(">") || a.equals(">=") || a.equals("==") || a.equals("!=") || a.equals("&&") || a.equals("||")
                || a.equals("!") || a.equals("=");
    }
    
    private boolean esVariable(String a) {
        return dicc.containsKey(a);
    }
    
    private boolean esVarBooleana(String a) {
        return a.equals("true") || a.equals("false");
    }
    
    private boolean esOperacionA(String a) {
        return a.contains("+") || a.contains("-") || a.contains("*") || a.contains("/") || a.contains("%");
    }
    
    private boolean esOperacioB(String a) {
        return a.contains("<") || a.contains("<=") || a.contains(">") || a.contains(">=") || a.contains("==") || a.contains("!=") || a.contains("!");
    }
    
    public String imprimirMatriz() {
        String salida = "";
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < 3; j++) {
                salida += "[" + arr[i][j] + "] ";
                if (j == arr[i].length - 1) {
                    salida += "\n";
                }
            }
        }
        return salida;
    }
}
