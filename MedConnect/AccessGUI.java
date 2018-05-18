/*
AccessGUI
    Displays Login GUI

    clicking 'submit' sends trigger to SubmitButtonHandler:
                      makes connection to SQL server
                      checks information for accuracy
 */
import javax.swing.*;
import java.awt.*;
import java.awt.Event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AccessGUI extends JFrame {
    private JLabel user, pass;
    private JButton submit;
    private JTextField userT, passT;
    private Panel p1, p2, p3;
    private Container pane;
    private JMenuBar menuBar;
    private JMenu adminMenu, doctorMenu;
    private ArrayList<JMenuItem> items;

    public AccessGUI(){
            //panel 1 holds submit button
            p1 = new Panel();

            SubmitButtonHandler sbh = new SubmitButtonHandler();
            submit = new JButton("Submit");
            submit.addActionListener(sbh);
            p1.add(submit);

            //panel 2 holds username input box and label
            p2 = new Panel();
            user = new JLabel("username:");
            userT = new JTextField(10);
            p2.add(user);
            p2.add(userT);

            //panel 3 holds password input box and label
            p3 = new Panel();
            pass = new JLabel("password:");
            passT = new JTextField(10);
            p3.add(pass);
            p3.add(passT);

            //menu
            menuBar = new JMenuBar();
            adminMenu = new JMenu("Admins");
            doctorMenu = new JMenu("Doctors");

            //initialize content pane
            pane = new Container();
            pane=getContentPane();
            pane.setLayout(new GridLayout(1,2) );

            //add panels to content pane
            pane.add(p2);
            pane.add(p3);
            pane.add(p1);

            //configure content pane and display
            setSize(480, 120);
            setVisible(true);
            setDefaultCloseOperation(EXIT_ON_CLOSE);


    }

    //program automatically jumps here when submit button is clicked
    public class SubmitButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Connect c = new Connect();

            //check for connection success
            if (c.Connection()) {
                String AccessLevel = c.Query("Select accessLevel from Admin Where Admin.AdminId = '" + passT.getText() + "' AND Admin.name = '" + userT.getText() + "';");

                //if an administrator
                if (!(AccessLevel == "")) {
                    dispose();
                    ContentGUI cObj = new ContentGUI(Integer.parseInt(AccessLevel.substring(0, 1)));
                    //if not administrator, doctor?
                } else if (!(c.Query("Select * from Doctor Where Doctor.DoctorId = '" + passT.getText() + "' AND Doctor.name = '" + userT.getText()) == "")) {
                    dispose();
                    ContentGUI cObj = new ContentGUI(0);
                    //not administrator or doctor, bad login
                } else {
                    JOptionPane.showMessageDialog(null, "Incorrect Login Information");
                }
                //check for connection failure
            } else {
                //display popup message "Connection Failed"
                JOptionPane.showMessageDialog(null, "Connection to database failed. God help you.");
            }

        }
    }



    //Populate Menu for easy selection
    public class menuItemHandler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent actionEvent){
            Connect c = new Connect();
            for(JMenuItem j: items){

            }
        }

    }
    public void addMenuItems(JMenu jMenu){
        Connect c = new Connect();
        menuItemHandler m = new menuItemHandler();
        String s;
        String[] ss;
        JMenuItem jItem;
        if(c.Connection()) {
            if (jItem.getName() == "Doctors") {

                s = c.Query("Select name from Doctor;");
                ss = s.split(",");

                for(int i=0;i<ss.length;i++){
                    jItem = new JMenuItem(ss[i]);
                    items.add(jItem);
                    jItem.addActionListener(m);
                }
            }
            if (jItem.getName() == "Admins") {
                s = c.Query("Select name from Admin;");
            }
        }

    }


}
