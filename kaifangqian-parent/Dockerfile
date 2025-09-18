FROM registry.cn-beijing.aliyuncs.com/kaifangqian/eclipse-temurin:8u462-b08-jre-noble
#ENV LANG en_US.UTF-8
#ENV LANGUAGE en_US:en
#ENV LC_ALL en_US.UTF-8

RUN mkdir /usr/share/fonts/chinese -p
COPY file/simsun.ttc /usr/share/fonts/chinese
COPY file/simkai.ttf /usr/share/fonts/chinese
COPY file/simhei.ttf /usr/share/fonts/chinese
COPY file/simfang.ttf /usr/share/fonts/chinese
#  刷新字体缓存
RUN fc-cache -fv && fc-list : family

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /app
COPY file /home/kaifangqian/file
ADD  ./kaifangqian-system/target/kaifangqian.jar /app/kaifangqian.jar
ENTRYPOINT ["sh", "-c", "exec java -jar -Dspring.profiles.active=prod -Dproject.name=kaifangqian kaifangqian.jar"]