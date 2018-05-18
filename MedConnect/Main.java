/*
Main:
    Initialized  either GUI from object

 */

import java.util.ArrayList;

public class Main
{
    public static void main(String[] args){
        /*
        ArrayList<Integer> DoctorId = new ArrayList<>();

        Connect c = new Connect();
        c.Connection();
        String s =c.Query("Select * from Doctor;");
        String[] ss = s.split(",");
        for(int i=0;i<ss.length;i++){
            if(i%6==0){
                AdminId.add(Integer.parseInt(ss[i]));
            }


        }
        for(int i: AdminId){
            System.out.println(i);
        }
        String t = c.Query("Select * from isTreating");
        ArrayList<Integer> DoctorId
           */



        //ContentGUI cObj = new ContentGUI(2);
        AccessGUI aObj = new AccessGUI();

    }
}
