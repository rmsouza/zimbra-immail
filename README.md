# Zimbra and imMail integration

# Installation

  git clone https://github.com/myinfoshare-user/zimbra-immail.git
  nano zimbra-immail/zimlet/config_template.xml
  cd zimbra-immail/zimlet
  zip br_com_immail.zip *
  su zimbra
  zmzimletctl deploy br_com_immail.zip
  zmprov mc default +zimbraProxyAllowedDomains *.immail.com.br
