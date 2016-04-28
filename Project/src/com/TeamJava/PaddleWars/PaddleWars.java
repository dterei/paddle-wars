package com.TeamJava.PaddleWars;

import java.awt.*;
import java.awt.event.*;
import java.applet.AudioClip;
import javax.swing.*;
import java.awt.geom.*;



/**
 * This is the main clas for the paddle Wars game. 
 * It creates and controls all the other classes, paddle, ball, bonusManager. 
 * It also controls event handling and AI.
 *
 * @see Paddle
 * @see Ball
 * @see BonusManager
 */
public class PaddleWars extends JApplet implements Runnable {

    /**
     * An Array of ball Objects, which is responisble for creating all instances of the ball class., for Game Ball, and Bonus Ball use. An Array must be used so that the number of instances of the ball class can be determined by the code.
     *
     * @see Ball
     */
    public static Ball[] Ball = new Ball[2];
    
    /**
     * n Array of ball Objects, which is responisble for creating all instances of the ball class.
     * An Array must be used so that the number of instances of the ball class can be determined by the code.
     */
    public static Ball[][] Missiles = new Ball[2][3];
    
    /**
     * An Array of paddle Objects, which is responisble for creating all instances of the paddle class.
     * An Array must be used so that the number of instances of the paddle class can be determined by the code.
     *
     * @see Paddle
     */
    public static Paddle[] Paddle = new Paddle[2];

    /**
     * An Object which holds an instance of the bonusManager Class. Responsible for the classes operation.
     *
     * @see BonusManager
     */
    public static BonusManager bonManager;

    /**
     * An array of AI's which are used to control the paddle's if the game mode is single player or Computer blitz.
     */
    private static AI[] Computer;

    /**
     * An array which stores the time until a computer should predict the ball location.
     */
    private int[] predictTime;

    /**
     * This stores an MouseHandler object, which handles all the mouse events, has to be done this way so that it can be removed latter.
     */
    private MouseHandler MouseyHandler = new MouseHandler();

    //Class Wide
    /**
     * Stores the width of the area in which the game should operate.
     */
    private int aWidth;

    /**
     * Stores the height of the area in which the game should operate.
     */
    private int aHeight;

    /**
     * Stores the current game mode.
     *
     * @see Interface
     */
    private int gameMode;

    //Paddles
    /**
     * Stores the x posistion of the paddles.
     */
    private int[] Px = new int[2];

    /**
     * Stores the y size of the paddles.
     */
    private int[] PSizeY = { 30, 30 };

    /**
     * Stores the x size of each paddle.
     */

    private int[] PSizeX = { 5, 5 };

    /**
     * Stores which colour paddle each paddle is.
     */
    private int[] PaddleColour = { 1, 2 };

    /**
     * Stores the movement velocity of each paddle.
     */
    private int[] PyM = { 0, 0 };

    /**
     * Stores the normal movement value of the keys.
     */
    private int[] PyMv = { 1, 1 };

    /**
     * Stores the hyper movement value of the keys.
     */
    private int[] pMvB = { 3, 3 };

    //Scores
    /**
     * Stores the score of each player.
     */
    int[] Score = { 5, 5 };

    //Keys
    /**
     * Stores the key status (up or down) of every key in the assci value range.
     */
    private int[] keyStatus = new int[256]; //Array for storing the status of the key

    /**
     * Stores the ASSCI value for the up key for each player.
     */
    private int[] pU = { 87, 73 };

    /**
     * Stores the ASSCI value for the down key for each player.
     */
    private int[] pD = { 88, 77 };

    /**
     * Stores the ASSCI value for the hyper key for each player.
     */
    private int[] pB = { 83, 75 };

    /**
     * Stores the ASSCI value for the missile key for each player.
     */
    private int[] pM = { 68, 74 };

    /**
     * Stores if a player has a missile paddle or not.
     */
    private int[] GotM = { 0, 0 };

    /**
     * Stores if a player has the hyper key or not.
     */
    private int[] gotHyperKey = { 0, 0 };

    //Mouse
    /**
     * Stores the number of mouse clicks.
     */
    private int mouseClicks;

    //Sounds
    /**
     * Stores the audio file to play when a ball hits the wall.
     */
    private AudioClip[] WallHit = new AudioClip[2];

    /**
     * Stores the audio clips to play when a ball hits a paddle.
     */
    private AudioClip[][] PaddleSounds = new AudioClip[2][3];

    //Thread
    /**
     * Stores the delay placed on the main thread, gameThread, or as i like to call it, the game speed.
     */
    private int Gspeed = 7;

    /**
     * The main thread.
     */
    private Thread gameThread;

    /**
     * Stores if the Thread should continue to runor not.
     */
    private int runFlag = 1;

    /**
     * Stores if the game is paused or not.
     */
    private boolean paused;

    //Bonuses
    /**
     * Stores if a bonus is running or not.
     */
    private boolean BonusRunning = false;

    /**
     * Stores the minimun time possible for a bonus ball to appear after another.
     */
    private int MinTime = 20;

    /**
     * Stores the time range in which a bonus ball can appear after the minimun time has been reached.
     */
    private int TimeRange = 20;

    /**
     * Stores the time until a bonus should appear.
     */
    private double BonusTimer;

    /**
     * Stores the type of bonus the current bonus ball is.
     */
    private int BonusType;

    /**
     * Stores the coulours possible for a bonus ball.
     */
    private Color[] BonusColor = { Color.blue, Color.cyan, Color.darkGray,
            Color.gray, Color.green, Color.lightGray, Color.magenta,
            Color.orange, Color.pink, Color.red, Color.white };

    //Missile
    /**
     * Stores if each players missiles are ready or not.
     */
    private int[][] MissileReady;

    /**
     * Stores the current delay left before a player can fire a missile again.
     */
    private int[] MissileDelay = { 0, 0 };

    /**
     * Stores if a paddle is stunned or not.
     */
    private int[] Stunned = { 0, 0 };

    //AI
    /**
     * Stores the delay left before a computer can fire a missile.
     */
    private int[] AImissileDelay = { 0, 0 };

    //Ending
    /**
     * Stores the player which who won, and the ending msg to dsiplay.
     */
    private String winMsg;

    /**
     * Stores if the game is over or not.
     */
    private int gameOver = 0;

    public void init() {
        //TODO convert to self applet
    }

    /**
     * The constructor for PaddleWars, used to pass in settings and resources.
     *
     * @param width The width of the area in which the game should operate.
     * @param height The height of the area in which the game should operate.
     * @param gameSounds All the sounds needed for the game.
     * @param gameMode The current game mode.
     */
    public PaddleWars(int width, int height, AudioClip[] gameSounds, int gameMode) {

        this.addMouseListener(MouseyHandler);
        this.addKeyListener(new KeyHandler());

        aWidth = width;
        aHeight = height;

        WallHit[0] = gameSounds[3];
        WallHit[1] = gameSounds[4];
        PaddleSounds[0][0] = gameSounds[0];
        PaddleSounds[1][0] = gameSounds[0];
        PaddleSounds[0][1] = gameSounds[1];
        PaddleSounds[1][1] = gameSounds[1];
        PaddleSounds[0][2] = gameSounds[2];
        PaddleSounds[1][2] = gameSounds[2];

        Px[0] = (int) Math.round(aWidth * 0.1);
        Px[1] = (aWidth - (Px[0] + PSizeX[1]));

        int missiles = Missiles[0].length;
        MissileReady = new int[2][missiles];

        if (gameMode == 0) {
            Computer = new AI[1];
            pU[0] = 0;
            pD[0] = 0;
            pB[0] = 0;
            pM[0] = 0;
        } else if (gameMode == 1) {
            Computer = null;
        } else if (gameMode == 2) {
            Computer = null;
            pU[1] = 0;
            pD[1] = 0;
            pB[1] = 0;
            pM[1] = 0;
            PSizeY[1] = aHeight / 2;
            PaddleColour[1] = 3;
            PSizeX[1] = 10;
            PaddleSounds[1][0] = gameSounds[3];
        }

        this.gameMode = gameMode;

        try {
            predictTime = new int[Computer.length];
        } catch (NullPointerException e) {
            predictTime = null;
        }

    }

    /**
     * Method used to start up the game. Initializes all the variables and objects.
     */
    public void start() {

        Ball[0] = new Ball((aWidth / 2), (aHeight / 2), 5, aWidth, aHeight, WallHit[0]);

        Ball[1] = new Ball((aWidth / 2), (aHeight / 2), 5, BonusColor, BonusType,
                									aWidth, aHeight, WallHit[0]);
        Ball[1].setBallVelocityX(0);
        Ball[1].setBallVelocityY(0);

        int MissileLength = Missiles[0].length;

        for (int i = 0; i < MissileLength; i++) {
            Missiles[0][i] = new Ball((aWidth / 2), (aHeight / 2), 5, aWidth,
                    							aHeight, WallHit[1], i, 0);
            Missiles[0][i].setBallVelocityX(0);
            Missiles[0][i].setBallVelocityY(0);
            Missiles[0][i].increaseSize(-2);
            Missiles[1][i] = new Ball((aWidth / 2), (aHeight / 2), 5, aWidth,
                    aHeight, WallHit[1], i, 1);
            Missiles[1][i].setBallVelocityX(0);
            Missiles[1][i].setBallVelocityY(0);
            Missiles[1][i].increaseSize(-2);
        }

        Paddle[0] = new Paddle(Px[0], ((aHeight - PSizeY[0]) / 2), PSizeX[0],
                				PSizeY[0], aHeight, Gspeed, 0, PaddleSounds[0]);
        Paddle[1] = new Paddle(Px[1], ((aHeight - PSizeY[1]) / 2), PSizeX[1],
                				PSizeY[1], aHeight, Gspeed, 1, PaddleSounds[1]);

        bonManager = new BonusManager();

        try {
            
            for (int i = 0; i < Computer.length; i++)
                Computer[i] = new AI(i);
            
        } catch (NullPointerException e) {
            System.err.println("Caught NullPointerException: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Caught ArrayIndexOutOfBoundsException: "
                    									+ e.getMessage());
        }

        BonusTimer = Math.round((Math.random() * TimeRange + MinTime)
                										* (10 / Gspeed));

        if (gameThread == null) {
            runFlag = 1;
            gameThread = new Thread(this, "Game"); //initializes the thread, gameThread.
            gameThread.start(); //starts the thread, gameThread.
        }

    }

    /**
     * Standard paint method, converted to a 2D paint method.
     *
     * @param g The graphics object.
     */
    public void paint(Graphics g) {

        //cast the graphics object g, into a 2D graphics object
        Graphics2D g2 = (Graphics2D) g;

        //draw a black background
        g2.setColor(Color.black);
        g2.fillRect(0, 0, aWidth, aHeight);

        //Scores
        Font scoreFont = new Font("TimesRoman", 1, (int) (0.026 * aWidth));
        Color scoreColor = new Color(124, 252, 124, 200);

        //set the current font and colour
        g2.setFont(scoreFont);
        g2.setColor(scoreColor);

        //convert The score integer to a string, so we can draw it.
        String P1ScoreString = Integer.toString(Score[0]);
        String P2ScoreString = Integer.toString(Score[1]);

        //draw the strings to the screen
        g2.drawString(P1ScoreString, (int) (aWidth * 0.14), (int) (aHeight * 0.032));
        g2.drawString(P2ScoreString, (int) (aWidth * 0.85), (int) (aHeight * 0.032));

        //draw Ball[0] to the graphics object
        Ball[0].paint(g2);

        //test if the bonus ball is running and if so draw it
        if (BonusRunning == true)
            Ball[1].paint(g2);

        //get the number of missiles
        int MissileLength = Missiles[0].length;

        //draw them all to the graphics object
        for (int i = 0; i < MissileLength; i++) {

            Missiles[0][i].paint(g2);
            Missiles[1][i].paint(g2);

        }

        //draw the two paddles to the graphics object
        Paddle[0].paint(g2);
        Paddle[1].paint(g2);

        //Test if the game is paused & if so, preint "PAUSED" on the screen
        if (paused == true) {
            g2.setColor(Color.RED);
            g2.drawString("PAUSED", (int) (aWidth * 0.45),
                    			(int) (aHeight * 0.51));
        }

        if (gameOver == 1) {
            g2.setColor(Color.RED);
            g2.drawString(winMsg, (int) (aWidth * 0.45),
                            (int) (aHeight * 0.51));
        }

        //Debugging info - draws the AI prediction ball.
        g2.setColor(Color.red);
        g2.fillOval((int)Computer[0].AIball.x,(int)Computer[0].AIball.y,20,20);
        //g2.setColor(Color.yellow);
        //g2.fillOval((int)Computer[1].AIball.x,(int)Computer[1].AIball.y,20,20);
        //*/

    }

    /**
     * Method used to set, and/or return if a player has the Hyper key.
     *
     * @param acessMode If you want to set if a player has the hyper key, or just 
     * find out if they do or not.
     * @param paddle Which paddle you want to perform the operations on.
     * @param giveHyperKey ive them the Hyper Key or not, 0= not, 1=yes. 
     * Only relevant if acessMode = 1.
     * @return The Hyper key Status of the player.
     */
    public int hyperKey(int acessMode, int paddle, int giveHyperKey) {

        if (acessMode == 1)
            gotHyperKey[paddle] = giveHyperKey;
        
        return gotHyperKey[paddle];

    }

    /**
     * Sets or returns the current hyper movement value for a player.
     *
     * @param acessMode Set it, 1, or retunrn it, 0.
     * @param paddle Which paddle to set or find.
     * @param increasePercent The percent by which to increase the hyper movement value.
     * @return The current hyper movement value for a player.
     */
    public int setHyperMovement(int acessMode, int paddle, int increasePercent) {

        if (acessMode == 1)
            pMvB[paddle] = pMvB[paddle] * (increasePercent / 100);

        return pMvB[paddle];

    }

    /**
     * This method is used to increase the score.
     *
     * @param P1 The value by which to increase player 1's score.
     * @param P2 The value by which to increase player 2's score.
     */
    public void setScore(int P1, int P2) {

        if (gameMode != 2) {
            Score[0] += P1;
            Score[1] += P2;
        }

        if (Score[0] <= 0)
            endGame(0);
        else if (Score[1] <= 0)
            endGame(1);

    }

    /**
     * This method sets if a bonus is running or not.
     *
     * @param isRunning true = running, flase = not.
     */
    public void setBonusRunning(boolean isRunning) {

        BonusRunning = isRunning;

        if (BonusRunning == false) {
            Ball[1] = new Ball(aWidth / 2, aHeight / 2, 5, BonusColor,
                    		BonusType, aWidth, aHeight, WallHit[0]);
            Ball[1].setBallVelocityX(0);
            Ball[1].setBallVelocityY(0);
        }
    }

    /**
     * This method sets the predict time left until a computer predicts the balls loation.
     *
     * @param time The time till the AIshould predict the ball location.
     */
    public void setpredictTime(int time) {

        if (gameMode == 0) {
            try {
                for (int i = 0; i < predictTime.length; i++) {
                    predictTime[i] = time;
                }
            } catch (NullPointerException e) {
                System.err.println("Caught NullPointerException: "
                        + e.getMessage());
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("Caught ArrayIndexOutOfBoundsException: "
                        + e.getMessage());
            }
        }

    }

    /**
     * This method sets the predict time left until a computer predicts the balls loation, or tells it to strafe, depending on which paddle the ball just hit.
     *
     * @param WhichPaddle Which paddle the ball just hit.
     * @param time Time until the AI should predict the ball's loation.
     */
    public void setpredictTime(int WhichPaddle, int time) {

        if (gameMode == 0) {
            if (WhichPaddle == 0) {
                try {
                    Computer[0].setBallLocation(aHeight / 2);
                    Computer[0].setStrafe(aHeight / 2 - 60, aHeight / 2 + 60);
                } catch (NullPointerException e) {
                    System.err.println("Caught NullPointerException: "
                            + e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err
                            .println("Caught ArrayIndexOutOfBoundsException: "
                                    + e.getMessage());
                }
            } else if (WhichPaddle == 1) {
                try {
                    predictTime[0] = time;
                } catch (NullPointerException e) {
                    System.err.println("Caught NullPointerException: "
                            + e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err
                            .println("Caught ArrayIndexOutOfBoundsException: "
                                    + e.getMessage());
                }
            }
        }

    }

    /**
     * This method organizes the binus ball timer. It reduces it until it is equal to 0, then it creates a new bonus ball, and a new binus type, and timer.
     */
    private void BonusTimer() {

        if (BonusRunning == false) {

            if (BonusTimer > 0) {

                BonusTimer -= 1;

            } else {

                BonusRunning = true;
                BonusTimer = Math.round((Math.random() * TimeRange + MinTime)
                        * (1000 / Gspeed));
                BonusType = (int) Math.round(Math.random() * 10);

                Ball[1] = new Ball(aWidth / 2, aHeight / 2, 5, BonusColor,
                        BonusType, aWidth, aHeight, WallHit[0]);
                if (Math.random() > 0.5) {
                    Ball[1].setBallVelocityX(-1);
                } else {
                    Ball[1].setBallVelocityX(1);
                }

            }

        }

    }

    /**
     * This method gives a missile paddle to a player. And resets all their missiles to ready.
     *
     * @param player Which player to give the missile paddle.
     */
    public void giveMissilePaddle(int player) {

        GotM[player] = 1;

        int missileAmount = Missiles[0].length;

        for (int i = 0; i < missileAmount; i++) {

            MissileReady[player][i] = 1;

        }

        /* Debugging
         System.out.println("Player : "+player+";");
         System.out.println("GotM["+player+"] : "+GotM[player]+";");
         System.out.println("MissileReady["+player+"][0] : "+MissileReady[player][0]+";");
         System.out.println("MissileReady["+player+"][1] : "+MissileReady[player][1]+";");
         System.out.println("MissileReady["+player+"][2] : "+MissileReady[player][2]+";");
         */

    }

    /**
     * This method finds if a player has a missile ready, and if so, calls on the FireMissile method to fire that missile.
     *
     * @param player Which player is trying to fire the misile.
     */
    private void MissileHandler(int player) {

        int amountMissiles = Missiles[0].length;

        for (int i = 0; i < amountMissiles; i++) {
            if (MissileReady[player][i] == 1 && MissileDelay[player] <= 0) {
                FireMissile(player, i);
                break;
            }
        }

    }

    /**
     * This method fires a misile, and then sets that missile to not availible.
     *
     * @param player Which player is firing the missile.
     * @param missile Which missile of theirs to fire.
     */
    private void FireMissile(int player, int missile) {

        MissileDelay[player] = 5 * (100 / Gspeed);
        MissileReady[player][missile] = 0;
        int MissileX = Paddle[player].getMissileFireSpotX(player);
        int MissileY = Paddle[player].getMissileFireSpotY();

        MissileX += (player == 0) ? 12 : -16;

        Missiles[player][missile] = new Ball(MissileX, MissileY, 5, aWidth,
                aHeight, WallHit[1], missile, player);
        int MissileDirection = (player == 0) ? -3 : 3;
        Missiles[player][missile].setBallVelocityX(MissileDirection);

    }

    /**
     * This method resets a missile, stopping it from running anymore.
     *
     * @param player Which player owns the missile.
     * @param missile Which missile of that players to reset.
     */
    public void resetMissile(int player, int missile) {

        Missiles[player][missile].setBallVelocityX(0);
        Missiles[player][missile].setBallVelocityY(0);
        Missiles[player][missile].setBall(-70, 60);
        Missiles[player][missile].increaseSize(-2);

    }

    /**
     * This method sets the stunned time on a paddle.
     *
     * @param paddle Which paddle to set stunned.
     * @param splits For how many spilt seconds they should be stunned.
     */
    public void setStunned(int paddle, int splits) {

        Stunned[paddle] = splits * (100 / Gspeed);

    }

    /**
     * This method ends the game.
     *
     * @param winner Which player won the game.
     */
    private void endGame(int winner) {
        if (winner == 0)
            winner = 2;
        
        winMsg = ("Player " + winner + " Wins!");
        gameOver = 1;
        repaint();
        gameThread = null;
        this.removeMouseListener(MouseyHandler);

    }

    /**
     * This method is used to exit the game. It stops the thread, and releases any objects and variables, giving the resources back to the system.
     */
    public void exitGame() {

        //change the runFlag to 0 to notify the thread to finish
        runFlag = 0;

        //Release all the objects to reset them for future play,
        //and conserve system resources

        //Balls
        try {
            for (int i = 0; i < Ball.length; i++) {
                Ball[i] = null;
            }
            //Catch an error that will occur if the array
            //hasnt been initialized
        } catch (NullPointerException e) {
            System.err
                    .println("Caught NullPointerException: " + e.getMessage());
            //Catch an error that will occur if an element not within
            //the arrays bounds is tryed to access.
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Caught ArrayIndexOutOfBoundsException: "
                    + e.getMessage());
        }
        //Missiles
        try {
            for (int i = 0; i < Missiles[0].length; i++) {
                Missiles[0][i] = null;
                Missiles[1][i] = null;
            }
        } catch (NullPointerException e) {
            System.err
                    .println("Caught NullPointerException: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Caught ArrayIndexOutOfBoundsException: "
                    + e.getMessage());
        }
        //Paddles
        try {
            for (int i = 0; i < Paddle.length; i++) {
                Paddle[i] = null;
            }
        } catch (NullPointerException e) {
            System.err
                    .println("Caught NullPointerException: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Caught ArrayIndexOutOfBoundsException: "
                    + e.getMessage());
        }
        //Computer
        try {
            for (int i = 0; i < Computer.length; i++) {
                Computer[i] = null;
            }
        } catch (NullPointerException e) {
            System.err
                    .println("Caught NullPointerException: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Caught ArrayIndexOutOfBoundsException: "
                    + e.getMessage());
        }

        try {
            bonManager = null;
        } catch (NullPointerException e) {
            System.err
                    .println("Caught NullPointerException: " + e.getMessage());
        }

        //Key Handlers
        this.removeAll();

    }

    /**
     * This method manages all the timers present in this class.
     */
    private void DelayTimer() {

        //A if - else statement used to reduce the MissileDelay int.
        //this
        if (MissileDelay[0] > 0) {
            MissileDelay[0]--;
        } else if (MissileDelay[1] > 0) {
            MissileDelay[1]--;
        }

        //A timing method that reduces the AImissileDelay variable
        //The point of this variable is to stop the computer(s) firing all their missiles at once.
        if (AImissileDelay[0] > 0) {
            AImissileDelay[0]--;
        }
        if (AImissileDelay[1] > 0) {
            AImissileDelay[1]--;
        }

        //A timing method that reduces the predictTime variable
        //The point of this variable is to fix a bug with the AI prediction routine.
        //The Bug is due to the AI trying to predict the ball location,
        //while the new ball velocity is being calculated.
        if (gameMode == 0) {
            try {
                for (int i = 0; i < predictTime.length; i++) {
                    predictTime[i]--;
                }
            } catch (NullPointerException e) {
                System.err.println("Caught NullPointerException: "
                        + e.getMessage());
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("Caught ArrayIndexOutOfBoundsException: "
                        + e.getMessage());
            }
        }

    }

    /**
     * This is a standard run method for a thread. this method get run when the gameThread is called to run, and continues to do so until the exitGame method is called, or paused.
     */
    public void run() { //the method that runs when the thread is started. The Thread method.

        Thread myThread = Thread.currentThread();

        while (gameThread == myThread && runFlag == 1) {

            repaint();

            BonusTimer();
            DelayTimer();

            if (gameMode == 0) {
                try {
                    for (int i = 0; i < Computer.length; i++) {
                        Computer[i].Runner();
                    }
                } catch (NullPointerException e) {
                    System.err.println("Caught NullPointerException: "
                            + e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err
                            .println("Caught ArrayIndexOutOfBoundsException: "
                                    + e.getMessage());
                }
            }

            try {
                Ball[0].runner();
                Ball[1].runner();
            } catch (NullPointerException e) {
                System.err.println("Caught NullPointerException: "
                        + e.getMessage());
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("Caught ArrayIndexOutOfBoundsException: "
                        + e.getMessage());
            }

            try {
                for (int i = 0; i < Missiles[0].length; i++) {
                    Missiles[0][i].runner();
                    Missiles[1][i].runner();
                }
            } catch (NullPointerException e) {
                System.err.println("Caught NullPointerException: "
                        + e.getMessage());
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("Caught ArrayIndexOutOfBoundsException: "
                        + e.getMessage());
            }

            try {
                if (Stunned[0] == 0) {
                    Paddle[0].move(PyM[0]);
                } else {
                    Stunned[0]--;
                }
            } catch (NullPointerException e) {
                System.err.println("Caught NullPointerException: "
                        + e.getMessage());
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("Caught ArrayIndexOutOfBoundsException: "
                        + e.getMessage());
            }

            try {
                if (Stunned[1] == 0) {
                    Paddle[1].move(PyM[1]);
                } else {
                    Stunned[1]--;
                }
            } catch (NullPointerException e) {
                System.err.println("Caught NullPointerException: "
                        + e.getMessage());
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("Caught ArrayIndexOutOfBoundsException: "
                        + e.getMessage());
            }

            try {
                Thread.sleep(Gspeed);
            } catch (ThreadDeath Death) {
                System.err.println("Thread Death caught");
                throw (Death);
            } catch (InterruptedException e) {
                gameThread = null;
                System.err.println("Runtime Error: " + e.getMessage());
            }

        }
    }

    class MouseHandler extends MouseAdapter {

        /**
         * This method handles any mouse clicked events.
         *
         * @param e The mouse button which was prssed.
         */
        public void mouseClicked(MouseEvent e) {

            mouseClicks++;

            if ((mouseClicks % 2) != 0) {
                gameThread = null;
                paused = true;
                repaint();
            } else {
                paused = false;
                gameThread = new Thread(Interface.paddleWars, "Game");
                gameThread.start();
            }

        }

    }

    class KeyHandler extends KeyAdapter {

        /**
         * Handle the key pressed event.
         *
         * @param e The key which was pressed.
         */
        public void keyPressed(KeyEvent e) {

            //get the ASSCI value of the key pressed.
            int key = e.getKeyCode();

            //test if the key pressed down is player 1's missile key
            //and that he has the missile paddle, and isnt stunned
            if (key == pM[0] && GotM[0] == 1 && Stunned[0] == 0)
                MissileHandler(0);

            //same as above but for player 2.
            if (key == pM[1] && GotM[1] == 1 && Stunned[1] == 0)
                MissileHandler(1);

            //check that the key isnt already down.
            if (keyStatus[key] == 0) {

                //set the key to down
                keyStatus[key] = 1;

                //check if player 1's Hyper key is ALREADY down.
                if (keyStatus[pB[0]] == 1) {

                    //check which key has been pressed and act accordingly
                    if (key == pU[0]) {
                        PyM[0] += -pMvB[0];
                    }
                    if (key == pD[0]) {
                        PyM[0] += pMvB[0];
                    }

                    //if player 1's Hyper key isnt ALREADY down.
                } else if (keyStatus[pB[0]] == 0) {

                    //check which key has been pressed and act accordingly
                    if (key == pU[0]) {
                        PyM[0] += -PyMv[0];
                    }
                    if (key == pD[0]) {
                        PyM[0] += PyMv[0];
                    }

                }

                //same as above, but for player 2.
                if (keyStatus[pB[1]] == 1) {

                    if (key == pU[1]) {
                        PyM[1] += -pMvB[1];
                    }
                    if (key == pD[1]) {
                        PyM[1] += pMvB[1];
                    }

                } else if (keyStatus[pB[1]] == 0) {

                    if (key == pU[1]) {
                        PyM[1] += -PyMv[1];
                    }
                    if (key == pD[1]) {
                        PyM[1] += PyMv[1];
                    }

                }

                //check if the key JUST pressed is player 1's Hyper.
                if (key == pB[0]) {

                    //reset the movement value
                    PyM[0] = 0;
                    //check wat keys are ALREADY down, and reaply their movement
                    //with the Hyper value
                    if (keyStatus[pU[0]] == 1) {
                        PyM[0] += -pMvB[0];
                    }
                    if (keyStatus[pD[0]] == 1) {
                        PyM[0] += pMvB[0];
                    }

                }

                //same as just above, but for player 2
                if (key == pB[1]) {

                    PyM[1] = 0;
                    //check wat keys are ALREADY down, and reaply their movement
                    //with the Hyper value
                    if (keyStatus[pU[1]] == 1) {
                        PyM[1] += -pMvB[1];
                    }
                    if (keyStatus[pD[1]] == 1) {
                        PyM[1] += pMvB[1];
                    }

                }

            }

        }

        /**
         * Handle the key released event
         *
         * @param e The Key which was released.
         */
        public void keyReleased(KeyEvent e) {

            //get the instance variables required.
            int[] pMvBI = Interface.paddleWars.pMvB;

            //get the ASSCI value of the key JUST pressed.
            int key = e.getKeyCode();

            //if player 1's Hyper key is ALREADY down
            if (keyStatus[pB[0]] == 1) {

                //see if one of the following key's were pressed
                //and act accordingly.
                if (key == pU[0]) {
                    PyM[0] -= -pMvBI[0];
                } //Player 1 up Hyper
                if (key == pD[0]) {
                    PyM[0] -= pMvBI[0];
                } //Player 1 down Hyper

                //if player 1's Hyper movement key isnt down
            } else if (keyStatus[pB[0]] == 0) {

                //see if one of the following key's were pressed
                //and act accordingly.
                if (key == pU[0]) {
                    PyM[0] -= -PyMv[0];
                } //Player 1 up
                if (key == pD[0]) {
                    PyM[0] -= PyMv[0];
                } //Player 1 down

            }

            //The same as just above, but for player 2.
            if (keyStatus[pB[1]] == 1) {

                if (key == pU[1]) {
                    PyM[1] -= -pMvBI[1];
                } //player 2 up Hyper.
                if (key == pD[1]) {
                    PyM[1] -= pMvBI[1];
                } //player 2 down Hyper.

            } else if (keyStatus[pB[1]] == 0) {

                if (key == pU[1]) {
                    PyM[1] -= -PyMv[1];
                } //player 2 up
                if (key == pD[1]) {
                    PyM[1] -= PyMv[1];
                } //player 2 down

            }

            //See if the key released was player 1's Hyper key.
            if (key == pB[0]) {

                PyM[0] = 0; //reset player 1's movement
                //check wat keys are ALREADY down, and reaply their movement
                //to cancel out the Hyper movement
                if (keyStatus[pU[0]] == 1) {
                    PyM[0] += -PyMv[0];
                } //player 1 up
                if (keyStatus[pD[0]] == 1) {
                    PyM[0] += PyMv[0];
                } //player 1 down

            }

            //Same as just above but for player 2.
            if (key == pB[1]) {

                PyM[1] = 0; //reset player 2's movement
                //check wat keys are ALREADY down, and reapply their movement
                //to cancel out the Hyper movement
                if (keyStatus[pU[1]] == 1) {
                    PyM[1] += -PyMv[1];
                } //player 2 up
                if (keyStatus[pD[1]] == 1) {
                    PyM[1] += PyMv[1];
                } //player 2 down

            }

            //Finally, reset the key's status to up, so that it may be pressed again
            keyStatus[key] = 0;

        }

    }

    class AI {

        /**
         * Stores which paddle the AI is controlling.
         */
        private int ThisPaddle;

        /**
         * Stores the posistion of the predicted ball location.
         */
        public Point2D.Double AIball = new Point2D.Double();

        /**
         * Stores the posistion of the predicted ball location.
         */
        private Point2D.Double ballLocation = new Point2D.Double();

        /**
         * Stores the random Skill of the AI.
         */
        private int SkillMultiplyer;

        /**
         * Stores which paddle zone the AI will try to hit the ball in.
         */
        private int whichZone;

        /**
         * Stores the Direction the AI is strafing in.
         */
        private int strafeDirection = 0;

        /**
         * Stores if the AI should strafe or not.
         */
        private int strafeFlag = 1;

        /**
         * The minimun point the AI should go to when strafing.
         */
        private int yMin = aHeight / 2;

        /**
         * The maximun point the AI should go to when strafing.
         */
        private int yMax = aHeight / 2;

        /**
         * Standard constructor for AI.
         *
         * @param WhichPaddle Which paddle the AI should control.
         */
        public AI(int WhichPaddle) {

            ballLocation.x = aWidth / 2;
            ballLocation.y = aHeight / 2;

        }

        /**
         * This method is used to run the AI.
         */
        public void Runner() {

            movement();
            missile();

            if (strafeFlag == 1 && PyM[ThisPaddle] == 0) {
                paddleStrafe();
            }

        }

        /**
         * This method causes the paddle to strafe.
         */
        public void paddleStrafe() {

            strafeDirection++;
            if (strafeDirection == 2) {
                ballLocation.y = yMax;
                strafeDirection = 0;
            } else {
                ballLocation.y = yMin;
            }

        }

        /**
         * This method sets the minimun and maximun strafing location, and starts the strafe action.
         *
         * @param yMin The minimun point to strafe to.
         * @param yMax The maximun point to strafe to.
         */
        public void setStrafe(int yMin, int yMax) {

            this.yMin = yMin;
            this.yMax = yMax;
            strafeFlag = 1;

        }

        /**
         * This method is usedt to set the location of the predicted ball location
         *
         * @param y The y-co-ordinate of where the predicted ball 'is'.
         */
        public void setBallLocation(int y) {

            ballLocation.y = y;

        }

        /**
         * This method moves the paddle, it calls on the predictBall method to find out where it should strafe to.
         */
        private void movement() {

            int BallS = Ball[0].getBallSize();
            int BallR = Ball[0].getBallRadius();

            if (predictTime[ThisPaddle] == 0) {

                //This contains code that only needs to be run once, at the begginging of each turn of the AI.
                strafeFlag = 0;
                ballLocation = predictBallLocation();
                ballLocation.x += BallS * BallR;
                ballLocation.y += BallS * BallR;
                predictTime[ThisPaddle] = -1;
                SkillMultiplyer = (int) (Math.random() * 15);
                whichZone = 1 + (int) (Math.round(Math.random() * 2));

            }

            int PaddleT = Paddle[ThisPaddle].getPaddleTop();
            int PaddleB = Paddle[ThisPaddle].getPaddleBottom();

            int BaseSkill = 0;

            int topBoundary = PaddleT - (BallR * BallS);
            int bottomBoundary = PaddleB + (BallS * BallR) - 3;

            if (ballLocation.y < topBoundary) {

                PyM[ThisPaddle] = -pMvB[ThisPaddle];

            } else if (ballLocation.y > bottomBoundary) {

                PyM[ThisPaddle] = pMvB[ThisPaddle];

            } else {

                //Top part of Paddle
                if (whichZone == 1) {
                    if (ballLocation.y > topBoundary + BaseSkill
                            + SkillMultiplyer) {
                        PyM[ThisPaddle] = pMvB[ThisPaddle];
                    } else {
                        PyM[ThisPaddle] = 0;
                    }
                    //Middile of Paddle
                } else if (whichZone == 2) {

                    if (ballLocation.y < topBoundary + BaseSkill
                            + SkillMultiplyer) {
                        PyM[ThisPaddle] = -pMvB[ThisPaddle];
                    } else if (ballLocation.y > bottomBoundary - BaseSkill
                            - SkillMultiplyer) {
                        PyM[ThisPaddle] = pMvB[ThisPaddle];
                    } else {
                        PyM[ThisPaddle] = 0;
                    }

                    //Bottom part of paddle
                } else if (whichZone == 3) {

                    if (ballLocation.y < bottomBoundary - BaseSkill
                            - SkillMultiplyer) {
                        PyM[ThisPaddle] = -pMvB[ThisPaddle];
                    } else {
                        PyM[ThisPaddle] = 0;
                    }

                    //Handle any unccounted for exception
                } else {

                    PyM[ThisPaddle] = 0;

                }

            }

        }

        /**
         * This method is used to predict where the ball is going.
         *
         * @return returns the x and y posistion of where it willl be when it is at
         *     hitting distance to a paddle.
         */
        private Point2D.Double predictBallLocation() {

            Point2D.Double ball = Ball[0].getBallCenter();
            int size = Ball[0].getBallSize();
            int r = Ball[0].getBallRadius();

            int xM = Ball[0].getVelocity(0);
            int yM = Ball[0].getVelocity(1);
            int Speed = Ball[0].getSpeed();

            Point2D.Double PaddleC = Paddle[ThisPaddle].getPaddleCenter();

            int topWall = (r * size);
            int bottomWall = aHeight - (size * r);

            int safteyBreak = 0;

            do {

                ball.x += xM * Speed;
                ball.y += Speed * (yM / 2);

                if (ball.y <= topWall) {

                    ball.y = topWall + 1;
                    yM = -yM;

                }

                if (ball.y >= bottomWall) {

                    ball.y = bottomWall - 1;
                    yM = -yM;

                }

                if (ball.x < 0 || ball.x > aWidth) {

                    break;

                }

                if (ThisPaddle == 0
                        && (int) ball.x < ((int) PaddleC.x - (size * r))) {
                    break;
                }

                if (ThisPaddle == 1
                        && (int) ball.x > ((int) PaddleC.x - (size * r))) {
                    break;
                }

                safteyBreak++;

            } while (safteyBreak < 2000);

            AIball.x = ball.x;
            AIball.x += (ThisPaddle == 0) ? (r * size) : -(r * size);
            AIball.y = ball.y - (r * size);

            return AIball;

        }

        /**
         * This method controls the AI's missile, and tells it when to fire one, based on 3 different hit zones, depending on the enmey's movement.
         */
        private void missile() {

            //decide which paddle is the enemy
            int OtherPaddle = Math.abs(ThisPaddle - 1);

            //Get the variables needed, locations of various parts of both paddles.
            int PaddleTe = Paddle[OtherPaddle].getPaddleTop();
            int PaddleBe = Paddle[OtherPaddle].getPaddleBottom();

            int PaddleT = Paddle[ThisPaddle].getPaddleTop();
            int PaddleB = Paddle[ThisPaddle].getPaddleBottom();
            int PaddleM = Paddle[ThisPaddle].getMissileFireSpotY();

            //Cheack that the paddle is able to fire missiles.
            if (GotM[ThisPaddle] == 1 && Stunned[ThisPaddle] == 0
                    && AImissileDelay[ThisPaddle] <= 0) {

                //check the paddles movement direction with three nested if statements,
                //as the fire zone is different in each case.
                //The AI's paddle movement is irrelevent.

                //Runs This if the enemy Paddle is moving up
                if (PyM[OtherPaddle] < 0) {

                    //test if the enemy paddle is within the boundaries of the hit zone.
                    //Only in the bottom part however to allow for paddle movement.
                    if (PaddleTe < PaddleB + 10 && PaddleTe > PaddleM + 5) {
                        MissileHandler(ThisPaddle);
                        AImissileDelay[1] = 25 * (int) (Math
                                .round(100 / Gspeed));
                    }

                }

                //Runs this if the enemy Paddle is stationary
                if (PyM[OtherPaddle] == 0) {

                    //test to see if the enemy paddle is within the hit zone.
                    if (PaddleM > PaddleTe && PaddleM < PaddleBe) {
                        MissileHandler(ThisPaddle);
                        AImissileDelay[ThisPaddle] = 25 * (int) (Math
                                .round(100 / Gspeed));
                    }

                }

                //Runs This if the enemy Paddle is moving down
                if (PyM[OtherPaddle] > 0) {

                    //test if the enemy paddle is within the boundaries of the hit zone.
                    //Only in the top part however to allow for paddle movement.
                    if (PaddleBe > PaddleT - 10 && PaddleBe < PaddleM - 5) {
                        MissileHandler(ThisPaddle);
                        AImissileDelay[ThisPaddle] = 25 * (int) (Math
                                .round(100 / Gspeed));
                    }

                }

            }

        }

    }

}