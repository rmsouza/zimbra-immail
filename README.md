<h1 align="center">
  <img src="https://user-images.githubusercontent.com/279535/55269168-e72ae280-5266-11e9-9edb-d7298777d34d.png" alt="logo" height="70"/>
  <img src="https://user-images.githubusercontent.com/279535/54444172-8e146800-4718-11e9-97ea-33baf30bc164.png" alt="logo" height="70"/><br>

  Zimbra & imMail Integration
</h1>

Here you will find important instructions to install a Zimlet that makes possible the integration between Zimbra Collaboration Server and imMail (Instant Messenger for Companies), and make an amazing integration with both inside the Zimbra Web Client..

# Installation

## Single Sign-on Integration
This Zimlet supports optional single sign-on to imMail using the imMail API. That way you only need to maintain the user accounts on Zimbra. The account creation can be done via LDAP import or by the own user on imMail platform, but in both cases you still be logged on automatically (single sign-on integration). Or you can just deploy only the Zimlet and let the user decide on the authentication (basic integration).

> If you do not want to activate the Single Sign-on option,  skip this step and go straight to the Basic Integration.

**API KEY**

In order to allow the integration with imMail, will be necessary to generate an API KEY.

1. Go to www.immail.ca;

2. If the domain that you want to configure already has an admin account in imMail, log in. Otherwise, create a new account (the first account created for each domain always will be the user admin);

3. Click the menu button on the top side of the screen and choose the "Manage Domain" option;

4. Go to API KEY tab;
![image](https://user-images.githubusercontent.com/279535/71903033-388acb00-3131-11ea-8521-a96a868af0e8.png)

5. Click on "Gerar API KEY" button;

6. Copy the created API KEY and save in a safe place.

**Java extension configuration**

1. For Single Sign-on Integration you have to set-up the Java server extension. Download the extension.jar file and copy it to _/opt/zimbra/lib/ext/immail/extension.jar_

Probably you will need to create the _immail_ folder in _/opt/zimbra/lib/ext_. See the example below:

```
$ mkdir /opt/zimbra/lib/ext/immail
$ cp extension.jar /opt/zimbra/lib/ext/immail/extension.jar
```

2. Then create the _config.properties_ text file in _/opt/zimbra/lib/ext/immail/config.properties_ with the contents:

```
immailURL=https://api.immail.ca
immailCreateTokenPath=/auth/token
```

3. This Zimlet allows integration of multiple domains. In order to make it possible, it is necessary to create the file in _/opt/zimbra/lib/ext/immail/config.domains.json_ as the example below:

> Note that you have to set the apiKey and the correspondent domain. Each Api Key is unique and exclusive for each domain. If you have no Api Key, please, contact us via imMail App (support@immail.ca).

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

1. Download the ca_immail.zip_ file into the server.
2. Change to zimbra user.

```
su zimbra
```
3. Execute the deploy command:

```
zmzimletctl deploy ca_immail.zip
```
4. Grant permission to imMail domains.

```
zmprov mc default +zimbraProxyAllowedDomains *.immail.ca
```
5. Clean up the Zimlet cache.

After update the zimlet you may have to clean up the cache.

```
 zmprov flushCache zimlet
```
6. Make sure Zimlet is active for users.

## LDAP Integration
In order to keep synchronized the user data in both platforms,  Zimbra and imMail, the LDAP data should be configured on imMail.

1. Go to www.immail.ca;

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

# Development

The project has two most important directories "/zimlet" and "/extension".

**/zimlet** has all javascript, css, xml and image files that makes up the Zimlet.

**/extension** has a Java extension project which is important for the single signon feature. It's a IntelliJ project, so, would be a good idea to open the project by it.

## Build process

**Step 1**: Generate the .jar file.

Open the "extension" folder on IntelliJ, go to main menu and click on "Build", and then "Build Artifacts", and finally "Build".

**Step 2**: Update the iframeURL properties on /zimlet/config_template.xml with the imMail frondend url. For example: https://app.immail.ca (for production) or https://testapp.immail.com.br (for test environment).

**Step3**: Run .build.sh providing the current version of the Zimlet like below:
```
./build.sh 0.3.0
```

## Zimbra command might be usefull

**Show ldap data connection**
```
su zimbra
zmlocalconfig -s | grep zimbra_ldap_password
```

**Recovery root password**
```
su zimbra
zmlocalconfig -s | grep ldap_root_password
```

**Restart all Zimbra services**
```
su zimbra
zmcontrol restart
```

**Restart only mail service**
```
su zimbra
zmmailboxdctl restart
```

**Admin password reset**

> If you do not know about administrator email address, please check the next item.
```
su - zimbra
zmprov sp <admin email address> <new password>

```

**Obtain list of all administrators**
```
su - zimbra
zmprov gaaa
```

**Zimbra Installation**

[Click here](https://github.com/immail/zimbra-immail/tree/master/zimbra-docker) to follow the Zimbra installation using Docker.

**Zimbra connection test**

Install ldap-utils. If you want to know a little more about how to manage LDAP servers with OpenLDAP utils, please take a look this https://www.digitalocean.com/community/tutorials/how-to-manage-and-use-ldap-servers-with-openldap-utilities

```
sudo apt-get update
sudo apt-get install ldap-utils
```
Try to connect.
```
ldapsearch -H ldap://host:389 -D <DN> -b <BASE> -W -s sub "(objectclass=*)"

Ex.:
ldapsearch -H ldap://localhost:389 -D uid=zimbra,cn=admins,cn=zimbra -b "dc=immailtest,dc=com" -W -s sub "(objectclass=*)"
```
After this command the password will be requested.
