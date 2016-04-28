package com.TeamJava.PaddleWars;

//Java Class Imports

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.applet.AudioClip;

//End of Java Class imports

/**
 * This class creates a rounded rectangle, which acts as a paddle. It creates the paddle with its various properties, it also animates it, and moves it. This class is also responsible for creating and managing the various instances need of the intercept class.
 * 
 * @author David Terei
 * @see PaddleWars
 * @see InterceptManager
 * @version 22/8/03
 */
public class Paddle extends JPanel {

    /**
     * An array of the intercept class. The arraylength is equal to the number of balls their are. This array manages the intercept class for each ball for this paddle.
     * 
     * @see InterceptManager
     */
    private static InterceptManager[] BallIntercept;

    /**
     * An array of the intercept class. The arraylength is equal to the number of missile for each player their are. This array manages the intercept class for each missile for this paddle.
     * 
     * @see InterceptManager
     */
    private static InterceptManager[][] MissileIntercept;

    /**
     * Stores which paddle this is, in terms of its array posistion in the PaddleWars paddles variable.
     * 
     * @see PaddleWars
     */
    private int WhichPaddle;

    /**
     * Stores the top left location of the paddle.
     */
    private Point2D.Double paddle = new Point2D.Double();

    /**
     * Stores the fixed size of the x and y size of the paddle.
     */
    private Point2D.Double size = new Point2D.Double();

    /**
     * Stores the variable y size of the paddle.
     */
    private int pSizeY = 2;

    /**
     * Stores the variable x size of the paddle.
     */
    private int pSizeX = 2;

    ///**
    // * Stores the array of Images of the paddle, which form its animation.
    // */
    //private Image[] PaddleImage = new Image[4];
    /**
     * Stores which frame the paddle's image animation is up to, in terms of which picture in the PaddleImage array.
     */
    private int frame = 0;

    /**
     * Stores how many times in the threads cycle, that a image in the  Paddle animation cycle has been displayed. This is to slow down the animation.
     */
    private int Ecycle = 0;

    /**
     * Stores how many times in the threads cycle, that a image in the  Explosion animation cycle has been displayed. This is to slow down the animation.
     */
    private int Pcycle = 0;

    /**
     * Stores for how long an image in the animation cycle for the paddle should be displayed.
     */
    private int animationTimer = 0;

    //**
    // * Stores the images needed for the explosion animation.
    // */
    //private Image[] Explosion;
    /**
     * Stores which frame the explosion's image animation is up to, in terms of which picture in the Explosion array.
     */
    private int ExpFrame;

    /**
     * A flag to tell if the explosion should run or not.
     */
    private boolean expFlag = false;

    /**
     * The spot at which the explosion animation should occur.
     */
    private Point2D.Double expSpot = new Point2D.Double();

    /**
     * Stores the y size or height of the applet.
     */
    private int aHeight;

    /**
     * Stores the length of the Ball array in the PaddleWars class.
     * 
     * @see PaddleWars
     */
    private int BallLength;

    /**
     * Stores the length of the Missiles array in the PaddleWars class.
     * 
     * @see PaddleWars
     */
    private int MissileLength;

    /**
     * The audio clip to play when a ball hits the paddle.
     */
    private AudioClip[] paddleHit;

    /**
     * The constructor for the paddle class. Provides the variables needed to create a paddle of an size at any posistion.
     * 
     * @param paddleX The x posistion of the paddle.
     * @param paddleY The y posistion of the paddle.
     * @param pXsize The fixed x size of the paddle.
     * @param pYsize The fixed y size of the paddle.
     * @param screenY The height of the area in which the paddle will operate.
     * @param Gspeed The delay on the gameThread, or the Game Speed.
     * @param arrayN The array posistion of this paddle, in the Paddle array, 
     *     in the PaddleWars class.
     * @param paddleHit The sound clip to play if a ball hits the paddle.
     * @see PaddleWars
     */
    public Paddle(int paddleX, int paddleY, int pXsize, int pYsize,
            int screenY, int Gspeed, int arrayN, AudioClip[] paddleHit) {

        paddle.x = paddleX;
        paddle.y = paddleY;
        size.x = pXsize;
        size.y = pYsize;

        aHeight = screenY;

        BallLength = PaddleWars.Ball.length;

        BallIntercept = new InterceptManager[BallLength];

        for (int i = 0; i < BallLength; i++) {
            int ballType = PaddleWars.Ball[i].getBallType();
            BallIntercept[i] = new InterceptManager(i, ballType);
        }

        this.paddleHit = paddleHit;

        MissileLength = PaddleWars.Missiles[0].length;

        MissileIntercept = new InterceptManager[2][MissileLength];

        for (int i = 0; i < MissileLength; i++) {
            MissileIntercept[0][i] = new InterceptManager(0, i, 2);
            MissileIntercept[1][i] = new InterceptManager(1, i, 2);
        }

        WhichPaddle = arrayN;

        animationTimer = (int) Math.round(100 / Gspeed * 0.6);

    }

    /**
     * The standard painting method. Paints utilizing the 2D Graphics class. Paints the paddle. Also calls the instances of the intercept class, to test for an intercept with this paddle.
     * 
     * @param g The Graphics object used to paint with.
     */
    public void paint(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        Color paddleColor = new Color(60, 60, 60);
        g2.setColor(paddleColor);
        g2.fillRoundRect((int) paddle.x, (int) paddle.y, (int) size.x * pSizeX,
                (int) size.y * pSizeY, (int) size.x * pSizeX, (int) size.x
                        * pSizeX);
        //g2.drawImage(PaddleImage[frame],(int)paddle.x,(int)paddle.y,(int)size.x*pSizeX,(int)size.y*pSizeY,this);

        animatePaddle();

        if (expFlag == true) {
            animateExplosion();

            g2.fillOval((int) expSpot.x - 15, (int) expSpot.y - 15, 20, 20);
        }

        for (int i = 0; i < BallLength; i++) {

            Point2D.Double ball = PaddleWars.Ball[i].getBallCenter();
            int ballR = PaddleWars.Ball[i].getBallRadius();
            int ballS = PaddleWars.Ball[i].getBallSize();

            BallIntercept[i].TestIntercept(paddle, size, pSizeY, pSizeX, ball,
                    ballR, ballS, WhichPaddle, paddleHit[i]);

        }

        for (int i = 0; i < MissileLength; i++) {

            Point2D.Double Missiles1Center = PaddleWars.Missiles[0][i]
                    .getBallCenter();
            int Missiles1Radius = PaddleWars.Missiles[0][i].getBallRadius();
            int Missiles1Size = PaddleWars.Missiles[0][i].getBallSize();

            MissileIntercept[0][i].TestIntercept(paddle, size, pSizeY, pSizeX,
                    Missiles1Center, Missiles1Radius, Missiles1Size,
                    WhichPaddle, paddleHit[2]);

            Point2D.Double Missiles2Center = PaddleWars.Missiles[1][i]
                    .getBallCenter();
            int Missiles2Radius = PaddleWars.Missiles[1][i].getBallRadius();
            int Missiles2Size = PaddleWars.Missiles[1][i].getBallSize();

            MissileIntercept[1][i].TestIntercept(paddle, size, pSizeY, pSizeX,
                    Missiles2Center, Missiles2Radius, Missiles2Size,
                    WhichPaddle, paddleHit[2]);

        }

    }

    /**
     * This method animate the paddle. It does this by cycling through the Images that make up the animation, and that were passeed in through the constructor.
     */
    private void animatePaddle() {

        Pcycle++;
        if (Pcycle == animationTimer) {
            Pcycle = 0;
            if (frame == 3) {
                frame = -1;
            }
            frame++;

        }

    }

    /**
     * This method is used to animate the explosion by cycling through the images that make it up.
     */
    private void animateExplosion() {

        Ecycle++;
        if (Ecycle == animationTimer) {
            ExpFrame += 1;
            System.out.println(ExpFrame);
            if (ExpFrame == 11) {
                expFlag = false;
            }
            Ecycle = 0;
        }

    }

    /**
     * Moves the paddle along its y axis.
     * 
     * @param velocity The value by which to move the paddle down the screen.
     */
    public void move(int velocity) {

        paddle.y += velocity;

        if (paddle.y < 0) {
            paddle.y = 0;
        }
        if (paddle.y + (size.y * pSizeY) > aHeight) {
            paddle.y = (aHeight - (size.y * pSizeY));
        }

    }

    /**
     * Returns the value of the paddle's variable x size.
     * 
     * @return Paddle's variable x size.
     */
    public int getpSizeX() {

        return pSizeX;

    }

    /**
     * Returns the value of the paddle's variable y size.
     * 
     * @return Paddle's variable y size.
     */
    public int getpSizeY() {

        return pSizeY;

    }

    /**
     * Finds the x co-ordinates for where a missile should start, when fired by this paddle.
     * 
     * @param side Which side of the paddle the missile shoudl start, left or 
     *     right.
     * @return Co-ordinates for where a missile should start.
     * @see Ball
     */
    public int getMissileFireSpotX(int side) {

        if (side == 1) {

            return (int) (paddle.x);

        } else {

            return (int) (paddle.x + (size.x * pSizeX));

        }

    }

    /**
     * Finds the y co-ordinates for where a missile should start, when fired by this paddle.
     * 
     * @return y co-ordinates for where a missile should start, when fired by this
     *      paddle.
     * @see Ball
     */
    public int getMissileFireSpotY() {

        return (int) (paddle.y + ((size.y * pSizeY) / 2));

    }

    /**
     * Find the paddle's center.
     * 
     * @return Co-ordinates for the paddles center.
     */
    public Point2D.Double getPaddleCenter() {

        Point2D.Double paddleC = new Point2D.Double();

        paddleC.x = paddle.x + ((pSizeX * size.x) / 2);
        paddleC.y = paddle.y + ((pSizeY * size.y) / 2);

        return paddleC;

    }

    /**
     * Is used to find the y co-ordinate of the very top of the paddle.
     * 
     * @return Y co-ordinate of the very top of the paddle.
     */
    public int getPaddleTop() {

        return (int) paddle.y;

    }

    /**
     * Is used to find the y co-ordinate of the very bottom of the paddle.
     * 
     * @return Y co-ordinate of the very bottom of the paddle.
     */
    public int getPaddleBottom() {

        return (int) (paddle.y + (int) (size.y * pSizeY));

    }

    /**
     * Increases the variable x size of the paddle.
     * 
     * @param amount The amount by which to increase the variable x size of the 
     *     paddle,
     */
    public void increasepSizeX(int amount) {

        pSizeX += amount;

    }

    /**
     * Increases the variable y size of the paddle.
     * 
     * @param amount The amount by which to increase the variable y size of the 
     *     paddle,
     */
    public void increasepSizeY(int amount) {

        pSizeY += amount;

    }

    /**
     * This method is used to set the explosion flag.
     * 
     * @param start If to start or not.
     * @param spot Where to play the animation if started.
     */
    public void setExpFlag(boolean start, Point2D.Double spot) {
        expFlag = start;
        ExpFrame = 0;
        expSpot = spot;
    }
}