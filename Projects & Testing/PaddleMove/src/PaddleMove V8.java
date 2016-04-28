/** Creator: David Terei
    Date Started:
    Last Modified: 29/7/03
    Version: Beta 1.9 (Build 9)
    History: --Build 9--
             * Rounded Paddle End Reflection Improved
             * Associated Bugs With Rounded Reflection Fixed
             --Build 8--
             * Rounded Padle End Reflection Added
             * Rounded Paddles Added
             --Build 7--
             * Double Buffering Added
             * Round Restart Fixed
             --Build 6--
             * Round Restart Added
             * Score Added
             * Background Added
             --Build 5--
             * Removed Faulty Event Handling Performance Improvement Code
             * Added Pause Feature
             --Build 4--
             * Thread Added to be able control the run spped of the applet
             --Build 3--
             * Code Added to Improve Event Handling Performance
             * Code Removed to Attempt to Slow Applet
             --Build 2--
             * Code Added to Attempt to Slow Applet
             --Build 1--
             * Applet Created
*/

//Java Class Imports
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.lang.*;
//End of Java Class imports

public class PaddleMove extends Applet /*sets up this class to be a applet*/ implements Runnable /*this sets up the applet to use threads*/ {

    //Variable Decleration
    //Ball
    private int Bx = 100; //x - co-ord
    private int By = 100; //y - co-ord
    private int BxM = -4; //variable for holding which move direction
    private int ByM = 0; //variable for holding which move direction
    //Paddle
    private int Py1 = 130; //paddle 1 (left) y co-ord
    private int Py2 = 130; //paddle 2 (right) y co-ord
    private int Py1M = 0;  //paddle 1 (left) y move
    private int Py2M = 0;  //padle 2 (right) y move
    //Paddle-Round Intercept
    private int BxC; //Ball X Co-ord Centre
    private int ByC; //''   Y    ''   ''
    private int P1xRC; //Paddle 1 X Co-ord of Round Centre
    private int P1yRCT; //''   '' Y    ''     ''     ''  Top
    private int P1yRCB; //''            ''       ''   Bottom
    private int P2xRC;  //Same as above but for Paddle 2
    private int P2yRCT; //              ''
    private int P2yRCB; //              ''
    private int XrIntercept; //The X Corodinate of where the ball intercepts the Rounded Paddle
    //private int YrIntercept;
    //Scores
    private int P1Score, P2Score;
    private String P1ScoreString;
    private String P2ScoreString;
    //Sounds
    private AudioClip ballHit;
    //Keys
    private int []keyStatus = new int[256]; //Array for storing the status of the key
    private int keyASSCI; //stores the ASSCI value of the key pressed
    //Mouse
    private int mouseClicks;
    //Double Buffering
    private Graphics graphicsBuffer;
    private Image offScreen;
    //Thread
    private int a = 1;
    private Thread gameThread; //the main thread
    //End of Variable Declaration

    public void init(){ //This is a special method, unique to applets that is run when the applet is loaded.
        //makeArray0();
        initComponents();
        loadSounds();
        bufferingSetup();
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
                KeyPressed(evt);
            }
            public void keyReleased(KeyEvent evt) { //what to do when a key released
                KeyReleased(evt);
            }
        });

        addMouseListener(new MouseAdapter() { //this sets up a mouse action listener with a scope of the whole applet
            public void mouseClicked(MouseEvent evt) { //what to do when a mouse is clicked
                MouseClicked(evt);
            }
        });
    }

    public void loadSounds(){
    try {
        ballHit = getAudioClip(getCodeBase(), "audio/ding.au");
    } catch (Exception e) {}
    }

    public void bufferingSetup() {
        offScreen = createImage(400,300);
        graphicsBuffer = offScreen.getGraphics();
    }

    public void ballMove(){  //moves the ball
        Bx += BxM;
        By += ByM;
    }

    public void restartRound(){  //changes the x direction of the ball
           if(BxM>0) {
                P1Score ++;
            } else {
                P2Score ++;
            }
            a++;
            Bx = 100;
            By = 200;
            BxM = 0;
            ByM = 0;
    }

    public void changeBallY(){
           ByM = -ByM;
           ballHit.play();
    }

    public void BPIntercept() {
        if (Bx>=10 && Bx<=40 && By>=(Py1-9) && By<=(Py1+49)) {
            BxM = 4;
            //System.out.println("HIT 1");
            ballHit.play();
        }
        if (Bx>=340 && Bx<=370 && By>=(Py2-9) && By<=(Py2+49)) {
            BxM = -4;
            //System.out.println("HIT 2");
            ballHit.play();
        }
    }

    public void BPRIntercept() {
        BxC = (Bx+10);
        ByC = (By+10);
        P1xRC = (30+5);
        P2xRC = (360+5);
        P1yRCT = (Py1+5);
        P1yRCB = (Py1+55);
        P2yRCT = (Py2+5);
        P2yRCB = (Py2+55);
        int BPD_1T = (int)Math.sqrt(Math.pow(BxC-P1xRC,2) + Math.pow(ByC-P1yRCT,2));
        int BPD_1B = (int)Math.sqrt(Math.pow(BxC-P1xRC,2) + Math.pow(ByC-P1yRCB,2));
        int BPD_2T = (int)Math.sqrt(Math.pow(BxC-P2xRC,2) + Math.pow(ByC-P2yRCT,2));
        int BPD_2B = (int)Math.sqrt(Math.pow(BxC-P2xRC,2) + Math.pow(ByC-P2yRCB,2));
        //System.out.println(BxC);
        //System.out.println(ByC);
        //System.out.println(BPD_2B);

        if (BPD_1T<=15 || BPD_1B<=15 || BPD_2T<=15 || BPD_2B<=15) {
          BPRgetIntercepts();
        }
    }

    public void BPRgetIntercepts() {
        int whereIsBall = 0;
        int XIntercept = 0;
        double Dx = 0;
        double Dy = 0;

        if (Bx<200) {
            whereIsBall = 0;
            BxM = 4;
        } else {
            whereIsBall = 2;
            BxM = -4;
        }
        if (whereIsBall == 0) {
            if(By<(Py1+30)) {
                whereIsBall = 1;
                XIntercept = (((BxC)+(2*P1xRC))/3)-35;
                Dx = (double)Math.sqrt(25-(XIntercept*XIntercept));
                Dy = -(-1/(-2*XIntercept /Dx));
            } else {
                whereIsBall = 2;
                XIntercept = (((BxC)+(2*P1xRC))/3)-35;
                Dx = (double)Math.sqrt(25-(XIntercept*XIntercept));
                Dy = (-1/(-2*XIntercept /Dx));
            }
        } else {
            if(By<(Py2+30)) {
                whereIsBall = 3;
                XIntercept = (((BxC)+(2*P2xRC))/3)-365;
                Dx = (double)Math.sqrt(25-(XIntercept*XIntercept));
                Dy = (-1/(-2*XIntercept /Dx));
                System.out.println(Dy);
            } else {
                whereIsBall = 4;
                XIntercept = (((BxC)+(2*P2xRC))/3)-365;
                Dx = (double)Math.sqrt(25-(XIntercept*XIntercept));
                Dy = -(-1/(-2*XIntercept /Dx));
            }
        }
        ByM = (int)Math.round(Dy*4);
        //int XIntercept = (((BxC)+(2*P2xRC))/3)-35;
        //int YIntercept = ((ByC)+(2*P1yRC))/3;
        //double Dx = (double)Math.sqrt(25 - (XIntercept*XIntercept));
        //double Dy = (-1/(-2*XIntercept /Dx));
        //Dy = (By<(Py1+30)) ? -1*Dy : Dy;

        //ByM = (int)Math.round(Dy*4);
        //BxM = 4;
    }

    public void paddleMove() {
        Py1 += Py1M;
        Py2 += Py2M;
    }

    public void paint(Graphics g){ //paint method. all painting done in this method.
        //if (mouseClicks >= 2) {
           graphicsBuffer.setColor(Color.black);
           graphicsBuffer.fillRect(0,0,400,300);
           graphicsBuffer.setColor(Color.cyan);
           graphicsBuffer.fillRoundRect(30,Py1,10,60,10,10); //PADDLE 1, left side
           graphicsBuffer.fillRoundRect(360,Py2,10,60,10,10); //PADDLE 2, right side
           graphicsBuffer.setColor(Color.blue);
           ballMove();
           paddleMove();
           graphicsBuffer.fillOval(Bx,By,20,20); //BALL
           Font font = new Font("TimesRoman", 0, 20);
           Color color = new Color(255,0,0,100);
           graphicsBuffer.setFont(font);
           graphicsBuffer.setColor(color);
           P1ScoreString = Integer.toString(P1Score);
           P2ScoreString = String.valueOf(P2Score);
           graphicsBuffer.drawString(P1ScoreString,10,20);
           graphicsBuffer.drawString(P2ScoreString,370,20);

           //detects if the ball has hit a wall and acts appropriatley
           if (Bx<1 || Bx>379) {restartRound(); }
           if (By<=0 || By>=280) {changeBallY(); }
           //stops the paddles going outside the bounds of the screen
           if (Py1<1) { Py1 = 0; }
           if (Py1>239){ Py1=239; }
           if (Py2<1) { Py2 = 0; }
           if (Py2>239){ Py2 =239; }
           g.drawImage(offScreen,0,0,this);
        //}
        //repaint(); //causes the whole paint method to loop and update itself
    }

    public void update(Graphics g) {
        paint(g);
    }

    private void KeyPressed(KeyEvent evt){ //This method is run when a key is pressed.

        //keyASSCI = evt.getKeyCode();  //get the ASSCI code for the key pressed.
        //if (keyStatus[keyASSCI] == 0) { //test to see if the key is already down, if so, ignores the pressed key util released.
           //System.out.println("key down");
           switch(evt.getKeyCode()) { //a multi-case selection used to determine what action to take depending on which key.
                default : break; //The default opeeration performed when the key pressed isnt any of the ones below.
                case 87 /* x */ : Py1M = -1; break; // P1 down
                case 88 /* w */ : Py1M = 1; break;  // P1 up
                case 77 /* i */ : Py2M = 1; break;  // P2 up
                case 73 /* m */ : Py2M = -1; break;  // P2 down
            //}
            //keyStatus[keyASSCI] = 1; //sets the keys status to down.
        }
        //System.out.print("Key Pressed :"); //debuging info.
        //gameThread = new Thread(this); //initializes the thrread, gameThread.
        //gameThread.start(); //starts the thread, gameThread.
    }

    private void KeyReleased(KeyEvent evt) { //This method is run when a key is released.
        //System.out.println("Key Released");
        Py1M = 0;
        Py2M = 0;
        //keyASSCI = evt.getKeyCode();
        //keyStatus[keyASSCI] = 0; //sets the keys status to up
    }

    private void MouseClicked(MouseEvent evt) { //This method runs when ever a mouse is clicked
        //System.out.println("moue clicked");
        mouseClicks ++;
        if ((mouseClicks % 2) != 0) {
            gameThread = null;
        } else {
            gameThread = new Thread(this, "Game");
            gameThread.start();
        }
        //if (gameThread != null) {
          // System.out.println("Thread Paused");
           //try {
             //  gameThread.suspend();
            //} catch (InterruptedException e) {
            //}
        //} else {
          //  System.out.println("Thread Resumed");
            //try {
             //   gameThread.resume();
            //} catch (InterruptedException e) {
            //}
        //}
    }

    public void run() { //the method that runs when the thread is started. The Thread method.
        //System.out.println(keyStatus[keyASSCI]); //prints the status of the key pressed.
        Thread myThread = Thread.currentThread();
        int b = 0;
        int c = 5;
        while (gameThread == myThread) {
            repaint();
            BPIntercept();
            BPRIntercept();
            if ((a % 2) == 0){
               //System.out.println("a == 1 ran");
               b++;
            }
            if (b == 2) {
               //System.out.println("b == 2 ran");
               c = 1000;
               BxM = -4;
               ByM = 0;
               b = 0;
               a = 1;
            }
            try {
                Thread.sleep(c);
            } catch (InterruptedException e){
            }
            c = 20;
            //System.out.println(c);
            //System.out.println("RUN");
        }
    }

}
