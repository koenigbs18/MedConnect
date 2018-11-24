
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

public class Connect {

    static final String databasePrefix = "cs366-2177_koenigbs18";
    static final String netID = "koenigbs18"; // Please enter your netId
    static final String hostName = "washington.uww.edu";
    static final String databaseURL = "jdbc:mysql://" + hostName + "/" + databasePrefix;
    static final String password = "-redacted-"; // please enter your own password

    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    public boolean Connection() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("databaseURL" + databaseURL);
            connection = DriverManager.getConnection(databaseURL, netID, password);
            System.out.println("Successfully connected to the database");
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    } // end of Connection

    public ArrayList<String> Query(String sqlQuery, boolean update) {
    	ArrayList<String> list = new ArrayList<String>();
        try {
            statement = connection.createStatement();
            if(update == true)
            	statement.executeUpdate(sqlQuery);
            else
            	resultSet = statement.executeQuery(sqlQuery);

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columns = metaData.getColumnCount();

            while (resultSet.next()) {
            	String s = "";

                for (int i=1; i<= columns; i++) {
                    s+=resultSet.getObject(i)+ ",";
                }
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    } // end of simpleQuery method
}
	    
