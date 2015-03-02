package com.honsal.dbcon;

import java.sql.*;

/**
 * Created by I on 2015-01-29.
 */
public class DBConnection {
    String dbname = null;
    String user = null;
    String pass = null;
    Connection conn = null;
    Statement stmt = null;
    ResultSet res = null;

    public DBConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setDBName(String name) {
        dbname = name;
    }

    public void setUserName(String name) {
        user = name;
    }

    public void setPassword(String passwd) {
        pass = passwd;
    }

    public String getDBName() {
        return dbname;
    }

    public String getUserName() {
        return user;
    }

    public String getPassword() {
        return pass;
    }

    public Connection connect() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + (dbname != null ? dbname : ""), user, pass);
            stmt = conn.createStatement();
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean close() {
        try {
            stmt.close();
            conn.close();
            if (res != null) {
                res.close();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet query(String sql) {
        try {
            if (res != null) {
                res.close();
            }
            res = stmt.executeQuery(sql);
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void queryNoResult(String sql) {
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
