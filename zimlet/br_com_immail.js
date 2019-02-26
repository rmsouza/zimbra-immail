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

/**
* This method gets called by the Zimlet framework when the zimlet loads.
*/
br_com_immail_HandlerObject.prototype.init = function() {
  this._simpleAppName = this.createApp("imMail App", "zimbraIcon", "imMail - Professional messaging app");
};

/**
* This method gets called by the Zimlet framework when the application is opened for the first time.
*
* @param	{String}	appName		the application name
*/
br_com_immail_HandlerObject.prototype.appLaunch = function(appName) {
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
