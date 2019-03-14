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
  this._simpleAppName = this.createApp("imMail App", "zimbraIcon", "imMail - Professional messaging app");

  ZimbraImmailZimlet.prototype.testRequest();
};

/**
* This method gets called by the Zimlet framework when the application is opened for the first time.
*
* @param	{String}	appName		the application name
*/
ZimbraImmailZimlet.prototype.appLaunch = function(appName) {
  switch (appName) {
    case this._simpleAppName: {
      // do something
      var app = appCtxt.getApp(appName); // get access to ZmZimletApp
      var iframeURL = this._zimletContext.getConfig('iframeURL');
      app.setContent("<iframe id=\"tabiframe-app\" name=\"tabiframe-app\" src=\"" + iframeURL + "\" width=\"100%\" height=\"100%\" /></iframe>"); // write HTML to app
      break;
    }
  }
};

ZimbraImmailZimlet.prototype.testRequest = function()
{
   var zimletInstance = appCtxt._zimletMgr.getZimletByName('br_com_immail').handlerObject;
   try{
      var xhr = new XMLHttpRequest();
      xhr.open('GET', '/service/extension/immail');

      xhr.onerror = function (err) {
         console.log(err);
      };

      xhr.send();
      xhr.onreadystatechange = function (oEvent)
      {
         console.log('xhr');
         if (xhr.readyState === 4)
         {
            console.log(xhr.status);
            console.log(xhr.response);
         }
      }
   } catch (err) {
      console.log(err);
   }
};
