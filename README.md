# Zimbra and imMail integration

# Installation

Estando no servidor onde o Zimbra encontra-se instalado:

```
git clone https://github.com/myinfoshare-user/zimbra-immail.git

nano zimbra-immail/zimlet/config_template.xml

cd zimbra-immail/zimlet

zip br_com_immail.zip *

su zimbra

zmzimletctl deploy br_com_immail.zip

zmprov mc default +zimbraProxyAllowedDomains *.immail.com.br
```

  ## After update the zimlet may be you have to clean the cache
`
 zmprov flushCache zimlet
`
