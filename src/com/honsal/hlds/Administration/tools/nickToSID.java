package com.honsal.hlds.Administration.tools;

import com.honsal.dbcon.DBConnection;

import java.sql.ResultSet;

/**
 * Created by I on 2015-02-01.
 */
public class nickToSID {
    public static String getSID(String dbname, String input) {
        String sid = "";
        if (input.matches("^STEAM_\\d:\\d:\\d+$")) {
            sid = input;
        } else {
            if (dbname.equals("") || dbname == null) {
                return null;
            }
            DBConnection dbcon = new DBConnection();
            dbcon.setUserName("honsal");
            dbcon.setPassword("Wkddldi)5284120MYSQLljm05284120");
            dbcon.setDBName(dbname);
            dbcon.connect();

            try {
                ResultSet res = dbcon.query("SELECT sid FROM warnings WHERE nick LIKE \"%" + input + "%\"");
                if (res.next()) {
                    sid = res.getString(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                dbcon.close();
            }
        }
        if (sid == null || sid.equals("")) {
            return null;
        }
        return sid;
    }
}
