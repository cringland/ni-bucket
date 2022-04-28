FROM openjdk:11.0.15-slim

ENV WORKDIR /usr/src
ENV NAME ni-bucket
ENV JAR_NAME ${NAME}-0.0.1.jar

COPY ./target/${JAR_NAME} ${WORKDIR} 

EXPOSE 8080
WORKDIR ${WORKDIR}

ENTRYPOINT ["java", "-jar", "ni-bucket-0.0.1.jar"]
