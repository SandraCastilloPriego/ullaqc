package ullaqc.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author drone
 */
public class Database {

    private Connection conn;
    private Statement stat;
    private PreparedStatement prep;

    public Database() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:conf/Ullalite.sqlite");
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Database getInstance() {
        if (ref == null) {
            // it's ok, we can call this constructor
            try {
                ref = new Database();
            } catch (Exception e) {
                System.out.println("getInstance says " + e.toString());
                return null;
            }
        }
        return ref;
    }

    /**
     * Returns a ResultSet 
     * @param queryString
     * @return
     */
    public ResultSet query(String queryString) {
        try {
            stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(queryString);
            return rs;
        } catch (Exception e) {
            System.out.println("Query says " + e.toString());
            return null;
        }
    }

    /* Returns a ResultSet, same with positional quoted string parameters, instead each sign '?' */
    public ResultSet query(String queryString, String[] parameters) {
        try {
            prep = conn.prepareStatement(queryString);
            for (int i = 1; i <= parameters.length; i++) {
                prep.setString(i, parameters[i - 1]);
                //System.out.println("Param"+parameters[i-1]);
            }
            ResultSet rs = prep.executeQuery();
            return rs;
        } catch (Exception e) {
            System.out.println("Query says " + e.toString());
            return null;
        }
    }

    public void insert(String table, Hashtable<String, String> vars) {
        String query = "INSERT INTO \"" + table + "\" ";

        Set<String> set = vars.keySet();
        Iterator<String> itr = set.iterator();
        String str;
        StringBuffer bufferColumns = new StringBuffer();
        StringBuffer bufferValues = new StringBuffer();
        while (itr.hasNext()) {
            str = itr.next();
            bufferColumns.append("'" + str + "'");
            bufferValues.append("'" + vars.get(str) + "'");
            if (itr.hasNext()) {
                bufferColumns.append(",");
                bufferValues.append(",");
            }
        }
        query = query.concat("(" + bufferColumns.toString() + ")");
        query = query.concat("VALUES (" + bufferValues.toString() + ")");
        //System.out.println(query);

        try {
            PreparedStatement prepST = conn.prepareStatement(query);
            prepST.execute();
        } catch (Exception e) {
            System.err.println("Error on insert " + e.toString());
        }
    }

    public void update(String table, Hashtable<String, String> vars, String where) {
        String query = "UPDATE " + table + " SET ";

        Set<String> set = vars.keySet();
        Iterator<String> itr = set.iterator();
        String str;
        StringBuffer buffer = new StringBuffer();
        while (itr.hasNext()) {
            str = itr.next();
            // Use carefully, no quotes!
            buffer.append(str + "=" + vars.get(str));
            if (itr.hasNext()) {
                buffer.append(", ");
            }
        }
        query = query.concat(buffer.toString());
        if (where.length() > 0) {
            query = query.concat(" WHERE " + where + ";");
        }

        try {
            PreparedStatement prepST = conn.prepareStatement(query);           
            prepST.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error on update " + e.toString());
        }
    }

    public void close() {
        try {
            conn.close();
        } catch (Exception e) {
            // pass
        }
    }

    public static Object[] getMolecules() {
        Database db = new Database();
        ResultSet result = db.query("SELECT * FROM Molecules");
        List<String> list = new ArrayList<String>();
        try {
            while (result.next()) {
                list.add((String) result.getString("Name"));
            }
        } catch (SQLException ex) {
        }
        db.close();
        return list.toArray(new String[0]);
    }
    private static Database ref;
}
