[![](https://images.microbadger.com/badges/image/jorgedlcruz/zimbra.svg)](https://microbadger.com/images/jorgedlcruz/zimbra "Get your own image badge on microbadger.com")
[![](https://images.microbadger.com/badges/version/jorgedlcruz/zimbra.svg)](https://microbadger.com/images/jorgedlcruz/zimbra "Get your own version badge on microbadger.com")

# Zimbra
In this Repository you will find how to install Zimbra on Docker

## Build the docker image
The first step is to build the docker zimbra image
```bash
docker build -t immail/zimbra-docker .
```

## Creating Zimbra Containers
Now that we have an image called immail/zimbra-docker, we can do a docker run with some special parameters, like this:
```bash
docker run -p 25:25 -p 80:80 -p 465:465 -p 587:587 -p 110:110 -p 143:143 -p 993:993 -p 995:995 -p 443:443 -p 8080:8080 -p 8443:8443 -p 7071:7071 -p 9071:9071 -h zimbra-docker.zimbra.io --dns 127.0.0.1 --dns 8.8.8.8 -i -t -e PASSWORD=Zimbra2017 immail/zimbra-docker
```
As you can see we tell the container the ports we want to expose, and on which port, we also specify the container hostname, the password foir the Zimbra Administrator Account, and the image to use.

That's it! You can visit now the IP of your Docker Machine using HTTPS, or try the Admin Console with HTTPS and port 7071.

## Known issues

After the Zimbra automated installation, if you close or quit the bash console from the container, the docker container might exit and keep in stopped state, you just need to run the next commands to start your Zimbra Container:

```bash
docker ps -a 
docker start YOURCONTAINERID
docker exec -it YOURCONTAINERID bash
su - zimbra
zmcontrol restart
```

## Distributed under MIT license
Copyright (c) 2017 Jorge de la Cruz

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
