FROM ubuntu:22.04

ARG LIB_DIR="/usr/lib"
ARG JVM_DIR=${LIB_DIR}"/jvm"
ARG KOTLIN_DIR_COMMON=${LIB_DIR}"/kotlin"
ARG KOTLIN_DIR=${KOTLIN_DIR_COMMON}"/kotlinc"
ARG KTLINT_DIR=${KOTLIN_DIR_COMMON}"/lint"
ARG BACKEND_DIR="/home/runner/work/ebiznes2022-ex5/ebiznes2022-ex5"

RUN apt-get update \
    && apt-get install -y apt-utils sudo wget -f
RUN echo 'debconf debconf/frontend select Noninteractive' | debconf-set-selections \
    && apt-get upgrade -y \
    && apt-get dist-upgrade -y
RUN useradd -m user \
    && usermod -aG sudo user \
    && passwd -d user
USER user

WORKDIR /home/user

RUN sudo apt-get install -y curl unzip zsh gradle

SHELL ["/bin/zsh", "-c"]
RUN chsh -s /bin/zsh user
RUN touch ~/.zshrc

RUN [ ! -d ${JVM_DIR} ] && sudo mkdir ${JVM_DIR} || exit 0
RUN [ ! -d ${KOTLIN_DIR_COMMON} ] && sudo mkdir ${KOTLIN_DIR_COMMON} || exit 0
RUN [ ! -d ${KOTLIN_DIR} ] && sudo mkdir ${KOTLIN_DIR} || exit 0
RUN [ ! -d ${KTLINT_DIR} ] && sudo mkdir ${KTLINT_DIR} || exit 0

RUN wget https://download.java.net/java/GA/jdk17.0.2/dfd4a8d0985749f896bed50d7138ee7f/8/GPL/openjdk-17.0.2_linux-x64_bin.tar.gz \
    && tar xvf openjdk-17.0.2_linux-x64_bin.tar.gz \
    && sudo mv jdk-17.0.2 ${JVM_DIR}

RUN wget https://github.com/JetBrains/kotlin/releases/download/v1.6.10/kotlin-compiler-1.6.10.zip \
    && unzip kotlin-compiler-1.6.10.zip \
    && sudo mv kotlinc ${KOTLIN_DIR_COMMON}

RUN wget https://github.com/pinterest/ktlint/releases/download/0.45.1/ktlint \
    && chmod a+x ktlint \
    && sudo mv ktlint ${KTLINT_DIR}

ENV JAVA_HOME=${JVM_DIR}"/jdk-17.0.2"
ENV PATH=$JAVA_HOME/bin:$PATH
ENV PATH=$PATH:${KOTLIN_DIR}"/bin":${KTLINT_DIR}

RUN mkdir "data"

VOLUME [ "data" ]

RUN echo "exec zsh" >> .bashrc \
    && source .bashrc

RUN [ ! -d ${JVM_DIR} ] && sudo mkdir ${BACKEND_DIR} || exit 0

WORKDIR $BACKEND_DIR

RUN echo "#/bin/zsh/\nGOOGLE_CLIENT_ID=${GOOGLE_CLIENT_ID} GOOGLE_CLIENT_SECRET=${GOOGLE_CLIENT_SECRET}  \
    GITHUB_CLIENT_ID=${GITHUB_CLIENT_ID} GITHUB_CLIENT_SECRET=${GITHUB_CLIENT_SECRET} DATABASE_URL=${DATABASE_URL}  \
    DATABASE_USERNAME=${DATABASE_USERNAME} DATABASE_PASSWORD=${DATABASE_PASSWORD} ./gradlew run" > /entrypoint.sh

ENTRYPOINT ["/bin/zsh", "/entrypoint.sh"]

EXPOSE 3000
EXPOSE 8080
