
/*
  ContentGUI:

  4 panels holding each section of the Content Pane
  p1 - [north] checkboxes
  p2 - [west] list
  p3 - [center] main display
  p4 - [east] user info

*/

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.awt.Color.*;
import java.util.ArrayList;

public class ContentGUI extends JFrame {

   private Container pane;
   private JList userInfoList;
   private Checkbox doctor, patient, medication;
   private JLabel lTemp;
   private JButton update, back;
   private JTextField tTemp, search;
   JTextField pID, pSSN, pName, pAge;
   JTextField pSideEffect;
   JTextField dID, dName, dDeg, dSpec, dYear, dPay;
   JTextField mID, mName, sideEffects;
   private JPanel sideEffectPanel;
   private Boolean medicationEnabled = false, doctorEnabled = false, patientEnabled = false;
   private int aLevel = 0;
   // Main Skeletal Panels
   private JPanel checkboxPanel, mainDisplayPanel, scrollListPanel;

   // Panels for Main Patient Display
   private JPanel idPanel, ssnPanel, namePanel, agePanel, updatePanel;

   // Panels for Main Doctor Display
   private JPanel payPanel, specPanel, yearPanel, degPanel;

   // Panels for Main Medication Display
   private JPanel sidePanel;

   private JPanel searchPanel;

   private JScrollPane userInfoScrollPane;
   DefaultListModel model = new DefaultListModel();
   String s, uName;
   JLabel jabel;
   Connect c = new Connect();

   public ContentGUI(int AccessLevel, Connect c, String userName) {
	   aLevel = AccessLevel;
	   uName = userName;
       if (AccessLevel == 0)
           System.out.println("We are a doctor!");
       if (AccessLevel == 1)
           System.out.println("We are an Admin!");
       this.c = c;

       // Initialize pane
       pane = new Container();
       pane = getContentPane();
       pane.setLayout(new BorderLayout(5, 5));
       getContentPane().setBackground(new Color(66, 185, 244));
       
       

       /*------------------------------------

       CHECKBOXES

       -------------------------------------- */
       // ---------------doctor Checkbox
       doctor = new Checkbox("Doctor");
       if (AccessLevel == 0)
           doctor.setEnabled(false);
       doctor.addItemListener(new ItemListener() {
           @Override
           public void itemStateChanged(ItemEvent itemEvent) {
               if (doctor.isEnabled()) {
                   medication.setState(false);
                   patient.setState(false);
               }
               if (doctor.getState() == false) {
                   model.removeAllElements();
                   doctorEnabled = false;
               } else {
                   doctorEnabled = true;
                   populateUserInfo("Doctor");
               }
           }
       });

       // --------------patient Checkbox
       patient = new Checkbox("Patient");
       patient.addItemListener(new ItemListener() {
           @Override
           public void itemStateChanged(ItemEvent itemEvent) {
               if (patient.isEnabled()) {
                   medication.setState(false);
                   doctor.setState(false);
               }
               if (patient.getState() == false) {
                   model.removeAllElements();
                   patientEnabled = false;
               } else {
                   patientEnabled = true;
                   populateUserInfo("Patient");
               }
           }
       });

       // -------------medication Checkbox
       medication = new Checkbox("Medication");
       medication.addItemListener(new ItemListener() {
           @Override
           public void itemStateChanged(ItemEvent itemEvent) {
               if (medication.isEnabled()) {
                   patient.setState(false);
                   doctor.setState(false);
               }
               if (medication.getState() == false) {
                   model.removeAllElements();
                   medicationEnabled = false;
               } else {
                   medicationEnabled = true;
                   populateUserInfo("Medication");
               }
           }
       });

       // --------Checkbox Panel
       checkboxPanel = new JPanel();
       checkboxPanel.setBackground(Color.WHITE);
       checkboxPanel.setSize(100, 500);
       jabel = new JLabel("|||" + userName + "|||");
       back = new JButton("Logout");
       back.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent actionEvent) {
               dispose();
               AccessGUI aObj = new AccessGUI(c);
           }
       });
       checkboxPanel.add(jabel);
       checkboxPanel.add(doctor);
       checkboxPanel.add(patient);
       checkboxPanel.add(medication);
       checkboxPanel.add(back);

       pane.add(checkboxPanel, BorderLayout.NORTH);

       /*-----------------------------------

          MAIN DISPLAY

       ------------------------------------ */

       mainDisplayPanel = new JPanel();

       mainDisplayPanel.setSize(395, 300);
       mainDisplayPanel.setBackground(Color.WHITE);
       pane.add(mainDisplayPanel, BorderLayout.CENTER);

       /*------------------------------------

          LIST

       ------------------------------------*/

       // JList of table's names
       // region JList
       userInfoList = new JList(model);
       userInfoList.addListSelectionListener(new ListSelectionListener() {
           @Override
           public void valueChanged(ListSelectionEvent listSelectionEvent) {
               String sql;
               ArrayList<String> content;
               if (medication.getState() == true && doctor.getState() == false && patient.getState() == false) {
                   sql = "Select * from Medication where medicationName = '" + userInfoList.getSelectedValue() + "';";
                   content = c.Query(sql, false);
                   if(!content.isEmpty()) {
                       content = singleQuerySplit(content.get(0));
                       updateDisplay(content);
                   }
               } else if (doctor.getState() == true && medication.getState() == false && patient.getState() == false) {
                   sql = "Select * from Doctor where doctorName = '" + userInfoList.getSelectedValue() + "';";
                   content = c.Query(sql, false);
                   if(!content.isEmpty()) {
                       content = singleQuerySplit(content.get(0));
                       updateDisplay(content);
                   }
               } else if (patient.getState() == true && medication.getState() == false && doctor.getState() == false) {
                   sql = "Select * from Patient where patientName = '" + userInfoList.getSelectedValue() + "';";
                   content = c.Query(sql, false);
                   if(!content.isEmpty()) {
                       content = singleQuerySplit(content.get(0));

                       updateDisplay(content);
                   }
               }
           }
       });
       // endregion

       // region Scroll Pane
       // Scroll pane
       userInfoScrollPane = new JScrollPane(userInfoList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
               JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
       userInfoScrollPane.setPreferredSize(new Dimension(300, 630));
       // endregion

       // region Add List Panel to Frame

       search = new JTextField(10);
       search.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent actionEvent) {
               model.removeAllElements();
               ArrayList<String> searchQuery = new ArrayList<>();
               String s = search.getText();
               if (medicationEnabled) {
                   if (s.isEmpty()) {
                       userInfoList.clearSelection();
                       populateUserInfo("Medication");
                   } else {
                       s = "Select medicationName from Medication where medicationName = '" + s + "';";
                       searchQuery = c.Query(s, false);
                   }


               } else if (doctorEnabled) {

                   if (s.isEmpty()) {
                       userInfoList.clearSelection();
                       populateUserInfo("Doctor");
                   }
                   else {
                       s = "Select doctorName from Doctor where doctorName = '" + s + "';";
                       searchQuery = c.Query(s, false);
                   }
               } else if (patientEnabled) {
                   if (s.isEmpty()) {
                       userInfoList.clearSelection();
                       populateUserInfo("Patient");
                   }
                   else {
                       s = "Select patientName from Patient where patientName = '" + s + "';";
                       searchQuery = c.Query(s, false);
                   }

               } else {

               }
               if(!searchQuery.isEmpty()) {
                   searchQuery = singleQuerySplit(searchQuery.get(0));
                   model.addElement(searchQuery.get(0));
               }

           }
       });
       searchPanel = new JPanel();
       searchPanel.add(search);
       // Containing Panel
       scrollListPanel = new JPanel();
       scrollListPanel.setSize(395, 100);
       scrollListPanel.setBackground(Color.WHITE);
       scrollListPanel.setLayout(new BorderLayout());
       scrollListPanel.add(searchPanel, BorderLayout.NORTH);
       scrollListPanel.add(userInfoScrollPane, BorderLayout.CENTER);
       pane.add(scrollListPanel, BorderLayout.WEST);
       // endregion

       /*----------------------------------

          CONTENT PANE

       ------------------------------------ */
       setSize(620, 720);
       setVisible(true);
       setLocationRelativeTo(null);
       setDefaultCloseOperation(EXIT_ON_CLOSE);

   }// end ContentGUI

   /*---------------------------------------------------

      updateDisplay:

              accepts a SQL statement from List Selection Event
              checks checkboxes for table to draw from

   --------------------------------------------------- */
   // region updateDisplay
   public void updateDisplay(ArrayList<String> SQLreturn) {

       JLabel jTemp;
       JTextField tTemp;



       if (medication.getState() == true) {

           mainDisplayPanel.removeAll();
           mainDisplayPanel.setLayout(new GridLayout(5, 1));

           // -----------------------id

           jTemp = new JLabel("Identification Number: ");

           mID = new JTextField(SQLreturn.get(0), 10);
           mID.setEditable(false);
           mID.setSize(new Dimension(100, 40));
           mID.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent actionEvent) {
               }
           });
           idPanel = new JPanel();
           idPanel.add(jTemp);
           idPanel.add(mID);
           idPanel.setSize(new Dimension(80, 300));
           mainDisplayPanel.add(idPanel);

           // ------------------------name

           jTemp = new JLabel("Medication Name: ");
           ;
           jTemp.setSize(new Dimension(400, 20));

           mName = new JTextField(SQLreturn.get(1), 10);
           mName.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent actionEvent) {

               }
           });
           namePanel = new JPanel();
           namePanel.add(jTemp);
           namePanel.add(mName);
           namePanel.setSize(new Dimension(80, 300));
           mainDisplayPanel.add(namePanel);

           // ------------------------side efects

           jTemp = new JLabel("Side Effects: ");

           pSideEffect = new JTextField(SQLreturn.get(2));
           pSideEffect.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent actionEvent) {

               }
           });
           sideEffectPanel = new JPanel();
           sideEffectPanel.add(jTemp);
           sideEffectPanel.setSize(80, 300);
           mainDisplayPanel.add(sideEffectPanel);
           mainDisplayPanel.add(pSideEffect);

           update = new JButton("Update");
           update.addActionListener(new ActionListener() {

               @Override
               public void actionPerformed(ActionEvent arg0) {
                   c.Query("UPDATE Medication SET medicationName = '" + mName.getText() + "', sideEffects = '"
                           + pSideEffect.getText() + "' WHERE medicationID = " + mID.getText() + ";", true);
               }

           });
           update.setVerticalTextPosition(AbstractButton.CENTER);
           update.setHorizontalTextPosition(AbstractButton.LEADING); // aka LEFT, for left-to-right locales
           updatePanel = new JPanel();
           updatePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
           updatePanel.add(update);
           mainDisplayPanel.add(updatePanel);

           pack();

       } else if (doctor.getState() == true) {

           mainDisplayPanel.removeAll();
           mainDisplayPanel.setLayout(new GridLayout(7, 1));

           // -----------------------id

           jTemp = new JLabel("Identification Number: ");

           dID = new JTextField(SQLreturn.get(0), 10);
           dID.setEditable(false);
           dID.setSize(new Dimension(100, 40));
           dID.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent actionEvent) {
               }
           });
           idPanel = new JPanel();
           idPanel.add(jTemp);
           idPanel.add(dID);
           idPanel.setSize(new Dimension(80, 300));
           mainDisplayPanel.add(idPanel);

           // ------------------------name

           jTemp = new JLabel("Doctor Name: ");
           ;
           jTemp.setSize(new Dimension(400, 20));

           dName = new JTextField(SQLreturn.get(1), 10);
           dName.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent actionEvent) {

               }
           });
           namePanel = new JPanel();
           namePanel.add(jTemp);
           namePanel.add(dName);
           namePanel.setSize(new Dimension(80, 300));
           mainDisplayPanel.add(namePanel);

           // ------------------------degree

           jTemp = new JLabel("Doctor Degree: ");

           dDeg = new JTextField(SQLreturn.get(2), 5);
           dDeg.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent actionEvent) {

               }
           });
           degPanel = new JPanel();
           degPanel.add(jTemp);
           degPanel.add(dDeg);
           degPanel.setSize(80, 300);
           mainDisplayPanel.add(degPanel);

           // ------------------------specialty

           jTemp = new JLabel("Doctor Specialty: ");

           dSpec = new JTextField(SQLreturn.get(3), 5);
           dSpec.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent actionEvent) {

               }
           });
           specPanel = new JPanel();
           specPanel.add(jTemp);
           specPanel.add(dSpec);
           specPanel.setSize(80, 300);
           mainDisplayPanel.add(specPanel);

           // ------------------------year

           jTemp = new JLabel("Doctor Year: ");

           dYear = new JTextField(SQLreturn.get(4), 5);
           dYear.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent actionEvent) {

               }
           });
           yearPanel = new JPanel();
           yearPanel.add(jTemp);
           yearPanel.add(dYear);
           yearPanel.setSize(80, 300);
           mainDisplayPanel.add(yearPanel);

           // ------------------------year

           jTemp = new JLabel("Doctor Pay: ");

           dPay = new JTextField(SQLreturn.get(5), 5);
           dPay.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent actionEvent) {

               }
           });
           payPanel = new JPanel();
           payPanel.add(jTemp);
           payPanel.add(dPay);
           payPanel.setSize(80, 300);
           mainDisplayPanel.add(payPanel);

           update = new JButton("Update");
           update.addActionListener(new ActionListener() {

               @Override
               public void actionPerformed(ActionEvent arg0) {
                   c.Query("UPDATE Doctor SET doctorName = '" + dName.getText() + "', doctorDeg = '" + dDeg.getText()
                           + "', doctorSpec = '" + dSpec.getText() + "', doctorYear = " + dYear.getText()
                           + ", doctorPay = " + dPay.getText() + " WHERE doctorID = " + dID.getText() + ";", true);

               }

           });

           update.setVerticalTextPosition(AbstractButton.CENTER);
           update.setHorizontalTextPosition(AbstractButton.LEADING); // aka LEFT, for left-to-right locales
           updatePanel = new JPanel();
           updatePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
           updatePanel.add(update);
           mainDisplayPanel.add(updatePanel);

           pack();

       } else if (patient.getState() == true) {

           mainDisplayPanel.removeAll();
           mainDisplayPanel.setLayout(new GridLayout(5, 1));

           // -----------------------id

           jTemp = new JLabel("Identification Number: ");

           pID = new JTextField(SQLreturn.get(0), 10);
           pID.setEditable(false);
           pID.setSize(new Dimension(100, 40));
           pID.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent actionEvent) {
               }
           });
           idPanel = new JPanel();
           idPanel.setLayout(new BorderLayout());
           idPanel.add(jTemp, BorderLayout.NORTH);
           idPanel.add(pID, BorderLayout.SOUTH);
           idPanel.setSize(new Dimension(80, 300));
           mainDisplayPanel.add(idPanel);

           // ------------------------ssn

           jTemp = new JLabel("Social Security Number: ");
           ;

           pSSN = new JTextField(SQLreturn.get(1), 10);
           pSSN.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent actionEvent) {

               }
           });
           ssnPanel = new JPanel();
           ssnPanel.setLayout(new BorderLayout());
           ssnPanel.add(jTemp, BorderLayout.NORTH);
           ssnPanel.add(pSSN, BorderLayout.SOUTH);
           ssnPanel.setSize(new Dimension(80, 300));
           mainDisplayPanel.add(ssnPanel);

           // ------------------------name

           jTemp = new JLabel("Patient Name: ");
           ;
           jTemp.setSize(new Dimension(400, 20));

           pName = new JTextField(SQLreturn.get(2), 10);
           pName.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent actionEvent) {

               }
           });
           namePanel = new JPanel();
           namePanel.setLayout(new BorderLayout());
           namePanel.add(jTemp, BorderLayout.NORTH);
           namePanel.add(pName, BorderLayout.SOUTH);
           namePanel.setSize(new Dimension(80, 300));
           mainDisplayPanel.add(namePanel);
           // ------------------------age

           jTemp = new JLabel("Patient Age: ");

           pAge = new JTextField(SQLreturn.get(3), 5);
           pAge.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent actionEvent) {

               }
           });
           agePanel = new JPanel();
           agePanel.add(jTemp);
           agePanel.add(pAge);
           agePanel.setSize(80, 300);
           mainDisplayPanel.add(agePanel);

           update = new JButton("Update");
           update.addActionListener(new ActionListener() {

               @Override
               public void actionPerformed(ActionEvent arg0) {
                   c.Query("UPDATE Patient SET patientName = '" + pName.getText() + "', patientAge = " + pAge.getText()
                           + " WHERE patientID = " + pID.getText() + ";", true);

               }

           });
           update.setVerticalTextPosition(AbstractButton.CENTER);
           update.setHorizontalTextPosition(AbstractButton.LEADING); // aka LEFT, for left-to-right locales
           updatePanel = new JPanel();
           updatePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
           updatePanel.add(update);
           mainDisplayPanel.add(updatePanel);

           pack();
       }

   }
   // endregion

   /*-------------------------------------------------------

      singleQuerySplit:

              takes an unsanitized ArrayList<String> from database and returns an ArrayList<String> with each column field having it's own index

              INPUT: ArrayList<String>

              OUTPUT: ArrayList<String>

              SUCCESS: function returns an ArrayList scrubbed of unwanted information

              FAILURE: function does not return value
                       function does not scrub data

   -------------------------------------------------------*/
   // region singleQuerySplit
   public ArrayList<String> singleQuerySplit(String query) {
       String[] temp;
       ArrayList<java.lang.String> cleaned = new ArrayList<>();
       temp = query.split(",");
       for (int j = 0; j < temp.length; j++) {
           cleaned.add(temp[j]);
       }
       for (String temp2 : cleaned) {
           if (temp2.isEmpty() || temp2 == null || temp2 == "") {
               cleaned.remove(temp);
           }
       }
       return cleaned;
   }

   // endregion
   public void populateUserInfo(String form) {
       if (form == "Doctor") {
           model.removeAllElements();
           s = "";
           s += "Select doctorName from Doctor";
           ArrayList<String> list = c.Query(s, false);
           ArrayList<String> list2 = new ArrayList<>();
           for (String temp : list) {
               list2.add(singleQuerySplit(temp).get(0));
           }
           for (String i : list2) {
               model.addElement(i);
           }
           return;
       }
       if (form == "Patient") {
           model.removeAllElements();
           if(aLevel == 0) {
        	   String docID = c.Query("select doctorID from Doctor where doctorName = '" + uName + "';", false).get(0);
        	   docID = docID.substring(0, docID.length()-1);
        	   s = "";
        	   s += "select patientName from Patient join isTreating on Patient.patientID = isTreating.patientID join Doctor on Doctor.doctorID = isTreating.doctorID"
        			   + " where Doctor.doctorID = " + docID + ";";
           }
           else {
        	   s = "";
        	   s += "Select patientName from Patient;";
           }
           ArrayList<String> list = c.Query(s, false);
           ArrayList<String> list2 = new ArrayList<>();
           for (String temp : list) {
               list2.add(singleQuerySplit(temp).get(0));
           }
           for (String i : list2) {
               model.addElement(i);
           }
           return;
       }
       if (form == "Medication") {
           model.removeAllElements();
           s = "";
           s += "Select medicationName from Medication";
           ArrayList<String> list = c.Query(s, false);
           ArrayList<String> list2 = new ArrayList<>();
           for (String temp : list) {
               list2.add(singleQuerySplit(temp).get(0));
           }
           for (String i : list2) {
               model.addElement(i);
           }
       }
       return;
   }

}





