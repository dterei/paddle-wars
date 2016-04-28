//
//  Interface.java
//  A simple Java Swing applet
//
/*
   Version 1.4 - Added an extra class to allow for use of painting and drawing
                 of buttons in the one applet. Re-arranged program dramatically
                 again. Another consolidation will need to take place.

   Version 1.3 - Cleaned up the source code.

   Version 1.2 - Changed JPanel to a JDesktopPane. This was used to allow
                a more compatible housing for the JInternalFrame.

   Version 1.1 - Coded everything Swing format. Compatability issues faced
               switching from AWT.
               - Incorporated a menu.
   Version 1.0
    Did everything
    - Added buttons and layout
*/




package com.TeamJava.PaddleWars;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;
import javax.swing.event.*;
import java.net.*;


public  class Interface extends JApplet implements ActionListener,
                                                  InternalFrameListener {

    //The following statements define the new variables.
    public static PaddleWars paddleWars;

           public JFrame jFrame1;
           private int jFrameSizeX;
           private int jFrameSizeY;
           private static Container jFrame1content;
           public JDesktopPane backPanel;
           public JPanel jPanel1;
           public JPanel jPanel2;

    //These are the buttons that are used throughout the program.
           public JButton newGame;
           public JButton options;


           public JButton SinglePlayer;
           public JButton MultiPlayer;
           public JButton Practice;

    //Global variables for the menu up the top of the Interface.
           public JMenu menu;

           public JMenuBar menuBar;
           public JMenuItem menuNew;
           public JMenuItem menuOptions;

           public JMenuItem menuIcon;
           public JSeparator menuSep;

    //Global variables for the Radio Buttons in the options Panel.
           JRadioButton GreenP1;
           JRadioButton GreenP2;
           JRadioButton YellowP1;
           JRadioButton YellowP2;
           JRadioButton BlueP1;
           JRadioButton BlueP2;
           JRadioButton Fire;
           JRadioButton Ice;
           JRadioButton Slime;

    //Two Internal Frames used in the program.
           JInternalFrame optionsFrame;
           JInternalFrame newFrame;

    //This tab allows switching between Paddle and Ball.
           JTabbedPane OptTab;

    //Labels for the Options Internal Frame.
           JLabel banner;
           JLabel Ball;
           JLabel fireball;
           JLabel iceball;
           JLabel slimeball;
           JLabel Paddlegreen;
           JLabel Paddleyellow;
           JLabel Paddleblue;

    //A variation of integers that allow dynamic changes in the size of windows.
           int desktopWidth;
           int desktopHeight;
           int optFrameWidth = 640;
           int optFrameHeight = 480;
           int newFrameWidth = 610;
           int newFrameHeight = 150;

    //Set of integers that are used to determine ball and paddle properties
           int[] paddles = new int[2];
           int balls;

    //Used to signal which internal frame is used.
           static final String SHOW = "show";
           static final String NEW = "new";

    //URL statements used to bypass security errors encountered when making new
    // ImageIcon's
           URL bannerURL;
           URL fireurl;
           URL iceurl;
           URL slimeurl;
           URL yellowpaddleurl;
           URL greenpaddleurl;
           URL bluepaddleurl;


    //Sounds
    private AudioClip[] gameSounds = new AudioClip[5];

    //Images
    private Image backGround;
    private Image[] BallImages = new Image[12];
    private Image[][] lightningBall = new Image[2][4];
    private Image[][] PaddleImage = new Image[2][4];
    private Image[] explosion = new Image[12];



    //The following class is the features of the program
    //that are initiliased when the program starts.
    public void init() {

        //Get applet Width and Hieght
        desktopWidth = size().width;
        desktopHeight = size().height;

        loadImageIcon();
        Menu();

        //Defines new objects here.
        jFrame1 = new JFrame();
        jFrame1content = jFrame1.getContentPane();
        jFrame1.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                jFrame1Closed();
            }
        });
        //Where the items on the main screen are initialised
          banner = new JLabel(new ImageIcon(bannerURL));
          newGame = new JButton("New Game");
          options = new JButton("Options");


        //Settings for the jFrame which is where the main game is displayed
          jFrameSizeX = 850;
          jFrameSizeY = 550;
          jFrame1.setBounds(0,75,jFrameSizeX,jFrameSizeY);
          jFrame1.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);


        //Following properties define the buttons and JLabel on the main screen
          newGame.setBounds((desktopWidth/2-50),(desktopHeight/2-50),100,30);
          newGame.setActionCommand(NEW);
          options.setBounds((desktopWidth/2-50),(desktopHeight/2),100,30);
          options.setActionCommand(SHOW);

          banner.setBounds((desktopWidth/2-212),0,425,146);

        //Everything here is added to the content pane
          getContentPane().add(newGame);
          getContentPane().add(options);

          getContentPane().add(banner);
          getContentPane().setBackground(Color.black);
          getContentPane().setLayout(null);

        //adds Action Listeners to the following items.
          newGame.addActionListener(this);

          options.addActionListener(this);

    }


    public void loadSounds(){

        try {
            gameSounds[0] = getAudioClip(getCodeBase(), "Resources/Sounds/rub_bounce.wav");
            gameSounds[1] = getAudioClip(getCodeBase(), "Resources/Sounds/bonus.wav");
            gameSounds[2] = getAudioClip(getCodeBase(), "Resources/Sounds/explode-1.wav");
            gameSounds[3] = getAudioClip(getCodeBase(), "Resources/Sounds/metal_bounce.wav");
            gameSounds[4] = getAudioClip(getCodeBase(), "Resources/Sounds/explode-0.wav");
        } catch (Exception e) {
            System.out.println("Error Loading Sounds: "+e);
            System.out.println("No Sounds shall be availible");
        }

    }

    public void loadImageIcon() {
        try {
            bannerURL = new URL(getDocumentBase(), "Resources/Pics/Banner.jpg");
            fireurl = new URL(getDocumentBase(), "Resources/Pics/FireBall.png");
            iceurl = new URL(getDocumentBase(), "Resources/Pics/IceBall.png");
            slimeurl = new URL(getDocumentBase(), "Resources/Pics/SlimeBall.png");
            yellowpaddleurl = new URL(getDocumentBase(), "Resources/Pics/Paddles/paddle-2-1.gif");
            greenpaddleurl = new URL(getDocumentBase(), "Resources/Pics/Paddles/paddle-0-1.gif");
            bluepaddleurl = new URL(getDocumentBase(), "Resources/Pics/Paddles/paddle-1-1.gif");
        } catch (MalformedURLException e) {
            System.err.println("Malformed Exception caught: "+ e);
        }
    }

    public void loadImages() {
        try {
            backGround = getImage(getCodeBase(), "Resources/Pics/backGround.gif");
            for (int i = 0; i<12; i++) {
                explosion[i] = getImage(getCodeBase(), "Resources/Pics/Explosion/Explosion-"+(i+1)+".gif");
                prepareImage(explosion[i],this);
            }
            for (int i = 0; i<12; i++) {
                BallImages[i] = getImage(getCodeBase(),"Resources/Pics/Balls/Ball-"+balls+"-"+(i+1)+".gif");
                prepareImage(BallImages[i],this);
            }
            for (int i = 0; i<4; i++) {
                lightningBall[0][i] = getImage(getCodeBase(),"Resources/Pics/lightningBall/lightning-0-"+(i+1)+".gif");
                lightningBall[1][i] = getImage(getCodeBase(),"Resources/Pics/lightningBall/lightning-1-"+(i+1)+".gif");
                prepareImage(lightningBall[0][i],this);
                prepareImage(lightningBall[1][i],this);
            }
            for (int i = 0; i<4; i++) {
                PaddleImage[0][i] = getImage(getCodeBase(),"Resources/Pics/Paddles/paddle-"+paddles[0]+"-"+(i+1)+".gif");
                PaddleImage[1][i] = getImage(getCodeBase(),"Resources/Pics/Paddles/paddle-"+paddles[1]+"-"+(i+1)+".gif");
                prepareImage(PaddleImage[0][i],this);
                prepareImage(PaddleImage[1][i],this);
            }
        } catch (Exception e) {
            System.out.println("Error Loading Images: "+e);
            System.out.println("The Game Will Not Function With No Images");
            System.out.println("Game Stopped:Please Reload It");
        }
    }


        public void newWindow() {
               newFrame = new JInternalFrame("Choose New Game type...",
                                            false,  //resizable
                                            true,  //closable
                                            false,  //maximizable
                                            false); //iconifiable
           //Buttons for the new Internal Frame created here.
               SinglePlayer = new JButton("Single Player");
               MultiPlayer = new JButton("Multi-Player");
               Practice = new JButton("Practice");

           //Boundaries for the buttons
               SinglePlayer.setBounds((desktopWidth/2-330),50,150,30);
               MultiPlayer.setBounds((desktopWidth/2-75),50,150,30);
               Practice.setBounds((desktopWidth/2+180),50,150,30);

               newFrame.setDefaultCloseOperation(
               WindowConstants.DISPOSE_ON_CLOSE);
               newFrame.getContentPane().setLayout(null);
               newFrame.setBackground(Color.black);
               newFrame.setBounds(0,
                            desktopHeight/2 - newFrameHeight/2,
                            desktopWidth,
                            newFrameHeight);
           //Add the buttons to the frame
               newFrame.getContentPane().add(SinglePlayer);
               newFrame.getContentPane().add(MultiPlayer);
               newFrame.getContentPane().add(Practice);
           //Action Listeners to listen to what the buttons do.
               SinglePlayer.addActionListener(this);
               MultiPlayer.addActionListener(this);
               Practice.addActionListener(this);

        }

        public void optionsWindow() {
          //Definition of all variables for the options tab.
               fireball = new JLabel(new ImageIcon(fireurl));
               iceball = new JLabel(new ImageIcon(iceurl));
               slimeball = new JLabel(new ImageIcon(slimeurl));
               Paddlegreen = new JLabel(new ImageIcon(greenpaddleurl));
               Paddleyellow = new JLabel(new ImageIcon(yellowpaddleurl));
               Paddleblue = new JLabel(new ImageIcon(bluepaddleurl));
               OptTab = new JTabbedPane();
               Ball = new JLabel("Ball skin");
               Fire = new JRadioButton("Fire Ball");
               Ice = new JRadioButton("Ice Ball");
               Slime = new JRadioButton("Slime Ball");
               GreenP1 = new JRadioButton("Green Paddle: Player 1");
               GreenP2 = new JRadioButton("Green Paddle: Player 2");
               BlueP1 = new JRadioButton("Blue Paddle: Player 1");
               BlueP2 = new JRadioButton("Blue Paddle: Player 2");
               YellowP1 = new JRadioButton("Yellow Paddle: Player 1");
               YellowP2 = new JRadioButton("Yellow Paddle: Player 2");
               jPanel2 = new JPanel();
               jPanel1 = new JPanel();
               optionsFrame = new JInternalFrame("Options",
                                              true,  //resizable
                                              true,  //closable
                                              true,  //maximizable
                                              true); //iconifiable

        //The next statement is necessary to work around bug 4138031.
        //Errors are created when hiding on close.
               optionsFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
               optionsFrame.setSize(300, 100);
               optionsFrame.setBackground(Color.black);
               optionsFrame.setBounds(desktopWidth/2 - optFrameWidth/2,
                                      desktopHeight/2 - optFrameHeight/2,
                                      optFrameWidth,
                                      optFrameHeight);


               OptTab.addTab("Paddle", jPanel1);
               OptTab.addTab("Ball", jPanel2);
               OptTab.setBackground(Color.black);
               OptTab.setForeground(Color.white);
               OptTab.setBounds(-2,
                               0,
                               optFrameWidth,
                               optFrameHeight);
        //Code for the Ball's in the Ball Tab.
               Ball.setBounds(10,10,100,10);
               Ball.setForeground(Color.white);
               Fire.setBounds(60,250,100,30);
               Fire.setBackground(Color.black);
               Fire.setForeground(Color.white);
               fireball.setBounds(10,50,175,175);
               fireball.setBackground(Color.black);
               iceball.setBounds(210,50,175,175);
               iceball.setBackground(Color.black);
               Ice.setBounds(260,250,100,30);
               Ice.setBackground(Color.black);
               Ice.setForeground(Color.white);

               slimeball.setBounds(410,50,175,175);
               slimeball.setBackground(Color.black);
               Slime.setBounds(460,250,100,30);
               Slime.setBackground(Color.black);
               Slime.setForeground(Color.white);

        //Code for the Paddle's in the Paddle Tab.
               Paddlegreen.setBounds(100,25,40,200);
               Paddleblue.setBounds(300,25,40,200);
               Paddleyellow.setBounds(500,25,40,200);

        //Placement of the JLabel's and the colours of them.
               GreenP1.setBounds(40,250,200,25);
               GreenP1.setBackground(Color.black);
               GreenP1.setForeground(Color.white);
               GreenP2.setBounds(40,270,200,25);
               GreenP2.setBackground(Color.black);
               GreenP2.setForeground(Color.white);
               BlueP1.setBounds(240,250,200,25);
               BlueP1.setBackground(Color.black);
               BlueP1.setForeground(Color.white);
               BlueP2.setBounds(240,270,200,25);
               BlueP2.setBackground(Color.black);
               BlueP2.setForeground(Color.white);
               YellowP1.setBounds(440,250,200,25);
               YellowP1.setBackground(Color.black);
               YellowP1.setForeground(Color.white);
               YellowP2.setBounds(440,270,200,25);
               YellowP2.setBackground(Color.black);
               YellowP2.setForeground(Color.white);

    //Button groups to ensure that only one can be selected.
        ButtonGroup paddlegroup1 = new ButtonGroup();
                paddlegroup1.add(GreenP1);
                paddlegroup1.add(BlueP1);
                paddlegroup1.add(YellowP1);


        ButtonGroup paddlegroup2 = new ButtonGroup();
                paddlegroup2.add(GreenP2);
                paddlegroup2.add(YellowP2);
                paddlegroup2.add(BlueP2);

        ButtonGroup ballgroup = new ButtonGroup();
                ballgroup.add(Fire);
                ballgroup.add(Ice);
                ballgroup.add(Slime);

    //Adding of items to the jPanel1 and jPanel2
        jPanel1.setLayout(null);
        jPanel1.setBackground(Color.black);
        jPanel1.add(Paddlegreen);
        jPanel1.add(Paddleyellow);
        jPanel1.add(Paddleblue);
        jPanel1.add(GreenP1);
        jPanel1.add(YellowP1);
        jPanel1.add(GreenP2);
        jPanel1.add(YellowP2);
        jPanel1.add(BlueP1);
        jPanel1.add(BlueP2);

        jPanel2.setLayout(null);
        jPanel2.setBackground(Color.black);
        jPanel2.add(fireball);
        jPanel2.add(iceball);
        jPanel2.add(slimeball);

        jPanel2.add(Fire);
        jPanel2.add(Ice);
        jPanel2.add(Slime);


        //optionsFrame.getContentPane().add(Ice);
        optionsFrame.getContentPane().setLayout(null);
        optionsFrame.getContentPane().add(OptTab);

            if(paddles[0] == 0) {
                GreenP1.setSelected(true);
            }

            if(paddles[0] == 1) {
                BlueP1.setSelected(true);
            }

            if(paddles[0] == 2) {
                YellowP1.setSelected(true);
            }

            if(paddles[1] == 0) {
                GreenP2.setSelected(true);
            }

            if(paddles[1] == 1) {
                BlueP2.setSelected(true);
            }

            if(paddles[1] == 2) {
                YellowP2.setSelected(true);
            }

            if(balls == 0) {
                Fire.setSelected(true);
            }

            if(balls == 1) {
                Ice.setSelected(true);
            }

            if(balls == 2) {
                Slime.setSelected(true);
            }
        }







    public void Menu() {
        menuBar = new JMenuBar();
        menu = new JMenu("A Menu");

        menuNew = new JMenuItem("New", KeyEvent.VK_N);
        menuNew.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        menuNew.setActionCommand(NEW);
        menu.setMnemonic(KeyEvent.VK_M);
        menuOptions = new JMenuItem("Options", KeyEvent.VK_O);
        menuOptions.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        menuOptions.setActionCommand(SHOW);
        menuSep = new JSeparator();

        menuBar.add(menu);

        menu.add(menuNew);
        menu.add(menuOptions);
        menu.add(menuSep);

        setJMenuBar(menuBar);
        menuNew.addActionListener(this);
        menuOptions.addActionListener(this);


    }


    public void paddleWarsSetup(int gameMode) {

           //get rid of everything in case its somehow open
            jFrame1Closed();

            //Start new slate in Single Player Mode
            if (gameMode == 2) {
                paddles[1] = 3;
            }
            loadImages();
            loadSounds();
            paddleWars = new PaddleWars((int)(jFrameSizeX*0.99),(int)(jFrameSizeY*0.89),
                                    gameSounds,backGround, PaddleImage,BallImages,
                                    lightningBall, explosion, gameMode);
            jFrame1content.add(paddleWars);
            jFrame1.setVisible(true);

            //Request keyboard focus for paddleWars & see if it gets it or not
            boolean getFocus = paddleWars.requestFocusInWindow();
            //test if focus wasnt recieved
            if (getFocus == false) {
                System.out.println("Error: Focus Not recieved");
                System.out.println("Window Closed");
                jFrame1.setVisible(false);
                //get rid of everything
                jFrame1Closed();
            } else {
                //Start the game
                paddleWars.start();
            }

    }


    public void actionPerformed(ActionEvent event) {

        if(event.getSource() == SinglePlayer){

            paddleWarsSetup(0);

        }

        if(event.getSource() == MultiPlayer){

            paddleWarsSetup(1);

        }

        if(event.getSource() == Practice){

            paddleWarsSetup(2);

        }



        if (event.getActionCommand().equals(SHOW)) {
           if (optionsFrame == null) {
                optionsWindow();
                optionsFrame.addInternalFrameListener(this);
                getContentPane().add(optionsFrame);
                //optionsFrame.setLocation(
                    //desktopWidth/2 - optionsFrame.getWidth()/2,
                    //desktopHeight/2 - optionsFrame.getHeight());
                optionsFrame.setVisible(true);  //necessary as of 1.3
            }
        }
        if (event.getActionCommand().equals(NEW)) {
           if (newFrame == null) {
                newWindow();
                newFrame.addInternalFrameListener(this);
                getContentPane().add(newFrame);
                //optionsFrame.setLocation(
                    //desktopWidth/2 - optionsFrame.getWidth()/2,
                    //desktopHeight/2 - optionsFrame.getHeight());
                newFrame.setVisible(true);  //necessary as of 1.3
            }
        }
    }


    public void internalFrameOpened(InternalFrameEvent e) {
        //XXX: We don't seem to get any of these.
        //displayMessage("Internal frame opened", e);
            newGame.setVisible(false);
            options.setVisible(false);

            banner.setVisible(false);


    }

    public void internalFrameActivated(InternalFrameEvent e) {

    }

    public void internalFrameDeactivated(InternalFrameEvent e) {

    }

    public void internalFrameIconified(InternalFrameEvent e) {

    }

    public void internalFrameDeiconified(InternalFrameEvent e) {

    }

    public void internalFrameClosed(InternalFrameEvent e) {
           optionsFrame = null;
           newFrame = null;
           System.out.println("Frame Closed");

    }

    public void internalFrameClosing(InternalFrameEvent e)   {
        try {
            if(GreenP1.isSelected() == true) {
                System.out.println("GreenP1 is selected");
                paddles[0] = 0;
                System.out.println("Paddle[0] = "+ paddles[0]);
            } else if(YellowP1.isSelected() == true) {
                System.out.println("YellowP1 is selected");
                paddles[0] = 2;
                System.out.println("Paddle[0] = "+ paddles[0]);
            } else if(BlueP1.isSelected() == true) {
                System.out.println("BlueP1 is selected");
                paddles[0] = 1;
                System.out.println("Paddle[0] = "+ paddles[0]);
            }

            if(GreenP2.isSelected() == true) {
                System.out.println("GreenP2 is selected");
                paddles[1] = 0;
                System.out.println("Paddle[1] = "+ paddles[1]);
            } else if(YellowP2.isSelected() == true) {
                System.out.println("YellowP2 is selected");
                paddles[1] = 2;
                System.out.println("Paddle[1] = "+ paddles[1]);
            } else if(BlueP2.isSelected() == true) {
                System.out.println("BlueP2 is selected");
                paddles[1] = 1;
                System.out.println("Paddle[1] = "+ paddles[1]);
            }

            if(Fire.isSelected() == true) {
                System.out.println("Fire is selected");
                balls = 0;
            } else if(Ice.isSelected() == true) {
                System.out.println("Ice is selected");
                balls = 1;
            } else if(Slime.isSelected() == true) {
                System.out.println("Slime is selected");
                balls = 2;
            }
        } catch (NullPointerException exc) {
            System.err.println("Caught NullPointerException: " +
                                    exc.getMessage());
        } catch (ArrayIndexOutOfBoundsException exc) {
            System.err.println("Caught ArrayIndexOutOfBoundsException: " +
                                    exc.getMessage());
        }

            Buttons();
    }

    public void Buttons(){
            newGame.setVisible(true);
            options.setVisible(true);

            banner.setVisible(true);
    }


    public void jFrame1Closed() {

           //get rid of everything in jFrame1
           try {
               jFrame1content.removeAll();
               jFrame1.dispose();
               paddleWars.exitGame();
               paddleWars = null;
            } catch (NullPointerException e) {
                System.err.println("NullPointerException Caught: "
                                        + e.getMessage());
            }

    }




  /** Tell system to use native look and feel, as in previous
   *  releases. Metal (Java) LAF is the default otherwise.
   */

  public static void setNativeLookAndFeel() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch(Exception e) {
      System.out.println("Error setting native LAF: " + e);
    }
  }

  public static void setJavaLookAndFeel() {
    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch(Exception e) {
      System.out.println("Error setting Java LAF: " + e);
    }
  }

  public static void setMotifLookAndFeel() {
    try {
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
    } catch(Exception e) {
      System.out.println("Error setting Motif LAF: " + e);
    }
  }

  public static void setMetalBorders() {
    try {
      UIManager.setLookAndFeel("UIManager.com.sun.javax.swing.plaf.basic.BasicInternalFrameUI");
    } catch(Exception e) {
      System.out.println("Error setting Metal LAF: " + e);
    }
  }


  class MyAdapter extends KeyAdapter {

             /** Handle the key typed event from the text field. */
             public void keyTyped(KeyEvent e) {

                    System.out.println("KEY");

             }

             /** Handle the key pressed event from the text field. */
             public void keyPressed(KeyEvent e) {

                    System.out.println("KEY");

             }

             /** Handle the key released event from the text field. */
             public void keyReleased(KeyEvent e) {

                    System.out.println("KEY");

             }

       }

}