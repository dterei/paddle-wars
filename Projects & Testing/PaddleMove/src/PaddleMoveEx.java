/*
 * PaddleMove.java
 *
 * Created on July 1, 2003, 11:50 PM
 */

/**
 *
 * @author  David Terei
 */



import java.applet.*;
import java.awt.*;


public class PaddleMove extends Applet {
    
    /** Initializes the applet PaddleMove */
    
    public void init() {
        initComponents();
    }

    
    /** This method is called from within the init() method to
     * initialize the form.
     */


    private void initComponents() {
        panel1 = new java.awt.Panel();
        button1 = new java.awt.Button();

        setLayout(new java.awt.BorderLayout());

        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Pressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Released(evt);
            }
        });

        button1.setLabel("button1");
        panel1.add(button1);

        add(panel1, java.awt.BorderLayout.CENTER);

    }

    private void Released(java.awt.event.KeyEvent evt) {
        // Add your handling code here:
    }

    private void Pressed(java.awt.event.KeyEvent evt) {
        // Add your handling code here:
    }
    
    
    // Variables declaration

    private java.awt.Button button1;
    private java.awt.Panel panel1;
    
    // End of variables declaration
    
}
