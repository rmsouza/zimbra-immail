# Zimbra and imMail Integration
Here you will find important instructions to install a Zimlet that makes possible the integration between Zimbra Collaboration Server and imMail (Instant Messenger for Companies), and make an amazing integration with both inside the Zimbra Web Client.

# Installation

## Single signon integration
This Zimlet supports optional single sign-on to imMail using the imMail API. That way you only need to maintain the user accounts on Zimbra. The account creation can be done via LDAP import or by the own user on imMail platform, but in both of cases, you will still log them on automatically (single sign-on integration). Or you can just deploy only the Zimlet and let the user decide on the authentication (basic integration).

1. Clone o repositório do Zimlet.
```
git clone https://github.com/myinfoshare-user/zimbra-immail.git
```
2. Copiar arquivo extension.jar
```
mkdir /opt/zimbra/lib/ext/immail
cp zimbra-immail/extension/out/artifacts/extension_jar/extension.jar /opt/zimbra/lib/ext/immail/extension.jar
```
3. Crie um arquivo de configuração chamado "config.properties" como no exemplo abaixo:
```
# vi /opt/zimbra/lib/ext/immail/config.properties
```
Insira o seguinte conteúdo no arquivo:
```
immailApiKey=API_KEY (Fornecida pela imMail)
immailURL=https://api.immail.com.br
immailCreateTokenPath=/auth/token
```

```
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
