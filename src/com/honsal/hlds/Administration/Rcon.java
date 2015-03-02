package com.honsal.hlds.Administration;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.honsal.hlds.Administration.tools.RconSend;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by I on 2015-02-16.
 */
public class Rcon extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        String ipdomain = request.getParameter("ipdomain");
        String _port = request.getParameter("port");
        Integer port = null;
        String password = request.getParameter("password");

        String rcon_string = request.getParameter("rcon");

        if (rcon_string == null || rcon_string.equals("")) {
            response.getWriter().println("!@RCON 입력 없음@!");
        }

        if (ipdomain == null)
            ipdomain = "192.168.0.8";

        if (_port == null)
            _port = "27015";
        port = Integer.parseInt(_port);

        if (password == null)
            password = "hsljm";

        RconSend server = new RconSend(ipdomain, port, password);
        if (server.connect()) {
            try {
                response.getWriter().println(server.exec(rcon_string));
            } catch (SteamCondenserException e) {
                response.getWriter().println("!@RCON 시도 중 에러가 발생했습니다.@!");
            }
        } else {
            response.getWriter().println("!@서버가 닫혀있거나 RCON 모듈에 문제가 발생했습니다.@!");
        }
    }
}
