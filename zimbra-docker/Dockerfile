FROM jorgedlcruz/zimbra

RUN apt-get update -y
RUN apt-get install -y software-properties-common
RUN apt-get install -y language-pack-en-base
RUN export LC_ALL=en_US.UTF-8 && export LANG=en_US.UTF-8

RUN apt-get update -y && apt-get install -y iputils-ping
