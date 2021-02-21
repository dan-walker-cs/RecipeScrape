# sets up temporary gradle environment
FROM gradle:6.8.2-jdk11 AS TEMP_BUILD_IMAGE
ENV APP_HOME=/
WORKDIR $APP_HOME
COPY build.gradle settings.gradle $APP_HOME

# set permissions for gradle environment
COPY gradle $APP_HOME/gradle
COPY --chown=gradle:gradle . /home/gradle/src
USER root
RUN chown -R gradle /home/gradle/src

# build the project with gradle
run gradle --stacktrace  build || return 0
COPY . .
RUN gradle build clean

# actual container for application image
FROM adoptopenjdk/openjdk11:alpine-jre
ENV ARTIFACT_NAME=build/libs/*.jar
ENV APP_HOME=/usr/app/

# collect the .jar file from the temporary environment
WORKDIR $APP_HOME
COPY --from=TEMP_BUILD_IMAGE $APP_HOME/build/libs/$ARTIFACT_NAME .

# run the application
EXPOSE 8080
ENTRYPOINT exec java -jar ${ARTIFACT_NAME}