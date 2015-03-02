<%@ page import="com.honsal.dbcon.DBConnection" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.Timestamp" %>
<%
    DBConnection dbcon = new DBConnection();
    dbcon.setDBName("unitedban");
    dbcon.setUserName("honsal");
    dbcon.setPassword("Wkddldi)5284120MYSQLljm05284120");
    dbcon.connect();

    ResultSet list = dbcon.query("SELECT * FROM banlist");
    while (list.next()) {
        String id = Integer.toString(list.getInt("id"));
        String nick = list.getString("nick");
        if (list.wasNull())
            nick = "{UNKNOWN}";
        String serial = list.getString("serial");
        if (list.wasNull())
            serial = "{UNKNOWN}";
        String reason = list.getString("reason");
        if (list.wasNull())
            reason = "{UNKNOWN}";
        Timestamp bat = list.getTimestamp("bannedAt");
        String bannedAt = "";
        if (list.wasNull())
            bannedAt = "0";
        else
            bannedAt = bat.toString();
        Timestamp bto = list.getTimestamp("bannedTo");
        String bannedTo = "";
        if (list.wasNull())
            bannedTo = "0";
        else
            bannedTo = bto.toString();
        String complete = "";
        complete += nick;
        complete += ",";
        complete += serial;
        complete += ",";
        complete += reason;
        complete += ",";
        complete += bannedAt;
        complete += ",";
        complete += bannedTo;
        complete += ",";
%>
<%= complete%><br />
<%
    }
    dbcon.close();
%>