FROM openjdk:8
VOLUME /tmp
EXPOSE 8080
COPY src.py /home/carpeta
ARG JAR_FILE=target/atm.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

