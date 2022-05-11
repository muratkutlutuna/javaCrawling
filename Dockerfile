FROM gradle:4.7.0-jdk8-alpine AS build

WORKDIR .

COPY --chown=gradle:gradle . .

RUN gradle build --no-daemon

ENTRYPOINT ["gradle","build","run"]
