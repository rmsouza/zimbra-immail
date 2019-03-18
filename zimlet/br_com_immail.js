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
  var zimletInstance = appCtxt._zimletMgr.getZimletByName('br_com_immail').handlerObject;
  zimletInstance.iframeURL = zimletInstance._zimletContext.getConfig("iframeURL");
  zimletInstance.appName = zimletInstance._zimletContext.getConfig("appName");
  zimletInstance.appDescription = zimletInstance._zimletContext.getConfig("appDescription");

  zimletInstance.immailAuthToken = ZimbraImmailZimlet.prototype.sso();

  ZimbraImmailZimlet.prototype.loadIframe();

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
  try {
    var xhr = new XMLHttpRequest();
    xhr.open('GET', '/service/extension/immail?action=signOn');

    xhr.onerror = function (err) {
      console.log(err);
      return false;
    };

    xhr.send();
    xhr.onreadystatechange = function (oEvent) {
      if (xhr.readyState === 4) {
        if (xhr.status === 200) {
          var response = JSON.parse(xhr.response);
          console.log(response.token);
          return response.token;
        }
      }
    }
  } catch (err) {
    console.log(err);
    return false;
  }
};

ZimbraImmailZimlet.prototype.loadIframe = function() {

  try {
    var zimletInstance = appCtxt._zimletMgr.getZimletByName('br_com_immail').handlerObject;
    var iframeURL = zimletInstance.iframeURL;
    var token = zimletInstance.immailAuthToken;

    zimletInstance.ZimbraImmailApp = zimletInstance.createApp(zimletInstance.appName, "", zimletInstance.appDescription);
    var app = appCtxt.getApp(zimletInstance.ZimbraImmailApp);

    // var appPosition = document.getElementById('skin_container_app_new_button').getBoundingClientRect();
    // app.setContent('<div style="position: fixed; top:'+appPosition.y+'px; left:0; width:100%; height:92%; border:0px;"><iframe id="ZimbraRocketFrame" style="z-index:2; left:0; width:100%; height:100%; border:0px;" src=\"'+zimletInstance._zimletContext.getConfig("rocketurl")+'\"></div>');

    app.setContent("<iframe id=\"tabiframe-app\" name=\"tabiframe-app\" src=\"" + iframeURL + "?token=" + token + "\" width=\"100%\" height=\"100%\" /></iframe>"); // write HTML to app

  } catch (err) { console.log (err)}

};
