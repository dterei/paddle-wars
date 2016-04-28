/** Creator: David Terei
    Date Started:
    Last Modified: 29/7/03
    Version: Beta 2.4! (Build 12)
    History: --Build 12--
             * Applet Is Now a primitive "Engine."
             * Most Raw Values Removed, Applet Now Uses Varibles, Dependent on A
               Few Prime Varibles To Get Most Of Its Data.
             --Build 11--
             * Key Handling Improoved
             * Bonus Ball InterceptRecognition Added
             * Bonus Ball Added
             --Build 10--
             * Round Restart Improved. Paddle Movement During Paused Time Is
               Now Possible
             --Build 9--
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
    int Bx; //x - co-ord
    int By; //y - co-ord
    int BxM = 0; //variable for holding which move direction
    int ByM = 0; //variable for holding which move direction
    //Paddle
    int Py1; //paddle 1 (left) y co-ord
    int Py2; //paddle 2 (right) y co-ord
    int Py1M = 0;  //paddle 1 (left) y move
    int Py2M = 0;  //padle 2 (right) y move
    int P1SizeY = 60;
    int P1SizeX = 10;
    int P2SizeY = 60;
    int P2SizeX = 10;
    //Paddle-Round Intercept
    int BxC; //Ball X Co-ord Centre
    int ByC; //''   Y    ''   ''
    int P1xRC; //Paddle 1 X Co-ord of Round Centre
    int P1yRCT; //''   '' Y    ''     ''     ''  Top
    int P1yRCB; //''            ''       ''   Bottom
    int P2xRC;  //Same as above but for Paddle 2
    int P2yRCT; //              ''
    int P2yRCB; //              ''
    int XrIntercept; //The X Corodinate of where the ball intercepts the Rounded Paddle
    //Scores
    int P1Score, P2Score;
    String P1ScoreString;
    String P2ScoreString;
    //Sounds
    AudioClip ballHit;
    //Keys
    int []keyStatus = new int[256]; //Array for storing the status of the key
    p1D = 87;
    p1U = 88;
    p2D = 69;
    p2U = 70;
    //private int keyASSCI; //stores the ASSCI value of the key pressed
    //Mouse
    int mouseClicks;
    //Double Buffering
    Graphics graphicsBuffer;
    Image offScreen;
    //Thread
    int a = 2;
    Thread gameThread; //the main thread
    //Bonuses
    double BonusTimer;
    boolean BonusRunning = false;
    int BonX = -20;
    int BonY = -20;
    int BonXm = 0;
    int BonYm = 0;
    int BonusCount = 6;
    //Applet
    int aWidth;
    int aHeight;
    //End of Variable Declaration

    public void init(){ //This is a special method, unique to applets that is run when the applet is loaded.
        initComponents();
        setupVariables();
        loadSounds();
        bufferingSetup();
        BonusTimer = Math.round((Math.random()*20+20)*1);
    }

    public void start() {
        if (gameThread == null) {
           gameThread = new Thread(this, "Game"); //initializes the thread, gameThread.
           gameThread.start(); //starts the thread, gameThread.
        }
    }

    public void stop() {
           gameThread = null;
    }

    public void setupVariables() {
        aWidth = size().width;
        aHeight = size().height;
        Bx = aWidth/2;
        By = aHeight/2;
        Py1 = (aHeight-P1SizeY)/2;
        Py2 = (aHeight-P2SizeY)/2;
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
        offScreen = createImage(aWidth,aHeight);
        graphicsBuffer = offScreen.getGraphics();
    }

    public void ballMove(){  //moves the ball
        Bx += BxM;
        By += ByM;
    }

    public void restartRound(){
           if(BxM>0) {
                P1Score ++;
            } else {
                P2Score ++;
            }
            a++;
            Bx = aWidth/2;;
            By = aHeight/2;;
            BxM = 0;
            ByM = 0;
    }

    public void changeBallY(){
           if (By<=0) {By = 1; }
           if (By >=280) {By = 279; }
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
          BPRreflect();
        }
    }

    public void BPRreflect() {
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
                Dx = (double)Math.sqrt(25-Math.pow(XIntercept,2));
                Dy = -(-1/(-2*XIntercept /Dx));
            } else {
                whereIsBall = 2;
                XIntercept = (((BxC)+(2*P1xRC))/3)-35;
                Dx = (double)Math.sqrt(25-Math.pow(XIntercept,2));
                Dy = (-1/(-2*XIntercept /Dx));
            }
        } else {
            if(By<(Py2+30)) {
                whereIsBall = 3;
                XIntercept = (((BxC)+(2*P2xRC))/3)-365;
                Dx = (double)Math.sqrt(25-Math.pow(XIntercept,2));
                Dy = (-1/(-2*XIntercept /Dx));
            } else {
                whereIsBall = 4;
                XIntercept = (((BxC)+(2*P2xRC))/3)-365;
                Dx = (double)Math.sqrt(25-Math.pow(XIntercept,2));
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
        //Applet - Gets The Dimensions Of The Applet
        //aWidth = size().width;
        //aHeight = size().height;
        //System.out.println(aWidth);
        //System.out.println(aHeight);

        //PADDLES
        graphicsBuffer.setColor(Color.black);
        graphicsBuffer.fillRect(0,0,aWidth,aHeight);
        graphicsBuffer.setColor(Color.cyan);
        graphicsBuffer.fillRoundRect(30,Py1,P1SizeX,P1SizeY,10,10); //PADDLE 1, left side
        graphicsBuffer.fillRoundRect(360,Py2,P2SizeX,P2SizeY,10,10); //PADDLE 2, right side
        graphicsBuffer.setColor(Color.blue);

        //BALL
        ballMove();
        paddleMove();
        graphicsBuffer.fillOval(Bx,By,20,20);

        //Scores
        Font font = new Font("TimesRoman", 1, 20);
        Color color = new Color(250,240,19,100);
        graphicsBuffer.setFont(font);
        graphicsBuffer.setColor(color);
        P1ScoreString = Integer.toString(P1Score);
        P2ScoreString = String.valueOf(P2Score);
        graphicsBuffer.drawString(P1ScoreString,10,20);
        graphicsBuffer.drawString(P2ScoreString,370,20);

        //Bonus
        graphicsBuffer.setColor(Color.yellow);
        graphicsBuffer.fillOval(BonX,BonY,20,20);

        //detects if the ball has hit a wall and acts appropriatley
        if (Bx<1 || Bx>379) {restartRound(); }
        if (By<=0 || By>=280) {changeBallY(); }
        //stops the paddles going outside the bounds of the screen
        if (Py1<1) { Py1 = 0; }
        if (Py1>239){ Py1=239; }
        if (Py2<1) { Py2 = 0; }
        if (Py2>239){ Py2 =239; }
        g.drawImage(offScreen,0,0,this);
    }

    public void update(Graphics g) {
        paint(g);
    }

    private void KeyPressed(KeyEvent evt){ //This method is run when a key is pressed.
        int key = evt.getKeyCode();
        if (keyStatus[key] == 0) {

           keyStatus[key] = 1;

           switch(key) {
                default : break;
                case p1U /* x */ : Py1M += -3; break; // P1 down
                case p1D /* w */ : Py1M += 3; break;  // P1 up
                case p2U /* i */ : Py2M += 3; break;  // P2 up
                case p2D /* m */ : Py2M += -3; break;  // P2 down
            }
        }
    }

    private void KeyReleased(KeyEvent evt) { //This method is run when a key is released.
        int key = evt.getKeyCode();

        switch(key) {
            default : break;
            case 87 /* x */ : Py1M += 3; break; // P1 down
            case 88 /* w */ : Py1M += -3; break;  // P1 up
            case 77 /* i */ : Py2M += -3; break;  // P2 up
            case 73 /* m */ : Py2M += 3; break;  // P2 down
        }
        keyStatus[key] = 0;
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
    }

    public void BonusTimer() {
        if (BonusRunning == false) {
           if (BonusTimer > 0) {
              BonusTimer -= 1;
           } else {
              Bonus();
              BonusRunning = true;
              BonusCount = 6;
              BonusTimer = Math.round((Math.random()*20+20)*50);
              //System.out.println(BonusTimer);
           }
        }
    }

    public void Bonus() {
           //System.out.println("GIVE ME A Bonus");
           BonX = 200;
           BonY = 150;
           BonXm = -4;
           BonYm = 4;
    }

    public void BonusMove() {
           BonX += BonXm;
           BonY += BonYm;
           if (BonX <= 0) { BonX = 1; BonXm =4; BonusCount -= 1; }
           if (BonX >= 380) { BonX =379; BonXm = -4; BonusCount -= 1; }
           if (BonY <= 0) { BonY = 1; BonYm = 4; }
           if (BonY >= 280) { BonY = 279; BonYm = -4; }

           if (BonusCount == 0) {
              BonX = -20;
              BonY = -20;
              BonXm = 0;
              BonYm = 0;
              BonusRunning = false;
            }

            BonIntercept();

    }

    public void BonIntercept() {
        if (BonX>=10 && BonX<=40 && BonY>=(Py1-9) && BonY<=(Py1+49)) {
            ballHit.play();
            //System.out.println("BON HIT");
        }
        if (BonX>=340 && BonX<=370 && BonY>=(Py2-9) && BonY<=(Py2+49)) {
            ballHit.play();
            //System.out.println("BON HIT");
        }

        int BonXC = (BonX+10);
        int BonYC = (BonY+10);

        int BPD_1T = (int)Math.sqrt(Math.pow(BonXC-P1xRC,2) + Math.pow(BonYC-P1yRCT,2));
        int BPD_1B = (int)Math.sqrt(Math.pow(BonXC-P1xRC,2) + Math.pow(BonYC-P1yRCB,2));
        int BPD_2T = (int)Math.sqrt(Math.pow(BonXC-P2xRC,2) + Math.pow(BonYC-P2yRCT,2));
        int BPD_2B = (int)Math.sqrt(Math.pow(BonXC-P2xRC,2) + Math.pow(BonYC-P2yRCB,2));
        //System.out.println(BxC);
        //System.out.println(ByC);
        //System.out.println(BPD_2B);

        if (BPD_1T<=15 || BPD_1B<=15 || BPD_2T<=15 || BPD_2B<=15) {
          //System.out.println("BON HIT");
          GiveBonous();
        }
    }

    public void GiveBonous() {
        int Paddle;
        if (BonX < 200) {
           Paddle = 1;
        } else {
           Paddle = 2;
        }
        //System.out.println(Paddle);
    }

    public void run() { //the method that runs when the thread is started. The Thread method.
        //System.out.println(keyStatus[keyASSCI]); //prints the status of the key pressed.
        Thread myThread = Thread.currentThread();
        int b = 0;
        int c = 20;
        while (gameThread == myThread) {
            repaint();
            BPIntercept();
            BPRIntercept();
            BonusTimer();
            if (BonusRunning == true) { BonusMove(); }
            if ((a % 2) == 0){
               //System.out.println("a == 1 ran");
               b++;
            }
            if (b == 50) {
               //System.out.println("b == 2 ran");
               BxM = -4;
               ByM = 0;
               b = 0;
               a = 1;
            }
            try {
                Thread.sleep(c);
            } catch (InterruptedException e){
            }
            //System.out.println(c);
            //System.out.println("RUN");
        }
    }
}