package com.TeamJava.PaddleWars;




/**
 * Handles all of the bonuses availible. The Intercept class acesses it if a bonus ball intercects a paddle. This class then places limits on paddle and ball properties, and modifies them according to the bonus generated originally in the paddleWars class.
 *
 * @since 1/9/03
 * @author David Terei
 * @see intercept
 * @see PaddleWars
 * @version 2
 */
public class bonusManager {


    /**
     * This Method is used by the intercept class to acess this class.
     * This method gets which paddle recieved the ball, and which ball that was,
     * passed into it. It then gets the balls bonus type, and determines which
     * method to run.
     *
     * @param paddle Which paddle the ball intercepted
     * @param ball Which ball in the Ball array, which is a bonus,
     *     intercepted the paddle
     */
    public void giveBonus(int paddle, int ball) {

           //get what the bonus just recieved was
           int Bonus = Interface.paddleWars.Ball[ball].getBonusType();

           /* Debugging Information
           System.out.println("Paddle: " + Paddle);
           System.out.println("Ball: " + Ball);
           System.out.println("Bonus: " + Bonus);
           System.out.println("................");
           */

           //compare the bonus with a multi way selection, to see what action to take.
           if (paddle == 0) {

              switch(Bonus) {

                //Paddle 1
                case 0: increasePaddleY(0); break;
                case 1: decreasePaddleY(0);break;
                case 2: increasePaddleX(0); break;
                case 3: decreasePaddleX(0); break;
                case 4: increaseBallSpeed(); break;
                case 5: decreaseBallSpeed(); break;
                case 6: increaseBallSize(); break;
                case 7: decreaseBallSize(); break;
                case 8: increaseScore(1,-1); break;
                case 9: enableSpecialKey(0); break;
                case 10: Interface.paddleWars.giveMissilePaddle(0); break;
                default: break;

              }

           } else {

              switch(Bonus) {

                //Paddle 2
                case 0: increasePaddleY(1); break;
                case 1: decreasePaddleY(1);break;
                case 2: increasePaddleX(1); break;
                case 3: decreasePaddleX(1); break;
                case 4: increaseBallSpeed(); break;
                case 5: decreaseBallSpeed(); break;
                case 6: increaseBallSize(); break;
                case 7: decreaseBallSize(); break;
                case 8: increaseScore(-1,1); break;
                case 9: enableSpecialKey(1); break;
                case 10: Interface.paddleWars.giveMissilePaddle(1); break;
                default: break;

              }

           }

    }


    /**
     * Test what the paddle's current y size is, and increases it or does nothing.
     * Depending on the starting size.
     *
     * @param paddle which paddle to act upon.
     */
    private void increasePaddleY(int paddle) {

           //Get the current variable y size of the paddle
           int PaddleSizeY = Interface.paddleWars.Paddle[paddle].getpSizeY();

           //test that it doesn't equal 3 first, because that is the max size limit.
           if (PaddleSizeY != 3) {
              //since it isn't at the max size, we then increase it.
              Interface.paddleWars.Paddle[paddle].increasepSizeY(1);
           }

    }


    /**
     * Test what the paddle's current y size is, and decreases it or does nothing.
     * Depending on the starting size.
     *
     * @param paddle which paddle to act upon.
     */
    private void decreasePaddleY(int paddle) {

           //test that it doesn't equal 1 first, because that is the min size limit.
           int PaddleSizeY = Interface.paddleWars.Paddle[paddle].getpSizeY();

           if (PaddleSizeY != 1) {
              //since it isn't at the min size, we then decrease it.
              Interface.paddleWars.Paddle[paddle].increasepSizeY(-1);
           }

    }


    /**
     * Test what the paddle's current x size is, and increase it or does nothing.
     * Depending on the starting size.
     *
     * @param paddle which paddle to act upon.
     */
    private void increasePaddleX(int paddle) {

           //test that it doesn't equal 3 first, because that is the max size limit.
           int PaddleSizeX = Interface.paddleWars.Paddle[paddle].getpSizeX();

           if (PaddleSizeX != 3) {
              //since it isn't at the max size, we then increase it.
              Interface.paddleWars.Paddle[paddle].increasepSizeX(1);
           }

    }


    /**
     * Test what the paddle's current x size is, and decreases it or does nothing.
     * Depending on the starting size.
     *
     * @param paddle which paddle to act upon.
     */
    private void decreasePaddleX(int paddle) {

           //test that it doesn't equal 1 first, because that is the min size limit.
           int PaddleSizeX = Interface.paddleWars.Paddle[paddle].getpSizeX();

           if (PaddleSizeX != 1) {
              //since it isn't at the min size, we then decrease it.
              Interface.paddleWars.Paddle[paddle].increasepSizeX(-1);
           }

    }


    /**
     * Test what the main game Balls current speed is, and increases it or does nothing.
     * Depending on the starting speed
     */
    private void increaseBallSpeed() {

           //test that it doesn't equal 3 first, because that is the max size limit.
           int BallSpeed = Interface.paddleWars.Ball[0].getSpeed();

           if (BallSpeed != 3) {
              //since it isn't at the max size, we then increase it.
              Interface.paddleWars.Ball[0].increaseSpeed(1);
           }

    }


    /**
     * Test what the main game Balls current speed is, and decreases it or does nothing.
     * Depending on the starting speed
     */
    private void decreaseBallSpeed() {

           //test that it doesn't equal 1 first, because that is the min size limit.
           int BallSpeed = Interface.paddleWars.Ball[0].getSpeed();

           if (BallSpeed != 1) {
              //since it isn't at the min size, we then decrease it.
              Interface.paddleWars.Ball[0].increaseSpeed(-1);
           }

    }


    /**
     * Test what the main game Balls current size is, and increases it or does nothing.
     * Depending on the starting size.
     */
    private void increaseBallSize() {

           //test that it doesn't equal 3 first, because that is the max size limit.
           int BallSize = Interface.paddleWars.Ball[0].getBallSize();

           if (BallSize != 3) {
              //since it isn't at the max size, we then increase it.
              Interface.paddleWars.Ball[0].increaseSize(1);
           }

    }


    /**
     * Test what the main game Balls current size is, and decreases it or does nothing.
     * Depending on the starting size.
     */
    private void decreaseBallSize() {

           //test that it doesn't equal 1 first, because that is the min size limit.
           int BallSize = Interface.paddleWars.Ball[0].getBallSize();

           if (BallSize != 1) {
              //since it isn't at the min size, we then decrease it.
              Interface.paddleWars.Ball[0].increaseSize(-1);
           }

    }


    /**
     * Add the paramaters passed in to paddle 1 & 2's score, respectivley.
     *
     * @param paddle1 How much to increase paddle 1's score
     * @param paddle2 How much to increase paddle 2's score
     */
    private void increaseScore(int paddle1, int paddle2) {

            //increase the score's.
            Interface.paddleWars.setScore(paddle1,paddle2);

    }


    /**
     * This method will test to see if the Paddle's Hyper key is enabled.
     * If it isnt, it will enable their Hyper movement key.
     * This is actually done by increasing the paddle's Hyper movement rate,
     * as the Hyper movement key is always availible, but defaultly set to
     * normal movement.
     *
     * @param paddle which paddle to enable.
     */
    private void enableSpecialKey(int paddle) {

            //Get if they have Hyper movement already or not.
            int gotKey = Interface.paddleWars.hyperKey(0,paddle,0);

            //Make sure they don't already have Hyper movement.
            if (gotKey != 1) {

              //Since they dont, set it that they now do.
              Interface.paddleWars.hyperKey(1,paddle,1);
              //Now double there Hyper movement rate, to actually 'give' it to them.
              Interface.paddleWars.setHyperMovement(1,paddle,200);

            }

    }


}