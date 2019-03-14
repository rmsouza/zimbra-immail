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

API implementation for Zimbra -> imMail..

signOn
https://zimbradev/service/extension/rocket?action=signOn
This URL needs to be configured in Rocket Chat under
Administration->Accounts->Iframe->API URL
If the user is logged into Zimbra this
url will return an auth token to Rocket to let the user login.

redirect
https://zimbradev/service/extension/rocket?action=redirect
This URL needs to be configured in Rocket Chat under
Administration->Accounts->Iframe->Iframe URL

This implementation uses HTTP GET
Administration->Accounts->Iframe->Api Method = GET

Before enabling the IFrame auth on Rocket, make sure to
create an Admin account first (usually this is done at initial
setup). You can use these credentials to debug if needed,
but they are also needed for Zimbra to perform its actions.

After enabling IFrame auth, you can no longer log-in via
the Rocket login page. You can either follow the steps here
to promote an account created by Zimbra to have an admin role.
https://rocket.chat/docs/administrator-guides/restoring-an-admin/
OR
You can create your own account first in Rocket before enabling
the integration and promote that to admin. Aka user.example.com.

Otherwise you may lock yourself out.


You can debug using the following commands:

createUser
Create a new user in Zimbra, make sure to set givenName and sn,
log in as that user in Zimbra, open a tab and point it to:
https://yourzimbra/service/extension/rocket?action=createUser
Creates users in Rocket chat based on their Zimbra account name.
The Zimbra Account name is mapped to the username in Rocket Chat.
(replacing @ with . so admin@example.com in Zimbra becomes
admin.example.com in Rocket)

Furthermore the users Zimbra givenName and sn (surname) are
concatenated to the Name in Rocket. Email is email in both
systems

If it returns 500, perhaps the account already exists or your admin
credentials configured in config.properties are wrong. Or the email
address is already configured on Rocket.

Try debug further with curl:

#Get admin auth token
curl https://beta.rocket.org:443/api/v1/login -d "username=adminUsername&password=adminPassword"
#Copy paste from the output

#create a user
curl -H "X-Auth-Token: from above command" \
     -H "X-User-Id: from above command" \
     -H "Content-type:application/json" \
     https://beta.rocket.org/api/v1/users.create \
     -d '{"name": "My User Name", "email": "exampleuser@zimbra.com", "password": "dfbgdyE%^&456645", "username": "exampleuser.zimbra.com"}'


Create a user token (used for login)
curl -H "X-Auth-Token: from above command" \
     -H "X-User-Id: from above command" \
     -H "Content-type:application/json" \
     https://beta.rocket.org:443/api/v1/users.createToken \
     -d '{ "username": "exampleuser.zimbra.com" }'

This command will tell you why the creation of a user failed.
*/

package br.com.immail;


import com.zimbra.cs.extension.ExtensionHttpHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setHeader("Content-Type", "text/html");
        resp.getWriter().write("<html><head></head><body><div style=\"background-color:white;color:black;padding:10px\">Please <a target=\"_blank\" href=''>Log in</a>.</div></body>");
        resp.getWriter().flush();
        resp.getWriter().close();
    }

}
