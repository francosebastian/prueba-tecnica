FROM amazoncorretto:17-alpine AS corretto-deps

#Install latest patches
RUN apk -i update && apk -i upgrade
RUN rm -rf /var/cache/apk/*

COPY build/libs/*.jar /usr/app/app.jar

RUN unzip /usr/app/app.jar -d temp &&  \
    jdeps  \
      --print-module-deps \
      --ignore-missing-deps \
      --recursive \
      --multi-release 17 \
      --class-path="./temp/BOOT-INF/lib/*" \
      --module-path="./temp/BOOT-INF/lib/*" \
      /usr/app/app.jar > /modules.txt

FROM amazoncorretto:17-alpine AS corretto-jdk

COPY --from=corretto-deps /modules.txt /modules.txt

# hadolint ignore=DL3018,SC2046
RUN apk add --no-cache binutils && \
    jlink \
     --verbose \
     --add-modules "$(cat /modules.txt),jdk.crypto.ec,jdk.crypto.cryptoki" \
     --strip-debug \
     --no-man-pages \
     --no-header-files \
     --compress=2 \
     --output /jre

# hadolint ignore=DL3007
FROM alpine:3.21.2
ENV JAVA_HOME=/jre
ENV PATH="${JAVA_HOME}/bin:${PATH}"

COPY --from=corretto-jdk /jre $JAVA_HOME

EXPOSE 8081
COPY build/libs/*.jar /usr/app/app.jar

WORKDIR /usr/app/

# fix trivy vulnerabilities
RUN apk add --upgrade libcrypto3 libssl3  && apk cache clean

CMD  ["java", "-jar", "app.jar","-Xms512m","-Xmx2g"]


