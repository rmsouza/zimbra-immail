<h1 align="center">
  <img src="https://user-images.githubusercontent.com/279535/55269168-e72ae280-5266-11e9-9edb-d7298777d34d.png" alt="logo" height="70"/>
  <img src="https://user-images.githubusercontent.com/279535/54444172-8e146800-4718-11e9-97ea-33baf30bc164.png" alt="logo" height="70"/><br>

  Zimbra & imMail Integration
</h1>

Here you will find important instructions to install a Zimlet that makes possible the integration between Zimbra Collaboration Server and imMail (Instant Messenger for Companies), and make an amazing integration with both inside the Zimbra Web Client.

# Installation

## Single Sign-on Integration
This Zimlet supports optional single sign-on to imMail using the imMail API. That way you only need to maintain the user accounts on Zimbra. The account creation can be done via LDAP import or by the own user on imMail platform, but in both of cases, you will still log them on automatically (single sign-on integration). Or you can just deploy only the Zimlet and let the user decide on the authentication (basic integration).

> If you do not want to activate the Single Sign-on option,  skip this step and go straight to the Basic Integration.

1. For Single Sign-on Integration you have to set-up the Java server extension. Download the extension.jar file and copy it to _/opt/zimbra/lib/ext/immail/extension.jar_

Probably you will need to create the _immail_ folder in _/opt/zimbra/lib/ext_. See the example below:

```
$ mkdir /opt/zimbra/lib/ext/immail
$ cp extension.jar /opt/zimbra/lib/ext/immail/extension.jar
```

2. Then create the _config.properties_ text file in _/opt/zimbra/lib/ext/immail/config.properties_ with the contents:

```
immailURL=https://api.immail.com.br
immailCreateTokenPath=/auth/token
```

3. This Zimlet allows integration of multiple domains. In order to make it possible, it is necessary to create the file in _/opt/zimbra/lib/ext/immail/config.domains.json_ as the example below:

> Note that you have to set the apiKey and the correspondent domain. Each Api Key is unique and exclusive for each domain. If you have no Api Key, please, contact us via imMail App (support@immail.com.br).

```
[
 {
  "domain": "companyxpto.com",
  "apiKey": "fbg3QZA9vZS9KCB62kb2E3mVWZu6Ez67"
 },
 {
  "domain": "companyxyz.com",
  "apiKey": "6SbkuQa9h6Te5N9yxJsGaPnWGFhXk2an"
 }
]

```

4. Restart the service as follows:

```
$ zmmailboxdctl restart
```

## Basic Integration

1. Download the _br_com_immail.zip_ file into the server.
2. Change to zimbra user.

```
su zimbra
```
3. Execute the deploy command:

```
zmzimletctl deploy br_com_immail.zip
```
4. Grant permission to imMail domains.

```
zmprov mc default +zimbraProxyAllowedDomains *.immail.com.br
```
5. Clean up the Zimlet cache.

After update the zimlet you may have to clean up the cache.

```
 zmprov flushCache zimlet
```
6. Make sure Zimlet is active for users.

## LDAP Integration
In order to keep synchronized the user data in both platforms,  Zimbra and imMail, the LDAP data should be configured on imMail.

1. Go to www.immail.com.br;

2. If the domain that you want to configure already has an admin account in imMail, log in. Otherwise create a new account (the first account created for each domain always will be the user admin);

3. Click the menu button on the top side of the screen and choose the "Manage Domain" option;

4. Go to AD/LDAP integration tab and fill in the LDAP data. See the example below:

![image](https://user-images.githubusercontent.com/279535/55256223-ef692a80-5232-11e9-9de1-b6f989825077.png)

> Note that you might create an LDAP search filter.  If you provide "Base" and "Filter" data, as the picture below, you'll be able to import all users from the current domain.

5. Click on "Save";

5. After that, click on "Sync AD/LDAP" in order to import the Zimbra users.

## Enjoy it!

## Important Notes

**No information related do LDAP will be presented into the imMail tab.**

If someone has a problem like this on Chrome, the browser can be blocking third-party cookies.

To find the setting option screen/windows, open Chrome settings, type "content" in the search box, click the Content Settings option, then click the Cookies option and uncheck the option that says something like this "Block third-party cookies and site data".

Click [here](https://www.chromium.org/for-testers/bug-reporting-guidelines/uncaught-securityerror-failed-to-read-the-localstorage-property-from-window-access-is-denied-for-this-document) for more info.
