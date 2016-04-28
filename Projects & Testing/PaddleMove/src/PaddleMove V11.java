/** Creator: David Terei
    Date Started:
    Last Modified: 29/7/03
    Version: Beta 2.4! (Build 12)
    History: --Build 12--
             * Applet Is Now a primitive "Engine."
             * Most Raw Values Removed, Applet Now Uses Varibles, Dependent on A
               Few Prime Varibles To Get Most Of Its Data.
             --Build 11--
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
    int Bd = 20;
    //Paddle
    int Py1; //paddle 1 (left) y co-ord
    int Py2; //paddle 2 (right) y co-ord
    int Py1M = 0;  //paddle 1 (left) y move
    int Py2M = 0;  //padle 2 (right) y move
    int Py1Mv = 3;
    int Py2Mv =3;
    int P1SizeY = 60;
    int P1SizeX = 10;
    int P2SizeY = 60;
    int P2SizeX = 10;
    int p1xPosistion;
    int p2xPosistion;
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
    int p1U = 87;
    int p1D = 88;
    int p2U = 73;
    int p2D = 77;
    //private int keyASSCI; //stores the ASSCI value of the key pressed
    //Mouse
    int mouseClicks;
    //Double Buffering
    Graphics graphicsBuffer;
    Image offScreen;
    //Thread
    int DelayCycle = 2;
    int Gspeed = 20;
    Thread gameThread; //the main thread
    //Bonuses
    int MinTime = 20;
    int TimeRange = 20;
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
        BonusTimer = Math.round((Math.random()*TimeRange+MinTime)*1/*(1000/Gspeed)*/);
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
        p1xPosistion = (int)Math.round(aWidth*0.1);
        p2xPosistion = (aWidth-(p1xPosistion+P2SizeX));
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
            DelayCycle++;
            Bx = aWidth/2;;
            By = aHeight/2;;
            BxM = 0;
            ByM = 0;
    }

    public void changeBallY(){
           if (By<=0) {By = 1; }
           if (By >=(aHeight-20)) {By = (aHeight-21); }
           ByM = -ByM;
           ballHit.play();
    }

    public void BPIntercept() {

        if (Bx>= p1xPosistion && Bx<=(p1xPosistion+P1SizeX) && By>=(Py1-(Bd/2)) && By<=(Py1+(P1SizeY-(Bd/2)))) {
            BxM = 4;
            //System.out.println("HIT 1");
            ballHit.play();
        }
        if (Bx>=(p2xPosistion-Bd) && Bx<=(p2xPosistion+P2SizeX) && By>=(Py2-(Bd/2)) && By<=(Py2+(P2SizeY-(Bd/2)))) {
            BxM = -4;
            //System.out.println("HIT 2");
            ballHit.play();
        }
    }

    public void BPRIntercept() {
        BxC = (Bx+(Bd/2));
        ByC = (By+(Bd/2));
        P1xRC = (p1xPosistion+(P1SizeX/2));
        P2xRC = (p2xPosistion+(P2SizeX/2));
        P1yRCT = (Py1+(P1SizeX/2));
        P1yRCB = (Py1+(P1SizeY-(P1SizeX/2)));
        P2yRCT = (Py2+(P1SizeX/2));
        P2yRCB = (Py2+(P2SizeY-(P2SizeX/2)));
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
            if(By<(Py1+(P1SizeY/2))) {
                XIntercept = (((BxC)+(2*P1xRC))/3)-(p1xPosistion+(P1SizeX/2));
                Dx = (double)Math.sqrt(Math.pow((P1SizeX/2),2)-Math.pow(XIntercept,2));
                Dy = -(-1/(-2*XIntercept /Dx));
            } else {
                XIntercept = ((((BxC)+(2*P1xRC))/3)-(p1xPosistion+(P1SizeX/2)));
                Dx = (double)Math.sqrt(Math.pow((P1SizeX/2),2)-Math.pow(XIntercept,2));
                Dy = (-1/(-2*XIntercept /Dx));
            }
        } else {
            if(By<(Py2+30)) {
                XIntercept = (((BxC)+(2*P2xRC))/3)-(p2xPosistion+(P2SizeX/2));
                Dx = (double)Math.sqrt(Math.pow((P2SizeX/2),2)-Math.pow(XIntercept,2));
                Dy = (-1/(-2*XIntercept /Dx));
            } else {
                XIntercept = (((BxC)+(2*P2xRC))/3)-(p2xPosistion+(P2SizeX/2));
                Dx = (double)Math.sqrt(Math.pow((P2SizeX/2),2)-Math.pow(XIntercept,2));
                Dy = -(-1/(-2*XIntercept /Dx));
            }
        }
        ByM = (int)Math.round(Dy*4);
        ballHit.play();
        System.out.println(ByM);
    }

    public void paddleMove() {
        Py1 += Py1M;
        Py2 += Py2M;
    }

    public void paint(Graphics g){ //paint method. all painting done in this method.

        //PADDLES
        graphicsBuffer.setColor(Color.black);
        graphicsBuffer.fillRect(0,0,aWidth,aHeight);
        graphicsBuffer.setColor(Color.cyan);
        graphicsBuffer.fillRoundRect(p1xPosistion,Py1,P1SizeX,P1SizeY,P1SizeX,P1SizeX); //PADDLE 1, left side
        graphicsBuffer.fillRoundRect(p2xPosistion,Py2,P2SizeX,P2SizeY,P2SizeX,P2SizeX); //PADDLE 2, right side
        graphicsBuffer.setColor(Color.blue);

        //BALL
        ballMove();
        paddleMove();
        graphicsBuffer.fillOval(Bx,By,Bd,Bd);

        //Scores
        Font font = new Font("TimesRoman", 1, 20);
        Color color = new Color(250,240,19,100);
        graphicsBuffer.setFont(font);
        graphicsBuffer.setColor(color);
        P1ScoreString = Integer.toString(P1Score);
        P2ScoreString = String.valueOf(P2Score);
        graphicsBuffer.drawString(P1ScoreString,10,20);
        graphicsBuffer.drawString(P2ScoreString,(aWidth-20),20);

        //Bonus
        graphicsBuffer.setColor(Color.yellow);
        graphicsBuffer.fillOval(BonX,BonY,20,20);

        //detects if the ball has hit a wall and acts appropriatley
        if (Bx<-20 || Bx>aWidth) {restartRound(); }
        if (By<=0 || By>=(aHeight-Bd)) {changeBallY(); }
        //stops the paddles going outside the bounds of the screen
        if (Py1<1) { Py1 = 0; }
        if (Py1>(aHeight-P1SizeY)){ Py1=aHeight-P1SizeY; }
        if (Py2<1) { Py2 = 0; }
        if (Py2>(aHeight-P2SizeY)) { Py2=aHeight-P2SizeY; }
        g.drawImage(offScreen,0,0,this);
    }

    public void update(Graphics g) {
        paint(g);
    }

    private void KeyPressed(KeyEvent evt){ //This method is run when a key is pressed.
        int key = evt.getKeyCode();
        if (keyStatus[key] == 0) {

           keyStatus[key] = 1;

           if (key == p1U) { Py1M += -Py1Mv; }
           if (key == p1D) { Py1M += Py1Mv; }
           if (key == p2U) { Py2M += -Py2Mv; }
           if (key == p2D) { Py2M += Py2Mv; }

           //switch(key) {
             //   default : break;
               // case 87 /* x */ : Py1M += -3; break; // P1 down
                //case 88 /* w */ : Py1M += 3; break;  // P1 up
                //case 77 /* i */ : Py2M += 3; break;  // P2 up
                //case 73 /* m */ : Py2M += -3; break;  // P2 down
           // }
        }
    }

    private void KeyReleased(KeyEvent evt) { //This method is run when a key is released.
        int key = evt.getKeyCode();

        if (key == p1U) { Py1M += Py1Mv; }
        if (key == p1D) { Py1M += -Py1Mv; }
        if (key == p2U) { Py2M += Py2Mv; }
        if (key == p2D) { Py2M += -Py2Mv; }

        //switch(key) {
          //  default : break;
           // case 87 /* x */ : Py1M += 3; break; // P1 down
           // case 88 /* w */ : Py1M += -3; break;  // P1 up
           // case 77 /* i */ : Py2M += -3; break;  // P2 up
           // case 73 /* m */ : Py2M += 3; break;  // P2 down
        //}
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
              BonusTimer = Math.round((Math.random()*TimeRange+MinTime)*(1000/Gspeed));
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
           if (BonX <= 0) { BonX = 1; BonXm = -BonXm; BonusCount -= 1; }
           if (BonX >= (aWidth-Bd)) { BonX =(aWidth-20)-1; BonXm = -BonXm; BonusCount -= 1; }
           if (BonY <= 0) { BonY = 1; BonYm = -BonYm; }
           if (BonY >= (aHeight-Bd)) { BonY = (aHeight-Bd)-1; BonYm = -BonYm; }

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
        int Delay = 0;
        while (gameThread == myThread) {
            repaint();
            BPIntercept();
            BPRIntercept();
            BonusTimer();
            if (BonusRunning == true) { BonusMove(); }
            if ((DelayCycle % 2) == 0){
               //System.out.println("a == 1 ran");
               Delay++;
            }
            if (Delay == 50) {
               //System.out.println("b == 2 ran");
               BxM = -4;
               ByM = 0;
               Delay = 0;
               DelayCycle = 1;
            }
            try {
                Thread.sleep(Gspeed);
            } catch (InterruptedException e){
            }
            //System.out.println(c);
            //System.out.println("RUN");
        }
    }
}