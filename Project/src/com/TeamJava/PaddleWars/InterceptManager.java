package com.TeamJava.PaddleWars;

import java.awt.geom.*;
import java.applet.AudioClip;

/**
 * This Class monitors one specific ball and paddle, and test to see if they
 * intercept at any point in time. It accepts three different ball types, a game
 * Ball (0), a bonus Ball (1) and a missile ball (2) It will react differently
 * when an itersection occurs, depednding on the ball type.
 * 
 * @since 26/8/03
 * @author David Terei
 * @see PaddleWars
 * @see Ball
 * @see Paddle
 * @see BonusManager
 * @version 2.2
 */
public class InterceptManager {

    /**
     * Stores which paddle is being tested for an intercept with the ball being
     * tested.
     */
    private int WhichPaddle;

    /**
     * Holds which ball it is monitoring, in terms of its array posistion.
     * 
     * @see PaddleWars
     */
    private int WhichBall;

    /**
     * Holds the ball type. The three types are: Game Ball(0), Bonus Ball(1),
     * Missile Ball(2).
     * 
     * @see Ball
     */
    private int BallType;

    /**
     * Holds which paddle is the owner of the missile, if the ball type is a
     * missile ball.
     * 
     * @see Paddle
     */
    private int Firer;

    /**
     * paddle Top Left. The co-ordinates for the top left corner of the paddle
     * it is monitoring
     * 
     * @see Paddle
     */
    private Point2D.Double pTL = new Point2D.Double();

    /**
     * paddle Bottom Right. The co-ordinates for the bottom right corner of the
     * paddle it is monitoring
     * 
     * @see Paddle
     */
    private Point2D.Double pBR = new Point2D.Double();

    /**
     * The variable x and y size of the paddle it's monitoring.
     * 
     * @see Paddle
     */
    private Point2D.Double pSize = new Point2D.Double();

    /**
     * The fixed x size of the paddle it's monitoring.
     * 
     * @see Paddle
     */
    private int pSizeX;

    /**
     * The fixed y size of the paddle it's monitoring.
     * 
     * @see Paddle
     */
    private int pSizeY;

    /**
     * paddle Round Center Top. The co-ordinates for the center of the top round
     * part of the paddle it is monitoring
     * 
     * @see Paddle
     */
    private Point2D.Double pRCT = new Point2D.Double();

    /**
     * paddle Round Center Bottom. The co-ordinates for the center of the bottom
     * round part of the paddle it is monitoring
     * 
     * @see Paddle
     */
    private Point2D.Double pRCB = new Point2D.Double();

    /**
     * The co-ordinates for the center of the ball it's monitoring.
     * 
     * @see Ball
     */
    private Point2D.Double Ball = new Point2D.Double();

    /**
     * The radius of the ball it's monitoring.
     * 
     * @see Ball
     */
    private int BallRadius = 0;

    /**
     * The Variable Size of the ball it's monitoring.
     * 
     * @see Ball
     */
    private int BallSize = 0;

    /**
     * The delay time for when to begin checking for an interception again, just
     * after one. This is too avoid one intersection been mistaken for two.
     */
    private int Delay = -1;

    /**
     * Contains the sound file to play when a game ball hits a paddle.
     * 
     * @see Ball
     */
    private AudioClip ballHit;

    /**
     * Contains the sound file to play when a bonus ball hits a paddle.
     * 
     * @see Ball
     */
    private AudioClip bonusHit;

    /**
     * Contains the sound file to play when a missile hits a paddle.
     * 
     * @see Ball
     */
    private AudioClip missileHit;

    /**
     * The constructor for game balls, and bonus balls.
     * 
     * @param bally
     *            Which ball to monitor, in terms of its array posistion.
     * @param ballType
     *            The type of ball it's monitoring
     * @see Ball
     */
    public InterceptManager(int bally, int ballType) {

        WhichBall = bally;
        BallType = ballType;

    }

    /**
     * The constructor for missile balls to use.
     * 
     * @param firer
     *            The owner of the missile ball its monitoring
     * @param bally
     *            Which ball to monitor, in terms of its array posistion.
     * @param ballType
     *            The type of ball it's monitoring
     * @see Paddle
     * @see Ball
     */
    public InterceptManager(int firer, int bally, int ballType) {

        Firer = firer;
        WhichBall = bally;
        BallType = ballType;

    }

    /**
     * This method is used to determine if an intersection has taken place, and
     * calls the appropriate response, depending on the ball type.
     * 
     * @param ptl
     *            paddle Top Left.
     * @param psize
     *            pSize.
     * @param psizeY
     *            pSizeY.
     * @param psizeX
     *            pSizeX.
     * @param ball
     *            Ball.
     * @param bR
     *            BallRadius.
     * @param ballSize
     *            BallSize.
     * @param WhichPaddle
     *            Holds which paddle is being tested to the Ball.
     * @param paddleHit
     *            The audio clip to play when a ball hits the paddle.
     * @see Paddle
     * @see Ball
     */
    public void TestIntercept(Point2D.Double ptl, Point2D.Double psize,
            int psizeY, int psizeX, Point2D.Double ball, int bR, int ballSize,
            int WhichPaddle, AudioClip paddleHit) {

        pTL = ptl;
        pSize = psize;

        pSizeX = psizeX;
        pSizeY = psizeY;

        pBR.x = (pTL.x + (pSize.x * pSizeX));
        pBR.y = (pTL.y + (pSize.y * pSizeY));

        pRCT.x = (pTL.x + ((pSize.x * pSizeX) / 2));
        pRCT.y = (pTL.y + ((pSize.x * pSizeX) / 2));

        pRCB.x = (pBR.x - ((pSize.x * pSizeX) / 2));
        pRCB.y = (pBR.y - ((pSize.x * pSizeX) / 2));

        Ball = ball;

        BallRadius = bR;
        BallSize = ballSize;

        this.WhichPaddle = WhichPaddle;
        ballHit = paddleHit;

        if (Delay < 0) {

            if ((Ball.x + (BallRadius * BallSize)) >= pTL.x
                    && (Ball.x - (BallRadius * BallSize)) <= pBR.x) {

                if ((Ball.y + (BallRadius * BallSize)) >= pTL.y
                        && Ball.y < pRCT.y) {

                    double BallPaddleD = Math.sqrt(Math.pow(Ball.x - pRCT.x, 2)
                            + Math.pow(Ball.y - pRCT.y, 2));

                    double D = ((pSize.x * pSizeX) / 2)
                            + (BallRadius * BallSize);

                    if (BallPaddleD <= D) {

                        ballHit.play();

                        if (BallType == 0) {

                            TopRoundReflection();

                        } else if (BallType == 1) {

                            PaddleWars.bonManager.giveBonus(WhichPaddle,
                                    WhichBall);
                            Interface.paddleWars.setBonusRunning(false);

                        } else if (BallType == 2) {

                            Interface.paddleWars.resetMissile(Firer, WhichBall);
                            missileHit(WhichPaddle);
                            Delay = 10;

                        }

                    }

                } else if (Ball.y >= pRCT.y && Ball.y <= pRCB.y) {

                    ballHit.play();

                    if (BallType == 0) {

                        FlatReflection();

                    } else if (BallType == 1) {

                        PaddleWars.bonManager.giveBonus(WhichPaddle, WhichBall);
                        Interface.paddleWars.setBonusRunning(false);

                    } else if (BallType == 2) {

                        Interface.paddleWars.resetMissile(Firer, WhichBall);
                        missileHit(WhichPaddle);
                        Delay = 10;

                    }

                } else if (Ball.y > pRCB.y
                        && Ball.y <= (pBR.y + (BallRadius * BallSize))) {

                    double BallPaddleD = Math.sqrt(Math.pow(Ball.x - pRCB.x, 2)
                            + Math.pow(Ball.y - pRCB.y, 2));

                    double D = ((pSize.x * pSizeX) / 2)
                            + (BallRadius * BallSize);

                    if (BallPaddleD <= D) {

                        ballHit.play();

                        if (BallType == 0) {

                            BottomRoundReflection();

                        } else if (BallType == 1) {

                            PaddleWars.bonManager.giveBonus(WhichPaddle,
                                    WhichBall);
                            Interface.paddleWars.setBonusRunning(false);

                        } else if (BallType == 2) {

                            Interface.paddleWars.resetMissile(Firer, WhichBall);
                            missileHit(WhichPaddle);
                            Delay = 10;

                        }

                    }

                }

            }

        } else {

            Delay--;

        }

    }

    /**
     * This method determines the x and y movement values that the ball should
     * take after intersectiong the top round section of the monitored paddle.
     * It does this by determinging firstly the x value for where the ball
     * intersected the paddle, then finding the gradient of tangent to the
     * semi-ccircle, which is the top of the paddle, at that point. It then
     * finds the gradient of the line percendicular to the tangent, and reflects
     * the ball along that path.
     * 
     * @see Ball
     */
    private void TopRoundReflection() {

        Delay = 10;

        int Xint = getXIntercept((int) pRCT.x, (int) (pSize.x * pSizeX) / 2,
                BallRadius * BallSize, (int) Ball.x);

        double Tangent = Math.abs((getTanget(Xint, BallRadius * BallSize)));

        double Perpendicular = getPerpendicular(Tangent);

        int X = -1;

        if (Perpendicular < -7) {
            X = 1;
            Perpendicular = -6;
        } else if (Perpendicular < -5) {
            Perpendicular = -5;
        }

        PaddleWars.Ball[WhichBall].setBallVelocityY((int) Perpendicular);
        PaddleWars.Ball[WhichBall].setBallVelocityX(X);
        Interface.paddleWars.setpredictTime(WhichPaddle, 5);

    }

    /**
     * This method determines the x and y movement values that the ball should
     * take after intersectiong the flat section of the monitored paddle. It
     * does this by reversing the x velocity of the ball, and leaving the y
     * velocity the same.
     * 
     * @see Ball
     */
    private void FlatReflection() {

        PaddleWars.Ball[WhichBall].setBallVelocityX(-1);
        Delay = 10;
        Interface.paddleWars.setpredictTime(WhichPaddle, 5);

    }

    /**
     * This method determines the x and y movement values that the ball should
     * take after intersectiong the bottom round section of the monitored
     * paddle. It does this by determinging firstly the x value for where the
     * ball intersected the paddle, then finding the gradient of tangent to the
     * semi-ccircle, which is the top of the paddle, at that point. It then
     * finds the gradient of the line percendicular to the tangent, and reflects
     * the ball along that path.
     * 
     * @see Ball
     */
    private void BottomRoundReflection() {

        Delay = 10;

        int Xint = getXIntercept((int) pRCB.x, (int) (pSize.x * pSizeX) / 2,
                BallRadius * BallSize, (int) Ball.x);

        double Tangent = -(Math.abs((getTanget(Xint, BallRadius * BallSize))));

        double Perpendicular = getPerpendicular(Tangent);

        int X = -1;

        if (Perpendicular > 7) {
            X = 1;
            Perpendicular = 6;
        } else if (Perpendicular > 5) {
            Perpendicular = 5;
        }

        PaddleWars.Ball[WhichBall].setBallVelocityY((int) Perpendicular);
        PaddleWars.Ball[WhichBall].setBallVelocityX(X);
        Interface.paddleWars.setpredictTime(WhichPaddle, 5);

    }

    /**
     * This method takes the given values and uses them to internally divide the
     * line formed by the points given, in the ratio given. The y values are not
     * important to this fourmula, as it only finds the x answer.
     * 
     * @param Xa
     *            The x co-ordinate of the first point
     * @param K1
     *            The value of the ratio between the first point, and the point
     *            you wish to find
     * @param K2
     *            The value of the ratio between the second point, and the point
     *            you wish to find
     * @param Xb
     *            The x co-ordinate of the second point
     * @return Returns the x value that divides the line formed by the given
     *         points, in the given ratio.
     */
    private int getXIntercept(int Xa, int K1, int K2, int Xb) {

        int intercept = ((K1 * Xb) + (K2 * Xa)) / (K1 + K2) - Xa;

        return intercept;

    }

    /**
     * Finds the gradient of the tangent to the top half of a circle, at a x
     * value given, taking the circle's center to be at (0,0).
     * 
     * @param x
     *            The x co-ordinate at which you wish to find the gradient
     * @param r
     *            The radius of the circle
     * @return The gradient of the tangent.
     */
    private double getTanget(int x, int r) {

        double tangent = x / Math.sqrt(Math.pow(r, 2) - Math.pow(x, 2));

        return tangent;
    }

    /**
     * Finds the gradient which represents a line perpendicular to the line
     * formed by the gradient given.
     * 
     * @param M1
     *            The gradient of the line for which to find the perpendicualr
     *            gradient.
     * @return The gradient fo the perpendicular line
     */
    private double getPerpendicular(double M1) {

        double M2 = -1 / M1;

        return M2;

    }

    /**
     * This method controls what happens when a missile hits the paddle.
     * 
     * @param paddle
     *            The array value of the Paddle hit.
     * @see PaddleWars
     */
    private void missileHit(int paddle) {

        Interface.paddleWars.setStunned(paddle, 18);
        PaddleWars.Paddle[paddle].setExpFlag(true, Ball);

    }

}