//Java Class Imports
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
//End of Java Class imports

public class PaddleMove extends Applet /*sets up this class to be a applet*/ implements Runnable /*this sets up the applet to use threads*/ {

    //Variable Decleration
    //Ball
    private int x = 0; //x - co-ord
    private int y = 0; //y - co-ord
    private int xM = 1; //variable for holding which move direction
    private int yM = 1; //variable for holding which move direction
    //Paddle
    private int y1 = 130; //paddle 1 (left) y co-ord
    private int y2 = 130; //paddle 2 (right) y co-ord
    private int y1M = 0;  //paddle 1 (left) y move
    private int y2M = 0;  //padle 2 (right) y move
    private int []keyStatus = new int[256]; //Array for storing the status of the key
    private int keyASSCI; //stores the ASSCI value of the key pressed
    private int globalKeyStatus = 0; //stores wether a key is currently being pressed
    //Thread
    private int mouseClicks = 0;
    private Thread gameThread; //the main thread
    //End of Variable Declaration

    public void init(){ //This is a special method, unique to applets that is run when the applet is loaded.
        makeArray0();
        initComponents();
    }

    public void start() {
        if (gameThread == null) {
           gameThread = new Thread(this, "Game"); //initializes the thread, gameThread.
           gameThread.start(); //starts the thread, gameThread.
        }
    }

    public void makeArray0() {
    for (int z = 0; z<255; z++) {
             keyStatus[z] = 0;
        }
    }

    public void initComponents() {
        addKeyListener(new KeyAdapter() {  //this sets up an key action listener with a scope of the whole applet
            public void keyPressed(KeyEvent evt) { //what to do when a key pressed
                if (globalKeyStatus == 0) {
                   KeyPressed(evt);
                   globalKeyStatus = 1;
                   }
            }
            public void keyReleased(KeyEvent evt) { //what to do when a key released
                KeyReleased(evt);
                globalKeyStatus = 0;
            }
        });

        addMouseListener(new MouseAdapter() { //this sets up a mouse action listener with a scope of the whole applet
            public void mouseClicked(MouseEvent evt) { //what to do when a mouse is clicked
                MouseClicked(evt);
            }
        });
    }

    public void ballMove(){  //moves the ball
        x += xM;
        y += yM;
    }

    public void changeBallX(){  //changes the x direction of the ball
           xM = (xM == 1)? -1 : 1; //this is a shortcut if else statement that decides which way the ball is going and acts appropriatley.
    }

    public void changeBallY(){
           yM = (yM == 1) ? -1 : 1;
    }

    public void paddleMove() {
        y1 += y1M;
        y2 += y2M;
    }

    public void paint(Graphics g){ //paint method. all painting done in this method.
        super.paint(g); //makes this paint method a subclass of the java.awt.Graphics class, allowing it to be repainted.
        //if (mouseClicks >= 2) {
           g.setColor(Color.cyan);
           g.fillRect(20,y1,20,60); //paddle 1, left side
           g.fillRect(360,y2,20,60); //paddle 2, right side
           g.setColor(Color.blue);
           ballMove();
           paddleMove();
           g.fillOval(x,y,20,20);
           //detects if the ball has hit a wall and acts appropriatley
           if (x<1 || x>379) {changeBallX(); }
           if (y<1 || y>279) {changeBallY(); }
           //stops the paddles going outside the bounds of the screen
           if (y1<1) { y1 = 0; }
           if (y1>239){ y1=239; }
           if (y2<1) { y2 = 0; }
           if (y2>239){ y2 =239; }
        //}
        //repaint(); //causes the whole paint method to loop and update itself
    }

    private void KeyPressed(KeyEvent evt){ //This method is run when a key is pressed.

        keyASSCI = evt.getKeyCode();  //get the ASSCI code for the key pressed.
        if (keyStatus[keyASSCI] == 0) { //test to see if the key is already down, if so, ignores the pressed key util released.
           System.out.println("key down");
           switch(evt.getKeyCode()) { //a multi-case selection used to determine what action to take depending on which key.
                default : break; //The default opeeration performed when the key pressed isnt any of the ones below.
                case 87 /* x */ : y1M = -1; break; // P1 down
                case 88 /* w */: y1M = 1; break;  // P1 up
                case 77 /* i */: y2M = 1; break;  // P2 up
                case 73 /* m */: y2M = -1; break;  // P2 down
            }
            keyStatus[keyASSCI] = 1; //sets the keys status to down.
        }
        //System.out.print("Key Pressed :"); //debuging info.
        //gameThread = new Thread(this); //initializes the thrread, gameThread.
        //gameThread.start(); //starts the thread, gameThread.
    }

    private void KeyReleased(KeyEvent evt) { //This method is run when a key is released.
        if (evt.getKeyCode() == keyASSCI) {
        System.out.println("Key Released");
        y1M = 0;
        y2M = 0;
        keyASSCI = evt.getKeyCode();
        keyStatus[keyASSCI] = 0; //sets the keys status to up
        }
    }

    private void MouseClicked(MouseEvent evt) { //This method runs when ever a mouse is clicked
        mouseClicks += 1;
        System.out.print("Mouse Clicked :");
        System.out.println(mouseClicks);
    }

    public void run() { //the method that runs when the thread is started. The Thread method.
        //System.out.println(keyStatus[keyASSCI]); //prints the status of the key pressed.
        Thread myThread = Thread.currentThread();
        while (gameThread == myThread) {
            repaint();
            try {
                Thread.sleep(5);
            } catch (InterruptedException e){
            }
            System.out.println("RUN");
        }
    }

}