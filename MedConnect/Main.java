/*
Main:
    Initialized  either GUI from object

 */

import java.util.ArrayList;

public class Main
{

    /*--------------------------------------
    WORK TO DO


            CONTENT GUI: textfields and labels for main display
                         passing all values correctly
                         search bar(?)
                         logout(?)

            CODE CLEANING: add comments to everything
                           eliminate redundancy

            BUG TESTING:
                           test every user option
                           make video


     ---------------------------------------*/
    public static void main(String[] args){
        Connect c = new Connect();
        c.Connection();
        //ContentGUI cObj = new ContentGUI(0,new Connect(),"Callie");
        AccessGUI aObj = new AccessGUI(c);

    }
}
