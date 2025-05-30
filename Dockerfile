FROM tomcat:11-jdk21

COPY app/build/libs/IRCTC-1.0.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080