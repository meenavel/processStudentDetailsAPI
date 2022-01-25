FROM openjdk:8-jdk-alpine
COPY ./target/processStudentDetails-0.0.1-SNAPSHOT.jar processStudentDetails-0.0.1-SNAPSHOT.jar
#ENTRYPOINT ["java","-jar", "processStudentDetails-0.0.1-SNAPSHOT.jar"]
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=dev", "processStudentDetails-0.0.1-SNAPSHOT.jar"]
