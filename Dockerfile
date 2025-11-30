#build
FROM maven:amazoncorretto as build
WORKDIR /build

COPY . .

RUN mvn clean package -DskipTests

#run
FROM amazoncorretto
WORKDIR /app

COPY --from=build ./build/target/*.jar ./library.jar

EXPOSE 8080
EXPOSE 9090

ENV DATASOURCE_URL=''
ENV DATASOURCE_USER=''
ENV DATASOURCE_PASSWORD=''
ENV GOOGLE_CLIENT_ID='client_id'
ENV GOOGLE_CLIENT_SECRET='client_id'

ENV SPRING_PROFILES_ACTIVE='production'
ENV TZ='America/Sao_Paulo'

ENTRYPOINT exec java -jar libraryapi.jar