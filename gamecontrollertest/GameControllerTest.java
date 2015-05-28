/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamecontrollertest;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glVertex2f;

/**
 *
 * @author Deahgib
 */
public class GameControllerTest {
    /**
     * @param args the command line arguments
     */
    private static Controller controller;
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            Display.create();
            Display.setDisplayMode(new DisplayMode(1200, 600)); 
        } catch (LWJGLException ex) {
            Logger.getLogger(GameControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        ControllerManager.init();

        float posX, posY, width, height;
        posX=150;
        posY=280;
        width=height=10;
        boolean readyToMove = false;
        
        float r, g, b;
        r = 1;
        g = 1;
        b = 1;
        Socket clientSocket = null;
        try {
            clientSocket = new Socket("192.168.0.9", 4959);
        } catch (IOException ex) {
            Logger.getLogger(GameControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        while(!Display.isCloseRequested()){
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            if(ControllerManager.isButtonUp(ControllerManager.CON_BACK_BUTTON)){
                break;
            }
            if(ControllerManager.isButtonPressed(ControllerManager.CON_START_BUTTON)){
                readyToMove = true;
            }
            if(ControllerManager.isButtonPressed(ControllerManager.CON_A_BUTTON)){
                r = 0;
                g = 1;
                b = 0;
            }
            if(ControllerManager.isButtonPressed(ControllerManager.CON_B_BUTTON)){
                r = 1;
                g = 0;
                b = 0;
            }
            if(ControllerManager.isButtonPressed(ControllerManager.CON_X_BUTTON)){
                r = 0;
                g = 0;
                b = 1;
            }
            if(ControllerManager.isButtonPressed(ControllerManager.CON_Y_BUTTON)){
                r = 1;
                g = 1;
                b = 0;
            }
            
            if(readyToMove){
                posX += ControllerManager.getAxisValue(ControllerManager.CON_LEFT_X_AXIS)/2;
                posY += ControllerManager.getAxisValue(ControllerManager.CON_LEFT_Y_AXIS)/2;
                
                ObjectOutputStream outToRobot;
                try {
                    outToRobot = new ObjectOutputStream(clientSocket.getOutputStream());
                    outToRobot.writeObject(ControllerManager.getNewControllerState());
                } catch (IOException ex) {
                    Logger.getLogger(GameControllerTest.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            
            glColor3f(r, g, b);
            glBegin(GL_QUADS);
                glVertex2f(posX, posY); // Upper Left;
                glVertex2f(posX + width, posY); // Upper Right;
                glVertex2f(posX + width, posY + height); // Bottom Right;
                glVertex2f(posX, posY + height); // Bottom Left;
            glEnd();
            glColor3f(0, 0, 0);

            // DO last
            ControllerManager.update();
            Display.sync(60);
            Display.update();
        }
        Display.destroy();
    }
}
