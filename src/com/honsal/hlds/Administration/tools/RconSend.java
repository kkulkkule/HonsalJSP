package com.honsal.hlds.Administration.tools;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.servers.SourceServer;

import java.util.concurrent.TimeoutException;

/**
 * Created by I on 2015-02-16.
 */
public class RconSend {

    private SourceServer server = null;
    private String ipdomain = null;
    private Integer port = null;
    private String password = null;

    public RconSend(String _ipdomain, Integer _port, String _password) {
        try {
            ipdomain = _ipdomain;
            port = _port;
            password = _password;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean connect() {
        if (ipdomain == null || port == null || password == null) {
            return false;
        }

        try {
            server = new SourceServer(ipdomain, port);
            return true;
        } catch (SteamCondenserException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String exec(String rcon_exec) throws SteamCondenserException {
        if (password != null) {
            try {
                server.rconAuth(password);
                String executed = server.rconExec(rcon_exec);
                if (executed != null) {
                    return executed;
                }
            } catch (SteamCondenserException e) {
                e.printStackTrace();
                return "!@COULDN'T AUTHORIZATION@!";
            } catch (TimeoutException e) {
                e.printStackTrace();
                return "!@TIMEOUT@!";
            } catch (Exception e) {
                e.printStackTrace();
                return "!@UNKNOWN ERROR@!";
            }
        } else {
            return "!@WRONG PASSWORD@!";
        }
        return null;
    }
}
