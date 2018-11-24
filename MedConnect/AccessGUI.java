    /*
        AccessGUI:

                Initializing this class creates a GUI interface for logging into our main program

                This constructor holds all code regarding the structure of this GUI

                INPUT: Connect

                OUTPUT: GUI/void

                    SUCCESS:  A Connection to the database is established and secure
                              The content is displayed to the screen
                              The menu's display doctor and administrator names respectively
                                clicking on these names fills the username and password fields
                              Clicking the submit button closes the AccessGUI and opens the ContentGUI
                                Clicking the submit button with invalid credentials or no textbox values gives a popup error

                    FAILURE:  The Connection is not established
                              The content does not display / does not display correctly
                              clicking buttons does not result in action

     */
import javax.swing.*;
import darrylbu.util.*;
import java.awt.*;
import java.awt.Event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AccessGUI extends JFrame {

    private JLabel user, pass;
    private JPasswordField password;
    private JButton submit;
    private JTextField userT;
    private Panel submitPanel, usernamePanel, passwordPanel;
    private Container pane;
    private JMenuBar menuBar;
    private JMenu adminMenu, doctorMenu;
    private Connect c = new Connect();


    public AccessGUI(Connect con){
        this.c = con;

        //panel 1 holds submit button
            submitPanel = new Panel();
            //submitPanel.setLayout(new GridLayout(2,1));
            submitPanel.setBackground(new Color(66, 185, 244));
            submit = new JButton("Submit");
            submit.addActionListener(new ActionListener(){
                /*

                 actionPerformed:

                        This function handles the event launched when the 'Submit' button is pressed

                        This function also acts as an authorization check for the user's input into the username and password text fields

                        SUCCESS: the login values match those in the database
                                 the AccessGUI closes
                                 opens the ContentGUI

                        FAILURE: the login values do not match those in the database
                 */
                @Override
                public void actionPerformed(ActionEvent actionEvent) {


                        if(!c.Query("Select accessLevel from Admin Where adminID = '" + password.getText() + "' AND adminName = '" + userT.getText() + "';", false).isEmpty())  {
                            dispose();
                            ContentGUI cObj = new ContentGUI(1, c,userT.getText());
                        }
                        else if (!(c.Query("Select * from Doctor Where doctorID = '" + password.getText() + "' AND doctorName = '" + userT.getText() + "';", false).isEmpty())) {
                            dispose();
                            ContentGUI cObj = new ContentGUI(0, c, userT.getText());
                            //not administrator or doctor, bad login
                        } else {
                            JOptionPane.showMessageDialog(null, "Incorrect Login Information");
                        }
                }
            });
            submitPanel.add(new JLabel());
            submitPanel.add(submit);
            

            //panel 2 holds username input box and label
            usernamePanel = new Panel();
            usernamePanel.setBackground(new Color(66, 185, 244));
            user = new JLabel("username:");
            userT = new JTextField(10);
            usernamePanel.add(user);
            usernamePanel.add(userT);

            //panel 3 holds password input box and label
            passwordPanel = new Panel();
            passwordPanel.setBackground(new Color(66, 185, 244));
            pass = new JLabel("password:");
            password = new JPasswordField(10);
            passwordPanel.add(pass);
            passwordPanel.add(password);

            //menu
            menuBar = new JMenuBar();
            adminMenu = new JMenu("Admins");
            adminMenu.setName("Admins");
            addMenuItems(adminMenu);
            doctorMenu = new JMenu("Doctors");
            doctorMenu.setName("Doctors");

            addMenuItems(doctorMenu);
            MenuScroller.setScrollerFor(adminMenu);
            MenuScroller.setScrollerFor(doctorMenu);
            menuBar.add(adminMenu);
            menuBar.add(doctorMenu);
            setJMenuBar(menuBar);

            //initialize content pane
            pane = new Container();
            pane=getContentPane();
            pane.setLayout(new GridLayout(1,2) );

            //add panels to content pane
            pane.add(usernamePanel);
            pane.add(passwordPanel);
            pane.add(submitPanel);

            //configure content pane and display
            setSize(480, 120);
            setVisible(true);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);


    }



/*
   addMenuItems:

      This Method adds database names from Doctor and Admin tables to the passed menu's in AccessGUI

      This allows for entry to the program without knowing database values beforehand (helpful for showcase)

          INPUT: JMenu

          OUTPUT: void

            SUCCESS: the specified JMenu with the name 'Doctors' or 'Admins' is filled with all respective values from the database

            FAILURE: the specified JMenu is not filled
*/
    public void addMenuItems(JMenu jMenu) {
        JMenuItem jItem = null;

        if (jMenu.getName() == "Doctors") {
            ArrayList<String> s = c.Query("Select doctorName from Doctor;", false);
            s = singleQuerySplit(s);

            for (String ss : s) {
                jItem = new JMenuItem(ss);
                jItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        userT.setText(ss);
                        password.setText(c.Query("Select doctorID from Doctor where doctorName = '" + ss + "';", false).get(0));
                    }
                });
                jMenu.add(jItem);
            }
        }
        if (jMenu.getName() == "Admins") {
            ArrayList<String> s = c.Query("Select adminName from Admin;", false);
            s = singleQuerySplit(s);

            for (String ss : s) {
                jItem = new JMenuItem(ss);
                jItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        userT.setText(ss);
                        password.setText(c.Query("Select adminID from Admin where adminName = '" + ss + "';", false).get(0));
                    }
                });
                jMenu.add(jItem);
            }
        }
    }
/*
  singleQuerySplit:

      the Query method in Connect class returns an arraylist of entire rows of entries, separated by commas

      this method parses that row entry into an arraylist of each column value


         INPUT: ArrayList<String>

         OUTPUT: ArrayList<String>

            SUCCESS: The specified ArrayList<String> (of one value) is parsed into an ArrayList<String> of values equal to their database column equivalent

            FAILURE: An empty ArrayList<String> is returned;
*/
    public ArrayList<String> singleQuerySplit(ArrayList<String> s){
        java.lang.String[] ss = new String[s.size()];
        ArrayList<java.lang.String> cleaned = new ArrayList<>();
        for(int i=0;i<s.size();i++){
            ss[i] = s.get(i).substring(0,s.get(i).indexOf(","));
        }

        for(int j=0;j<ss.length;j++){
            cleaned.add(ss[j]);
        }
        for(String temp: cleaned){
            if(temp.isEmpty()){
                cleaned.remove(temp);
            }
        }
        return cleaned;
    }

    }



