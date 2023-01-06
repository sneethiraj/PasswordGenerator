FROM ubuntu:lunar
ARG JAR_FILES=target/*.jar
ARG PASSWORD_SERVICE=passwdservice
ARG CONFIG_SRC_FILES=src/main/resources/*
ARG INSTALL_ROOT=/home/passwordgen
ARG LIB_FOLDER=${INSTALL_ROOT}/lib
ARG CONF_FOLDER=${INSTALL_ROOT}/config
RUN mkdir -p ${LIB_FOLDER}
RUN mkdir -p ${CONF_FOLDER}
COPY ${JAR_FILES} ${LIB_FOLDER}
COPY ${PASSWORD_SERVICE} ${INSTALL_ROOT}/
COPY ${CONFIG_SRC_FILES} ${CONF_FOLDER}
RUN apt-get update && apt-get -y install openjdk-8-jdk
EXPOSE 8080
ENTRYPOINT [ "/home/passwordgen/passwdservice" ]
