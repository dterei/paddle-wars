package com.TeamJava.PaddleWars;

//Java Class Imports

import java.awt.*;
import java.awt.geom.*;
import java.applet.AudioClip;
import javax.swing.*;

//End of Java Class imports

/** 
 * This class creates and manages various balls. There are three balls which can be created from this class, a game ball, a bonus ball, and a missile ball.
 * 
 * @since 20/8/03
 * @author David Terei
 * @version 2.6
 */
public class Ball extends JPanel {

    /** Stores the center location of the ball.
     */
    private Point2D.Double center = new Point2D.Double();

    /** Stores the radius of the ball.
     */
    private int r;

    /** Stores the x-movmement speed of the ball.
     */
    private int xM = -2;

    /** Stores the y-movmement speed of the ball.
     */
    private int yM = 0;

    /** Stores the variable speed of the ball. Ball Speeed * xM = the actual x speed of the ball.
     */
    private int Speed = 2;

    /** Stores the variable size of the ball. BallSize * r = the actual ball size.
     */
    private int Size = 2;

    /** Stores what type of ball this is, gameBall, Bonus Ball, or Missile.
     */
    private int BallType;

    /** Stores how many times a bonus ball can hit a wall before it dissapears.
     */
    private int bounces = 6;

    /** Stores how many times a missile can hit a wall before it explodes.
     */
    private int MissileExploded = 1;

    /** Stores how long the ball should pause before moving again each time it starts up.
     */
    private int Delay = 75;

    ///** Stores the ball's image. Not relevant with bonus balls.
    // */
    //private Image[] Ball;
    /** Stores where abouts in the frame delay the class currently is.
     */
    private int cycle = 0;

    /** Stores which frame the ball animation is at.
     */
    private int frame = 0;

    /** Stores the colour of the ball, only relevant with bonus balls.
     */
    private Color[] bColor;

    /** Stores what bonus type a bonus ball is.
     */
    private int BonusType;

    /** The audio clip to play when the ball hits a wall.
     */
    private AudioClip WallHit;

    /** Stores the width of the area the ball is to be within.
     */
    private int aWidth;

    /** Stores the height of the area the ball is to be within.
     */
    private int aHeight;

    /** Stores Which element in anarray the ball is. Only relevant for missiles, to be able to reset the appropriate one.
     * 
     * @see com.TeamJava.PaddleWars.PaddleWars
     */
    private int arrayNumber;

    /** Which player is the owner of the missile. Only relevant with missiles.
     */
    private int Player;

    /** This is the constructor used to create a new game ball.
     * 
     * @param ballX The starting x posistion of the ball.
     * @param ballY The starting y posistion of the ball.
     * @param ballR The radius of the ball.
     * @param screenX The x size of the area in which the ball is  
     *     to operate.
     * @param screenY The y size of the area in which the ball is  
     *     to operate.
     * @param wallhit The adudio clip to play when the ball hits a 
     *      wall.
     */
    public Ball(int ballX, int ballY, int ballR, int screenX, int screenY,
            AudioClip wallhit) {

        center.x = ballX;
        center.y = ballY;
        r = ballR;

        BallType = 0;

        aWidth = screenX;
        aHeight = screenY;

        WallHit = wallhit;

    }

    /** This is the constructor used to create a new bonus ball.
     * 
     * @param ballX The starting x posistion of the ball.
     * @param ballY The starting y posistion of the ball.
     * @param ballR The radius of the ball.
     * @param color The images the animation method will use to  
     *     display the ball.
     * @param bonusType The bonus type of the ball.
     * @param screenX The x size of the area in which the ball is  
     *     to operate.
     * @param screenY The y size of the area in which the ball is  
     *     to operate.
     * @param wallhit The adudio clip to play when the ball hits a 
     *      wall.
     */
    public Ball(int ballX, int ballY, int ballR, Color[] color, int bonusType,
            int screenX, int screenY, AudioClip wallhit) {

        center.x = ballX;
        center.y = ballY;
        r = ballR;

        BallType = 1;

        aWidth = screenX;
        aHeight = screenY;

        bColor = new Color[color.length];
        bColor = color;

        BonusType = bonusType;

        WallHit = wallhit;

    }

    /** This is the constructor used for creating new missiles., must use an array of missles to create.
     * 
     * @param ballX The starting x posistion of the ball.
     * @param ballY The starting y posistion of the ball.
     * @param ballR The radius of the ball.
     * @param screenX The x size of the area in which the ball is  
     *     to operate.
     * @param screenY The y size of the area in which the ball is  
     *     to operate.
     * @param wallhit The adudio clip to play when the ball hits a 
     *      wall.
     * @param ballNumber Which element in an array the balls is.
     * @param player AStores which player is the owner of the  
     *     missile.
     */
    public Ball(int ballX, int ballY, int ballR, int screenX, int screenY,
            AudioClip wallhit, int ballNumber, int player) {

        center.x = ballX;
        center.y = ballY;
        r = ballR;

        BallType = 2;

        aWidth = screenX;
        aHeight = screenY;

        WallHit = wallhit;

        arrayNumber = ballNumber;
        Player = player;

    }

    /** Standard Paint method, cast the standard graphics object into a 2D graphics object.
     * 
     * @param g Creates a standard graphics object to draw to.
     */
    public void paint(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        if (BallType != 1) {
            g2.setColor(java.awt.Color.GREEN);
            g2.fillOval((int) (center.x - r + 1), (int) (center.y - r + 1), r
                    * 2 * Size - 1, r * 2 * Size - 1);
            //g2.drawImage(Ball[frame],(int)(center.x-(r*Size)),(int)(center.y-(r*Size)),r*2*Size,r*2*Size,this);
            //animateBall();
        } else {
            g2.setColor(bColor[BonusType]);
            g2.fillOval((int) (center.x - (r * Size)),
                    (int) (center.y - (r * Size)), r * 2 * Size, r * 2 * Size);
        }

    }

    /** Reutrns the current center of the ball.
     * 
     * @return The balls center
     */
    public Point2D.Double getBallCenter() {

        Point2D.Double RealCenter = new Point2D.Double();

        RealCenter.x = center.x;
        RealCenter.y = center.y;

        return RealCenter;

    }

    /** Returns the radius of the ball.
     * 
     * @return The ball's radius.
     */
    public int getBallRadius() {

        return r;

    }

    /** Returns the variable size of the ball.
     * 
     * @return The variable Size of the ball.
     */
    public int getBallSize() {

        return Size;

    }

    /** Returns the type of ball it is.
     * 
     * @return Type of Ball it is.
     */
    public int getBallType() {

        return BallType;

    }

    /** Returns the bonus type of the ball, if the ball is a bonus ball.
     * 
     * @return Bonus Type of the ball.
     */
    public int getBonusType() {

        return BonusType;

    }

    /** Returns the variable speed of the ball, variable speed * xm = actual speed.
     * 
     * @return Variable speed of the ball.
     */
    public int getSpeed() {

        return Speed;

    }

    /** Returns the current x velocity or y velocity of the ball, depending on the way it is accessed.
     * 
     * @param whichOne Which velocity to return, 0 = x, 1 = y.
     * @return Current x or y velocity of the ball.
     */
    public int getVelocity(int whichOne) {

        if (whichOne == 0) {
            return xM;
        } else {
            return yM;
        }

    }

    ///** This method animates the ball, by cycling through its images, not relevant with bonus balls.
    // */
    /*private void animateBall() {

     cycle++;
     if (cycle % 3 == 0) {
     cycle = 0;
     if (frame == (Ball.length-1)) { frame = -1;}
     frame++;
     }

     }*/

    /** 
     * This method moves the ball.
     */
    private void moveBall() { //moves the ball

        center.x += (Speed * xM);
        center.y += (Speed * (yM / 2));

        changeBallY();

        if (BallType == 0) {
            restartRound();
        } else {
            changeBallX();
        }
    }

    /** 
     * This method changes the y movemnt of the ball, if it hits a top or bottom wall.
     */
    private void changeBallY() {

        if (center.y <= (r * Size)) {
            center.y = (r * Size) + 1;
            yM = -yM;
            WallHit.play();
        }

        if (center.y + (r * Size) >= aHeight) {
            center.y = (aHeight - (r * Size) - 1);
            yM = -yM;
            WallHit.play();
        }

    }

    /** This method changes the x movemnt of the ball, if it hits a side wall.
     */
    private void changeBallX() {

        if (center.x <= 0 && center.x > -50) {
            center.x = 1;
            xM = -xM;
            bounces--;
            MissileExploded--;
            WallHit.play();
            if (BallType == 1 && bounces == 0) {
                Interface.paddleWars.setBonusRunning(false);
            }
            if (BallType == 2 && MissileExploded == 0) {
                resetMissile();
            }
        }

        if (center.x + (r * 2 * Size) >= aWidth && center.x < aWidth + 100) {
            center.x = (aWidth - (r * 2 * Size) - 1);
            xM = -xM;
            bounces--;
            MissileExploded--;
            WallHit.play();
            if (BallType == 1 && bounces == 0) {
                Interface.paddleWars.setBonusRunning(false);
            }
            if (BallType == 2 && MissileExploded == 0) {
                resetMissile();
            }
        }

    }

    /** This method calls on paddleWars to reset the appropriate missile.
     */
    public void resetMissile() {

        Interface.paddleWars.resetMissile(Player, arrayNumber);

    }

    /** This method sets the x and y posistion of the ball.
     * 
     * @param x The x posistion.
     * @param y The y posistion.
     */
    public void setBall(int x, int y) {

        center.x = x;
        center.y = y;

    }

    /** This method alters the x velocity of the ball by multiplying the current x velocity by x.
     * 
     * @param x The value for which to multiply the current x velocity by.
     */
    public void setBallVelocityX(int x) {

        xM = xM * x;

    }

    /** This method sets the y velocity of the ball.
     * 
     * @param y The new y velocity to set for the ball.
     */
    public void setBallVelocityY(int y) {

        yM = y;

    }

    /** This method increases the size of the ball, by increasing its variable size.
     * 
     * @param amount The amount by which to increase it.
     */
    public void increaseSize(int amount) {

        Size += amount;

    }

    /** This method increases the variable speed.
     * 
     * @param amount The amount by which to increase it.
     */
    public void increaseSpeed(int amount) {

        Speed += amount;

    }

    /** This method restards the round, used for game balls if a player wins a point.
     */
    private void restartRound() {

        if (center.x + (r * 2 * Size) <= 0) {
            Interface.paddleWars.setScore(-1, 1);
            center.x = aWidth / 2;
            center.y = aHeight / 2;
            xM = 2;
            yM = 0;
            Delay = 75;

            int WhichWay;
            if (xM > 0) {
                WhichWay = 0;
            } else {
                WhichWay = 1;
            }
            Interface.paddleWars.setpredictTime(WhichWay, 5);
        }

        if (center.x + (r * 2 * Size) >= aWidth) {
            Interface.paddleWars.setScore(1, -1);
            center.x = aWidth / 2;
            center.y = aHeight / 2;
            xM = 2;
            yM = 0;
            Delay = 75;

            int WhichWay;
            if (xM > 0) {
                WhichWay = 0;
            } else {
                WhichWay = 1;
            }
            Interface.paddleWars.setpredictTime(WhichWay, 5);
        }

    }

    /** This method is used by other classes to actually move and run the ball.
     */
    public void runner() {

        if (Delay > 0 && BallType != 2) {
            Delay--;
        } else {
            moveBall();
        }

    }

}