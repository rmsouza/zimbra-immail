# Zimbra and imMail integration

# Installation

Estando no servidor onde o Zimbra encontra-se instalado:

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
