FROM openjdk:8u121-jdk

RUN apt-get update && \
    apt-get --assume-yes install git curl sudo

ENV ANDROID_SDK_URL "https://dl.google.com/android/repository/sdk-tools-linux-3859397.zip"
ENV ANDROID_SDK_FILE_NAME "android-sdk.zip"

ENV ANDROID_HOME /opt/android-sdk-linux
ENV PATH ${PATH}:${ANDROID_HOME}/tools:${ANDROID_HOME}/tools/bin:${ANDROID_HOME}/platform-tools

RUN \
  mkdir -p $ANDROID_HOME && \
  curl $ANDROID_SDK_URL --progress-bar --location --output $ANDROID_SDK_FILE_NAME && \
  unzip $ANDROID_SDK_FILE_NAME -d $ANDROID_HOME && \
  rm $ANDROID_SDK_FILE_NAME

COPY android-sdk-packages /android-sdk-packages

RUN echo "y" | $ANDROID_HOME/tools/bin/sdkmanager --package_file=/android-sdk-packages --verbose

COPY entrypoint.sh /usr/local/bin/entrypoint.sh
RUN chmod +x /usr/local/bin/entrypoint.sh
ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]