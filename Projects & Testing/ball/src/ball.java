

//Java Class Imports
import java.awt.*;
//import java.awt.Color;
import java.awt.event.*;
import java.applet.AudioClip;
import java.applet.*;
import javax.swing.*;
//End of Java Class imports

public class ball extends Applet {

      private int x= 30;
      private int y = 30;
      private int r = 4;
      private int Vx = 1;
      private int Vy = 1;

      public  boolean Sb = true;

      private int aWidth = 400;
      private int aHeight = 300;

      private int s =1;



      Image offScreen2;
      Graphics GB;

      public void init() {
              bufferingSetup2();
      }

      public void bufferingSetup2() {
             offScreen2 = createImage(400,300);
             GB = offScreen2.getGraphics();
             s = 2;
             System.out.println(s);
      }


      public void paint(Graphics g) {
             //Graphics2D g2 = (Graphics2D) g;

             //if (s==2) {
             GB.fillOval(x-r,y-r,r*2,r*2);
             g.drawImage(offScreen2,0,0,this);
             //}
      }

      private void moveBall() {
             x += Vx;
             y += Vy;
      }

      private void reflectY() {
             if ((y-r)<=0) { y = r+1; Vy = -Vy; }
             if ((y+r)>=aHeight) { y = aHeight-r-1; Vy = -Vy; }
      }

      private void reflectX() {
              if ((x-r)<=0) { x = r+1; Vx = -Vx;}
              if ((x+r)>=aWidth) { x = aWidth-r-1; Vx = -Vx;}
      }

      private void paddleReflect() {

      }

      public void runner() {
             bufferingSetup2();
             moveBall();
             reflectY();
             reflectX();
             paddleReflect();
      }



}