/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamecontrollertest;

import controllerstate.ControllerState;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;

/**
 *
 * @author Deahgib
 */
public class ControllerManager {
    
    public static Controller controller;
    private static final float DEAD_ZONE = 0.2f;
    
    private static final String CONTROLLER_NAME = "Controller (XBOX 360 For Windows)";
    
    public static final int CON_A_BUTTON = 0;
    public static final int CON_B_BUTTON = 1;
    public static final int CON_X_BUTTON = 2;
    public static final int CON_Y_BUTTON = 3;
    public static final int CON_LB_BUTTON = 4;
    public static final int CON_RB_BUTTON = 5;
    public static final int CON_BACK_BUTTON = 6;
    public static final int CON_START_BUTTON = 7;
    public static final int CON_L3_BUTTON = 8;
    public static final int CON_R3_BUTTON = 9;
    
    public static final int CON_LEFT_Y_AXIS = 0;
    public static final int CON_LEFT_X_AXIS = 1;
    public static final int CON_RIGHT_Y_AXIS = 2;
    public static final int CON_RIGHT_X_AXIS = 3;
    public static final int CON_TRIGGERS = 4;
    
    
    private static ArrayList<Integer> lastButtons = new ArrayList<Integer>();
    
    public static void init(){
        try {
            Controllers.create();
        } catch (LWJGLException ex) {
            Logger.getLogger(ControllerManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(int i = 0; i < Controllers.getControllerCount(); i++){
            controller = Controllers.getController(i);
            System.out.println(controller.getName());
            if(controller.getName().equals(CONTROLLER_NAME)){
                break;
            }
        }
        // For Dpad use controller.getPovX() or .getPovY() which returns a float.
        System.out.println("Controller Axies:");
        for(int i = 0; i < controller.getAxisCount(); i++){
            System.out.println(i+": "+controller.getAxisName(i));
            controller.setDeadZone(i, DEAD_ZONE);
        }
        System.out.println("Controller Buttons:");
        for(int i = 0; i < controller.getButtonCount(); i++){
            System.out.println(i+": "+controller.getButtonName(i));
        }
    }
    
    public static void update() {
        Controllers.poll();
        
        // This just stores all the keys that are currently pressed in this frame.
        lastButtons.clear();
        for(int i = 0; i < controller.getButtonCount(); i++){
            if(isButtonPressed(i)){
                //System.out.println(controller.getButtonName(i));
                lastButtons.add(i);
            }
        }
    }
    
    public static boolean isButtonPressed(int buttonCode){
        return controller.isButtonPressed(buttonCode);
    }
    
    public static boolean isButtonDown(int buttonCode){
        return isButtonPressed(buttonCode) && !lastButtons.contains(buttonCode);
    }
    
    public static boolean isButtonUp(int buttonCode){
        return !isButtonPressed(buttonCode) && lastButtons.contains(buttonCode);
    }
    
    public static float getAxisValue(int axisCode){
        return controller.getAxisValue(axisCode);
    }
    
    public static float getAxisAbsValue(int axisCode){
        return Math.abs(controller.getAxisValue(axisCode));
    }
    
    public static ControllerState getNewControllerState(){
        float[] axies = new float[controller.getAxisCount()];
        for(int i = 0; i<axies.length; i++){
            axies[i] = controller.getAxisValue(i);
        }
        boolean[] buttons = new boolean[controller.getButtonCount()];
        for(int i = 0; i<buttons.length; i++){
            buttons[i] = controller.isButtonPressed(i);
        }
        return new ControllerState(axies, buttons);
    }
}
