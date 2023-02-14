FROM openjdk:17
EXPOSE 8090
ADD /target/deal-1.jar deal-1.jar
ENTRYPOINT ["java", "-jar", "deal-1.jar"]