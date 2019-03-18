/**
This file is part of the imMail Zimlet
Copyright (C) 2015-2019  Rafael Menezes

Bugs and feedback: support@immail.com.br

**/

/**
* Defines the Zimlet handler class.
*/
function br_com_immail_HandlerObject() {
}

/**
* Makes the Zimlet class a subclass of ZmZimletBase.
*/
br_com_immail_HandlerObject.prototype = new ZmZimletBase();
br_com_immail_HandlerObject.prototype.constructor = br_com_immail_HandlerObject;
var ZimbraImmailZimlet = br_com_immail_HandlerObject;

/**
* This method gets called by the Zimlet framework when the zimlet loads.
*/
ZimbraImmailZimlet.prototype.init = function() {
  this._simpleAppName = this.createApp(zimletInstance._zimletContext.getConfig("appName"), "zimbraIcon", zimletInstance._zimletContext.getConfig("appDescription"));

  var app = appCtxt.getApp(appName); // get access to ZmZimletApp
  var zimletInstance = appCtxt._zimletMgr.getZimletByName('br_com_immail').handlerObject;
  zimletInstance.iframeURL = zimletInstance._zimletContext.getConfig("iframeURL");

  if (ZimbraImmailZimlet.prototype.sso()) {
    ZimbraImmailZimlet.prototype.loadIframe();
  }

};

/**
* This method gets called by the Zimlet framework when the application is opened for the first time.
*
* @param	{String}	appName		the application name
*/
ZimbraImmailZimlet.prototype.appLaunch = function(appName) {
  console.log('opened for the first time');
  switch (appName) {
    case this._simpleAppName: {

      break;
    }
  }
};

ZimbraImmailZimlet.prototype.sso = function() {
  var zimletInstance = appCtxt._zimletMgr.getZimletByName('br_com_immail').handlerObject;

  try {
    var xhr = new XMLHttpRequest();
    xhr.open('GET', '/service/extension/immail');

    xhr.onerror = function (err) {
      console.log(err);
      return false;
    };

    xhr.send();
    xhr.onreadystatechange = function (oEvent) {
      if (xhr.readyState === 4) {
        if (xhr.status === 200) {
          console.log(xhr);
          console.log(xhr.response);
          zimletInstance.token = xhr.response.token;
          return true;
        }
      }
    }
  } catch (err) {
    console.log(err);
    return false;
  }
};

ZimbraImmailZimlet.prototype.loadIframe = function() {
  var zimletInstance = appCtxt._zimletMgr.getZimletByName('br_com_immail').handlerObject;
  var iframeURL = zimletInstance.iframeURL;
  var token = zimletInstance.token;
  app.setContent("<iframe id=\"tabiframe-app\" name=\"tabiframe-app\" src=\"" + iframeURL + "?token=" + token + "\" width=\"100%\" height=\"100%\" /></iframe>"); // write HTML to app
};
