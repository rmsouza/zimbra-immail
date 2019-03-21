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

  ZimbraImmailZimlet.prototype.sso();

};

/**
* This method gets called by the Zimlet framework when the application is opened for the first time.
*
* @param	{String}	appName		the application name
*/
ZimbraImmailZimlet.prototype.appLaunch = function(appName) {
  console.log('opened for the first time.. reloading iframe');
};

ZimbraImmailZimlet.prototype.sso = function() {
  try {
    var zimletInstance = appCtxt._zimletMgr.getZimletByName('br_com_immail').handlerObject;
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
          zimletInstance.immailAuthToken = response.token;
          ZimbraImmailZimlet.prototype.loadIframe();

          return true;
        }

        ZimbraImmailZimlet.prototype.setEventListeners();

        ZimbraImmailZimlet.prototype.loadIframe();
      }
    }
  } catch (err) {
    console.log(err);
    return false;
  }
};

ZimbraImmailZimlet.prototype.loadIframe = function() {
  console.log('load iframe....');
  try {
    var zimletInstance = appCtxt._zimletMgr.getZimletByName('br_com_immail').handlerObject;

    if (zimletInstance.immailAuthToken) {
      var token = zimletInstance.immailAuthToken;
      var iframeURL = zimletInstance.iframeURL + '?token=' + token;
    } else {
      var iframeURL = zimletInstance.iframeURL;
    }

    zimletInstance.ZimbraImmailApp = zimletInstance.createApp(zimletInstance.appName, "", zimletInstance.appDescription);
    var app = appCtxt.getApp(zimletInstance.ZimbraImmailApp);

    var appPosition = document.getElementById('skin_container_app_new_button').getBoundingClientRect();
    app.setContent('<div style="position: fixed; top:'+appPosition.y+'px; left:0; width:100%; height: calc(100vh - 71px); border:0px;"><iframe id="ZimbraImmailFrame" style="z-index:2; left:0; width:100%; height:100%; border:0px;" src=\"'+ iframeURL +'\"></div>');

  } catch (err) { console.log (err)}

};

ZimbraImmailZimlet.prototype.appActive = function(appName, active) {
  var zimletInstance = appCtxt._zimletMgr.getZimletByName('br_com_immail').handlerObject;

  if (active) {
    console.log('app active');

    document.title = 'Zimbra: ' + 'imMail';
    //In the Zimbra tab hide the left menu bar that is displayed by default in Zimbra, also hide the mini calendar
    document.getElementById('z_sash').style.display = "none";
    //Users that click the tab directly after logging in, will still be served with the calendar, as it is normal
    //it takes some time to be displayed, so if that occurs, try to remove the calender again after 10 seconds.
    try {
      var cal = document.getElementsByClassName("DwtCalendar");
      var chat = document.getElementsByClassName("DwtBaseDialog");
      cal[0].style.display = "none";
      chat[0].style.display = "none";
    } catch (err) {
      setTimeout(
        function(){
          try {
            var cal = document.getElementsByClassName("DwtCalendar");
            var chat = document.getElementsByClassName("DwtBaseDialog");
            cal[0].style.display = "none";
            chat[0].style.display = "none";
          }
          catch(err){}
        }, 5000);
      }

      var app = appCtxt.getApp(zimletInstance.ZimbraImmailApp);
      var overview = app.getOverview(); // returns ZmOverview
      overview.setContent("&nbsp;");
      try {
        var child = document.getElementById(overview._htmlElId);
        child.parentNode.removeChild(child);
      } catch(err) {
        // already gone
      }
    } else {
      console.log('app inactive');

      document.getElementById('z_sash').style.display = "block";
      try {
        var cal = document.getElementsByClassName("DwtCalendar");
        var chat = document.getElementsByClassName("DwtBaseDialog");
        cal[0].style.display = "block";
        chat[0].style.display = "block";
      } catch (err) { }
    }
  };

ZimbraImmailZimlet.prototype.setEventListeners = function() {
  var zimletInstance = appCtxt._zimletMgr.getZimletByName('br_com_immail').handlerObject;
  window.addEventListener('message', receiveMessage, false);

  function receiveMessage(event) {
    if (event.origin === zimletInstance.iframeURL) {
      console.log('orgin match', zimletInstance.iframeURL);
    } else {
      console.log('origin not match', zimletInstance.iframeURL + ' - ' + event.origin);
    }

    switch (event.data.type) {
      case 'unread-messages':
        if (event.data.count > 0) {
          var label = zimletInstance.appName + '(' + event.data.count + ')';
        } else {
          var label = zimletInstance.appName;
        }

        ZimbraImmailZimlet.prototype.setTabLabel(label);
        break;
      default:
        console.log('unknown event');
    }
  }
}

ZimbraImmailZimlet.prototype.setTabLabel = function(label) {
  var controller = appCtxt.getAppController();
  var appChooser = controller.getAppChooser();

  // change the tab label and tool tip
  var appButton = appChooser.getButton(this._tabAppName); // returns ZmAppButton
  appButton.setText(label);
  // appButton.setToolTipContent("NEW TAB TOOL TIP");
};
