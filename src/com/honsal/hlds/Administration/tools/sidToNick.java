package com.honsal.hlds.Administration.tools;

import com.honsal.dbcon.DBConnection;

import java.sql.ResultSet;

/**
 * Created by I on 2015-02-01.
 */
public class sidToNick {
    public static String getNick(String dbname, String input) {
        if (dbname.equals("") || dbname == null) {
            return null;
        }
        String nick = null;
        if (input.matches("^STEAM_\\d:\\d:\\d+$")) {
            DBConnection dbcon = new DBConnection();
            dbcon.setUserName("honsal");
            dbcon.setPassword("Wkddldi)5284120MYSQLljm05284120");
            dbcon.setDBName(dbname);
            dbcon.connect();

            try {
                ResultSet res = dbcon.query("SELECT nick FROM warnings WHERE sid = \"" + input + "\"");
                if (res.next()) {
                    nick = res.getString(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                dbcon.close();
            }
        }
        return nick;
    }
}
