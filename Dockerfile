FROM openjdk:11-jdk-slim

WORKDIR /app

COPY build/libs/zzimccong-0.0.1-SNAPSHOT.jar /app/zzimccong.jar

ENV SPRING_PROFILES_ACTIVE=local
ENV SPRING_DATASOURCE_URL=jdbc:mysql://database-zzimccong.cv0keg02ecgv.ap-northeast-3.rds.amazonaws.com:3306/zzimccong
ENV SPRING_DATASOURCE_USERNAME=admin
ENV SPRING_DATASOURCE_PASSWORD=qwer1234

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "/app/zzimccong.jar"]
