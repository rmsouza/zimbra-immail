/*

Copyright (C) 2018  Barry de Graaff

The MIT License

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

API implementation for Zimbra -> imMail...

*/

package br.com.immail;


import com.zimbra.cs.account.Account;
import com.zimbra.cs.account.AuthToken;
import com.zimbra.cs.account.Cos;
import com.zimbra.cs.account.Provisioning;
import com.zimbra.cs.extension.ExtensionHttpHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class Immail extends ExtensionHttpHandler {

    /**
     * The path under which the handler is registered for an extension.
     *
     * @return path
     */
    @Override
    public String getPath() {
        return "/immail";
    }

    private String adminAuthToken;
    private String adminUserId;
    private String adminUserName;
    private String adminPassword;
    private String rocketURL;
    private String loginurl;

    /**
     * Processes HTTP POST requests.
     *
     * @param req  request message
     * @param resp response message
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.getOutputStream().print("br.com.immail is installed. HTTP POST method is not supported");
    }

    /**
     * Processes HTTP GET requests.
     *
     * @param req  request message
     * @param resp response message
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String authTokenStr = null;
        Account zimbraAccount = null;
        //Just read a cos value to see if its a valid user
        try {
            Cookie[] cookies = req.getCookies();
            for (int n = 0; n < cookies.length; n++) {
                Cookie cookie = cookies[n];

                if (cookie.getName().equals("ZM_AUTH_TOKEN")) {
                    authTokenStr = cookie.getValue();
                    break;
                }
            }

            if (authTokenStr != null) {
                AuthToken authToken = AuthToken.getAuthToken(authTokenStr);
                Provisioning prov = Provisioning.getInstance();
                zimbraAccount = Provisioning.getInstance().getAccountById(authToken.getAccountId());
                Cos cos = prov.getCOS(zimbraAccount);
                Set<String> allowedDomains = cos.getMultiAttrSet(Provisioning.A_zimbraProxyAllowedDomains);
            } else {
                responseWriter("unauthorized", resp, null);
                return;
            }

        } catch (Exception ex) {
            //crafted cookie? get out you.
            ex.printStackTrace();
            responseWriter("unauthorized", resp, null);
            return;
        }

        String name = zimbraAccount.getName();
        responseWriter("ok", resp, name);
        return;

    }

    private void responseWriter(String action, HttpServletResponse resp, String message) {
        try {
            // this.initializeRocketAPI();
            // resp.setHeader("Access-Control-Allow-Origin", this.rocketURL);
            // resp.setHeader("Access-Control-Allow-Credentials", "true");
            switch (action) {
                case "ok":
                    resp.setStatus(HttpServletResponse.SC_OK);
                    if (message == null) {
                        resp.getWriter().write("OK");
                    } else {
                        resp.getWriter().write(message);
                    }
                    break;
                case "unauthorized":
                    resp.setHeader("Content-Type", "text/html");
                    resp.getWriter().write("<html><head></head><body><div style=\"background-color:white;color:black;padding:10px\">Unauthorized. Let's do something.</div></body>");
                    break;
                case "error":
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().write("The request did not succeed successfully.");
                    break;
            }
            resp.getWriter().flush();
            resp.getWriter().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
