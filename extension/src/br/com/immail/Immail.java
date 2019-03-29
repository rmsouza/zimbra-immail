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
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
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

    private String immailApiKey;
    private String immailURL;
    private String immailCreateTokenPath;

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

        final Map<String, String> paramsMap = new HashMap<String, String>();

        if (req.getQueryString() != null) {
            String[] params = req.getQueryString().split("&");
            for (String param : params) {
                String[] subParam = param.split("=");
                paramsMap.put(subParam[0], subParam[1]);
            }
        } else {
            responseWriter("ok", resp, null);
            return;
        }

        //Initializes immailApiKey, immailURL, immailCreateTokenPath on this instance
        if (this.initializeImmailAPI()) {
            switch (paramsMap.get("action")) {
                case "signOn":
                    String token;
                    token = this.createAuthToken(zimbraAccount.getName());
                    if (!"".equals(token)) {
                        resp.setHeader("Content-Type", "application/json");
                        responseWriter("ok", resp, "{\"token\":\""+token+"\"}");
                    } else {
                        responseWriter("error", resp, null);
                    }
                    break;
                case "test":
                    try {
                        JSONParser jsonParser = new JSONParser();
                        FileReader reader = new FileReader("/opt/zimbra/lib/ext/immail/config.domains.json");
                        Object obj = jsonParser.parse(reader);
                        JSONArray domainArray = (JSONArray) obj;
                        // JSONObject firstDomain = domainList.getJSONObject(0);

                        String domains = "";
                        for (int n = 0; n < domainArray.length(); n++) {
                            JSONObject domainConfig = domainArray.getJSONObject(n);
                            String domain = domainConfig.getString("domain");
                            String apiKey = domainConfig.getString("apiKey");

                            System.out.println("domain : " + domain);
                            System.out.println("apiKey : " + apiKey);

                            domains += domain;
                        }

                        responseWriter("ok", resp, domains);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        responseWriter("unauthorized", resp, null);
                        return;
                    }
                    break;

            }
        } else {
            responseWriter("error", resp, null);
        }

    }

    private void responseWriter(String action, HttpServletResponse resp, String message) {
        try {
            // this.initializeImmailAPI();
            resp.setHeader("Access-Control-Allow-Origin", this.immailURL);
            resp.setHeader("Access-Control-Allow-Credentials", "true");
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

    public Boolean initializeImmailAPI() {
        Properties prop = new Properties();
        try {
            FileInputStream input = new FileInputStream("/opt/zimbra/lib/ext/immail/config.properties");


            prop.load(input);
            this.immailApiKey = prop.getProperty("immailApiKey");
            this.immailURL = prop.getProperty("immailURL");
            this.immailCreateTokenPath = prop.getProperty("immailCreateTokenPath");
            input.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * In order to return the JWT Token of the user provided
     *
     * Implements: POST https://api.immail.com.br/auth/token
     * body: {
     *     email: USER_EMAIL
     * }
     * headers: {
     *     X-AUTH-TOKEN: IMMAIL_API_KEY
     * }
     */
    public String createAuthToken(String email) {
        HttpURLConnection connection = null;
        String inputLine;
        StringBuffer response = new StringBuffer();
        try {

            String urlParameters = "{ \"email\": \"" + email + "\" }";
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            URL url = new URL(this.immailURL + this.immailCreateTokenPath);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            connection.setRequestProperty("X-Auth-Token", this.immailApiKey);

            connection.setUseCaches(false);
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(postData);
            }

            if (connection.getResponseCode() == 200) {
                // get response stream
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                // feed response into the StringBuilder
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                // Start parsing
                JSONObject obj = new JSONObject(response.toString());
                String token = obj.getString("token");
                return token;

            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
