// Image Viewer
// Frans Coenen
// Dept. Comp. Sci., University of Liverpool
// Wednesday 31 January 20001


import java.awt.*;
import java.awt.event.*;
import java.applet.*;


public class imageApplet extends Applet {

    /* ------ FIELDS ------ */
    Image testImage;

    /* ------ METHODS ------ */

    /* INIT: Override init() */

    public void init() {
        testImage = getImage(getDocumentBase(),"imagey.gif");
    }

    /* PAINT */

    public void paint(Graphics g) {
        g.drawImage(testImage,0,0,243,54,this);
    }

}