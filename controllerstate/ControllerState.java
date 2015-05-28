/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllerstate;
import java.io.Serializable;

/**
 *
 * @author Deahgib
 */
public class ControllerState implements Serializable {
    private float[] axis;
    private boolean[] buttons;
    
    public ControllerState(){}
    public ControllerState(float[] a, boolean[] b){
        axis = a;
        buttons = b;
    }
    
    public void setController(float[] a, boolean[] b){
        axis = a;
        buttons = b;
    }
    
    public float getAxisValue(int index){
        return axis[index];
    }
    
    public float[] getAxis(){
        return axis;
    }
    
    public boolean getButtonState(int index){
        return buttons[index];
    }
    
    public boolean[] getButtons(){
        return buttons;
    }
}
