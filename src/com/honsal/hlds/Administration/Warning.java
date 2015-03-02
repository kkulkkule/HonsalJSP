package com.honsal.hlds.Administration;

import com.honsal.dbcon.DBConnection;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 * Created by I on 2015-01-30.
 */
public class Warning extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        String game = request.getParameter("game");
        String action = request.getParameter("action");

        if (game == null || action == null) {
            out.print("게임 혹은 행동이 지정되지 않았습니다.");
            return;
        }

        DBConnection dbcon = new DBConnection();
        dbcon.setUserName("honsal");
        dbcon.setPassword("Wkddldi)5284120MYSQLljm05284120");
        dbcon.setDBName(game);
        dbcon.connect();
        try {
            if (action.equals("playerConnect")) {
                String sid = request.getParameter("sid");
                String nick = request.getParameter("nick");

                if (sid == null || nick == null) {
                    out.print("[{Error:\"SID 혹은 닉네임이 지정되지 않았습니다.\"}]");
                    dbcon.close();
                    return;
                }

                ResultSet res = dbcon.query("SELECT * FROM warnings WHERE sid = \"" + sid + "\"");

//                플레이어 정보가 없으면 새로운 레코드 추가
                if (!res.next()) {
                    dbcon.queryNoResult("INSERT INTO warnings(sid, nick) VALUES (\"" + sid + "\", \"" + nick + "\")");
//                있으면 처리
                } else {
                    if (!res.getString("nick").equals(nick)) {
                        dbcon.queryNoResult("UPDATE warnings SET nick = \"" + nick + "\" WHERE sid = \"" + sid + "\"");
                    }
                    Boolean banned = res.getBoolean("banned");
                    Timestamp bannedTime = res.getTimestamp("bannedTime");
                    Timestamp bannedTo = res.getTimestamp("bannedTo");
                    out.print("banned:" + (banned ? "true" : "false"));
                    out.print("bannedTime:" + (bannedTime != null ? bannedTime.toString() : "0"));
                    out.print("bannedTo:" + (bannedTo != null ? bannedTo.toString() : "0"));
                }
            }

            if (action.equals("addWarn")) {
                String sid = request.getParameter("sid");
                String warns = request.getParameter("warns");

                if (sid == null || warns == null) {
                    out.print("에러 발생: SID 혹은 경고 횟수가 누락되었습니다.");
                    dbcon.close();
                    return;
                }

                if (!warns.matches("^-?\\d+$")) {
                    out.print("에러 발생: 경고 횟수가 숫자가 아닙니다.");
                    dbcon.close();
                    return;
                }

                ResultSet res = null;

                Boolean isnick = !sid.matches("^STEAM_\\d:\\d:\\d+$");

                if (isnick) {
                    res = dbcon.query("SELECT warns FROM warnings WHERE nick LIKE \"%" + sid + "%\"");
                } else {
                    res = dbcon.query("SELECT warns FROM warnings WHERE sid = \"" + sid + "\"");
                }

                if (!res.next()) {
                    out.print("에러 발생: 해당 유저의 정보가 DB에 없습니다.");
                    dbcon.close();
                    return;
                }

                int curwarns = res.getInt("warns");
                int reswarns = curwarns + Integer.parseInt(warns);

                if (reswarns < 0)
                    reswarns = 0;

                dbcon.queryNoResult("UPDATE warnings SET warns = " + reswarns + " WHERE " + (isnick ? "nick = " + sid : "sid = \"" + sid + "\""));
                dbcon.queryNoResult("UPDATE polices SET processed = TRUE, processed_at = NOW() WHERE reported = \"" + sid + "\" LIMIT 1");

                res = dbcon.query("SELECT warns FROM warnings WHERE " + (isnick ? "nick = " + sid : "sid = \"" + sid + "\""));

                if (res.next()) {
                    curwarns = res.getInt(1);
                    out.print(curwarns);
                }
            }

            if (action.equals("getWarn")) {
                String sid = request.getParameter("sid");
                Boolean isnick = !sid.matches("^STEAM_\\d:\\d:\\d+$");
                ResultSet res = dbcon.query("SELECT warns, nick FROM warnings WHERE " + (isnick ? "nick = " + sid : "sid = \"" + sid + "\""));
                if (res.next()) {
                    int warns = res.getInt(1);
                    String nick = res.getString("nick");
                    out.print(warns + "\n");
                    out.print(nick);
                }
            }

            if (action.equals("ban")) {
                String sid = request.getParameter("sid");
                String ban = request.getParameter("ban");
                if (ban.equals("1")) {
                    String banTime = request.getParameter("banTime");
                    Timestamp curtime = new Timestamp(System.currentTimeMillis());
                    Timestamp endtime = new Timestamp(System.currentTimeMillis() + (banTime == null ? 0 : Integer.parseInt(banTime)));

                    dbcon.queryNoResult("UPDATE warnings SET banned = " + ban + ", bannedTime = \"" + curtime.toString() + "\", bannedTo = \"" + endtime.toString() + "\" WHERE sid = \"" + sid + "\"");
                } else {
                    dbcon.queryNoResult("UPDATE warnings SET banned = " + ban + ", bannedTime = NULL, bannedTo = NULL, warns = 0 WHERE sid = \"" + sid + "\"");
                }
            }

            if (action.equals("getStatus")) {
                String sid = request.getParameter("sid");
                ResultSet res = dbcon.query("SELECT banned, bannedTime, bannedTo FROM warnings WHERE sid = \"" + sid + "\"");
                if (res.next()) {
                    Boolean banned = res.getBoolean("banned");
                    Timestamp bannedTime = res.getTimestamp("bannedTime");
                    Timestamp bannedTo = res.getTimestamp("bannedTo");

                    out.println("banned>>> " + banned);
                    out.println("bannedTime>>> " + (bannedTime == null ? 0 : bannedTime.getTime() / 1000));
                    out.println("bannedTo>>> " + (bannedTo == null ? 0 : bannedTo.getTime() / 1000));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbcon.close();
        }
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("GET REQUESTS ARE NOT ALLOWED.");

    }
}