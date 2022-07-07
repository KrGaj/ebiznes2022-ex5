FROM ubuntu:22.04

ARG LIB_DIR="/usr/lib"
ARG JVM_DIR=${LIB_DIR}"/jvm"
ARG KOTLIN_DIR_COMMON=${LIB_DIR}"/kotlin"
ARG KOTLIN_DIR=${KOTLIN_DIR_COMMON}"/kotlinc"
ARG KTLINT_DIR=${KOTLIN_DIR_COMMON}"/lint"
ARG BACKEND_DIR="./app"
ARG ENTRYPOINT_FILENAME="./entrypoint.sh"

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

RUN echo "exec zsh" >> .bashrc \
    && source .bashrc

RUN [ ! -d ${JVM_DIR} ] && sudo mkdir ${BACKEND_DIR} || exit 0

WORKDIR $BACKEND_DIR
COPY . .

RUN touch ${ENTRYPOINT_FILENAME}
RUN echo "#/bin/zsh/" > ${ENTRYPOINT_FILENAME}
RUN --mount=type=secret,id=GOOGLE_CLIENT_ID echo "GOOGLE_CLIENT_ID=$(sudo cat /run/secrets/GOOGLE_CLIENT_ID) \\" >> ${ENTRYPOINT_FILENAME}
RUN --mount=type=secret,id=GOOGLE_CLIENT_SECRET echo "GOOGLE_CLIENT_SECRET=$(sudo cat /run/secrets/GOOGLE_CLIENT_SECRET) \\" >> ${ENTRYPOINT_FILENAME}
RUN --mount=type=secret,id=GITHUB_CLIENT_ID echo "GITHUB_CLIENT_ID=$(sudo cat /run/secrets/GITHUB_CLIENT_ID) \\" >> ${ENTRYPOINT_FILENAME}
RUN --mount=type=secret,id=GITHUB_CLIENT_SECRET echo "GITHUB_CLIENT_SECRET=$(sudo cat /run/secrets/GITHUB_CLIENT_SECRET) \\" >> ${ENTRYPOINT_FILENAME}
RUN --mount=type=secret,id=DATABASE_URL echo "DATABASE_URL=$(sudo cat /run/secrets/DATABASE_URL) \\" >> ${ENTRYPOINT_FILENAME}
RUN --mount=type=secret,id=DATABASE_USERNAME echo "DATABASE_USERNAME=$(sudo cat /run/secrets/DATABASE_USERNAME) \\" >> ${ENTRYPOINT_FILENAME}
RUN --mount=type=secret,id=DATABASE_PASSWORD echo "DATABASE_PASSWORD=$(sudo cat /run/secrets/DATABASE_PASSWORD) \\" >> ${ENTRYPOINT_FILENAME}
RUN echo "./gradlew run" >> ${ENTRYPOINT_FILENAME}

RUN sudo chown -R user ./
RUN sudo chmod -R 755 ./

ENTRYPOINT ["/bin/zsh", "./entrypoint.sh"]

EXPOSE 3000
EXPOSE 8080
