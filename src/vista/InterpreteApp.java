/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package vista;

import controlador.Controlador;

/**
 *
 * @author Usuario
 */
public class InterpreteApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Controlador ctrl = new Controlador();
        ctrl.leerTXT();
        ctrl.interpretar();
    }    
}
