/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.Interprete;

/**
 *
 * @author Usuario
 */
public class Controlador {
    Interprete interprete;
    
    //Constructor
    public Controlador() {
        interprete = new Interprete();
    }
    
    //Métodos Get y Set
    
    public Interprete getInterprete(){
        return interprete;
    }

    public String[][] leerTXT() {
        return interprete.leerTXT();
    }
    
    public void interpretar(){
        interprete.interpretar();
    }
}
