FROM infotekies/base-passwordgen
ARG JAR_FILES=target/*.jar
ARG PASSWORD_SERVICE=passwdservice
ARG CONFIG_SRC_FILES=src/main/resources/*
ARG INSTALL_ROOT=/home/passwordgen
ARG LIB_FOLDER=${INSTALL_ROOT}/lib
ARG CONF_FOLDER=${INSTALL_ROOT}/config
ARG TOMCAT_LOGS_FOLDER=${INSTALL_ROOT}/tomcat/logs
RUN mkdir -p ${LIB_FOLDER}
RUN mkdir -p ${CONF_FOLDER}
RUN mkdir -p ${TOMCAT_LOGS_FOLDER}
COPY ${JAR_FILES} ${LIB_FOLDER}
COPY ./docker/scripts/${PASSWORD_SERVICE} ${INSTALL_ROOT}/
COPY ${CONFIG_SRC_FILES} ${CONF_FOLDER}
EXPOSE 8080
WORKDIR    ${INSTALL_ROOT}
ENTRYPOINT [ "/home/passwordgen/passwdservice" ]
