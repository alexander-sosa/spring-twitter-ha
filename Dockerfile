FROM azul/zulu-openjdk:11

COPY target/twitter-0.0.1-SNAPSHOT.jar /

ENTRYPOINT ["java", "-jar", "/twitter-0.0.1-SNAPSHOT.jar"]