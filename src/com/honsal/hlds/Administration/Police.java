package com.honsal.hlds.Administration;

import com.honsal.dbcon.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import com.honsal.hlds.Administration.tools.*;

/**
 * Created by I on 2015-02-01.
 */
public class Police extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        String game = request.getParameter("game");
        String action = request.getParameter("action");

        if (game == null || action == null)
            out.print("게임 혹은 행동이 지정되지 않았습니다.");

        DBConnection dbcon = new DBConnection();
        dbcon.setUserName("honsal");
        dbcon.setPassword("Wkddldi)5284120MYSQLljm05284120");
        dbcon.setDBName(game);
        dbcon.connect();

        try {
            if (action.equals("receive")) {
                String reporter = request.getParameter("reporter");
                String reported = request.getParameter("reported");
                String reason = request.getParameter("reason");

                reporter = nickToSID.getSID(game, reporter);
                reported = nickToSID.getSID(game, reported);

                if (reporter == null || reported == null) {
                    out.println("잘못된 플레이어를 지정하였습니다.");
                    dbcon.close();
                    return;
                }

                if (reason == null || reason.equals("")) {
                    out.println("사유를 입력하지 않았습니다.");
                    dbcon.close();
                    return;
                }

                dbcon.queryNoResult("INSERT INTO polices(reporter, reported, reason) VALUES (\"" + reporter + "\", \"" + reported + "\", \"" + reason + "\")");
                out.println("신고 접수됨.");
            }

            if (action.equals("viewPolices")) {
                Boolean isAdmin = Boolean.parseBoolean(request.getParameter("isAdmin"));
                String requester = request.getParameter("requester");

                ResultSet res = null;
                String resStr = "";

                if (isAdmin) {
                    res = dbcon.query("SELECT * FROM polices");
                } else {
                    res = dbcon.query("SELECT * FROM polices WHERE reporter LIKE \"%" + requester + "%\"");
                }

                int count = 0;
                while (res.next()) {
                    String reporter = res.getString("reporter");
                    String reported = res.getString("reported");
                    String reporter_nick = sidToNick.getNick(game, reporter);
                    String reported_nick = sidToNick.getNick(game, reported);
                    String reason = res.getString("reason");
                    String processed = ((Boolean) res.getBoolean("processed")).toString();
                    String reported_at = res.getTimestamp("reported_at").toString();
                    String id = (Integer.toString(res.getInt("id")));

                    resStr += "신고자: " + reporter + "(" + reporter_nick + ")\n";
                    resStr += "대상자: " + reported + "(" + reported_nick + ")\n";
                    resStr += "신고사유: " + reason + "\n";
                    resStr += "처리상태: " + processed + "\n";
                    resStr += "신고시각: " + reported_at + "\n";
                    resStr += "신고번호: " + id + "\n";
                    resStr += "\n\n";
                    count++;
                }
                if (count == 0) {
                    resStr = "접수된 신고가 한 건도 없습니다.";
                }

                out.print(resStr);
            }

            if (action.equals("cancelPolice")) {
                String id = request.getParameter("id");
                dbcon.queryNoResult("DELETE FROM polices WHERE id = " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbcon.close();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("GET REQUESTS ARE NOT ALLOWED...");
    }
}
