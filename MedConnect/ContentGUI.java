/*
    ContentGUI:

    4 panels holding each section of the Content Pane
    p1 - [north] checkboxes
    p2 - [west] list
    p3 - [center] main display
    p4 - [east] user info

 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Color.*;
import java.util.ArrayList;

public class ContentGUI extends JFrame{

    private Container pane;
    private JList list;
    private Checkbox doctor, patient, medication;
    private JPanel p1, p2, p3, p4;
    private JTextArea j1, j2;
    private JLabel select;
    String s;


    public ContentGUI(int AccessLevel){

        //display GUI for administrator
        if(AccessLevel>0) {

            //Initialize pane
            pane = new Container();
            pane = getContentPane();
            pane.setLayout(new BorderLayout(5, 5));

            //Initialize checkboxes and set actions
            doctor = new Checkbox("Doctor");
            doctor.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent itemEvent) {
                    if (doctor.isEnabled()) {
                        medication.setState(false);
                        patient.setState(false);
                    }
                    s = "";
                    s += "Select * from Doctor";
                    System.out.println(s);
                }
            });
            patient = new Checkbox("Patient");
            patient.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent itemEvent) {
                    if (patient.isEnabled()) {
                        medication.setState(false);
                        doctor.setState(false);
                    }
                    s = "";
                    s += "Select * from Patient";
                    System.out.println(s);
                }
            });
            medication = new Checkbox("Medication");
            medication.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent itemEvent) {
                    if (medication.isEnabled()) {
                        patient.setState(false);
                        doctor.setState(false);
                    }
                    s = "";
                    s += "Select * from Medication";
                    System.out.println(s);
                }
            });

            //Checkbox Panel
            p1 = new JPanel();
            p1.setBackground(Color.WHITE);
            p1.setSize(100, 500);
            p1.add(doctor);
            p1.add(patient);
            p1.add(medication);
            pane.add(p1, BorderLayout.NORTH);

            //Selection Menu
            p2 = new JPanel();
            p2.setSize(395, 100);
            p2.setBackground(Color.WHITE);
            select = new JLabel("Select");
            String[] s = {"Harold","Maud"};
            list = new JList(s);
            list.setModel(new DefaultListModel());
            list.add(select);
            p2.add(list);
            pane.add(p2, BorderLayout.EAST);

            //Main Display
            p3 = new JPanel();
            p3.setSize(395, 300);

            p3.setBackground(Color.CYAN);
            j1 = new JTextArea();
            p3.add(j1);
            pane.add(p3, BorderLayout.CENTER);

            //User Info
            p4 = new JPanel();
            p4.setSize(395, 100);
            p4.setBackground(Color.WHITE);
            j2 = new JTextArea("User info");
            p4.add(j2);
            pane.add(p4, BorderLayout.WEST);

         //GUI structure for doctor
        }else{

        }


        //Configure pane and display
        setSize(500,500);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    public JTextArea updateDisplay(String SQLreturn){
        return null;

    }
}
