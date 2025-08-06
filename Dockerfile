## Создание виртуальной машины / контейнера / описание создания docker image
FROM openjdk:17-jdk-slim as MyProjectBuilder
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw ./
COPY pom.xml ./
RUN ./mvnw dependency:go-offline -B
COPY src/ ./src
RUN ./mvnw clean package -DskipTests
## одноразовый image MyProjectBuilder - будет уничтожен поле первого использования
## Он содержит в себе все зависимости, полную версию java, на которой был написан проект \
## а также компиляторы и прочее


## Запускаем интерпретатор java
FROM openjdk:17-jre
WORKDIR /app
COPY --from=MyProjectBuilder /app/target/backend-0.0.1-SNAPSHOT.jar myapp.jar
EXPOSE 8081
CMD["java","-jar","myapp.jar"]
