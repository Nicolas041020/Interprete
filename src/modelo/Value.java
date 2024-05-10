/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Usuario
 */
public class Value {

    //Atributos
    String type, value;

    //Constructor
    public Value(String type, String value) {
        this.type = type;
        this.value = value;
    }
    //Métodos Get y set
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Value{" + "type=" + type + ", value=" + value + '}';
    }
    

}
