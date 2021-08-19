# Docker image for springboot file run

# 基础镜像使用java
FROM java:8
# 作者信息
MAINTAINER whyat <whyat@foxmail.com>
# 暴露8081端口，可以被映射到8081
EXPOSE 8081
# 添加jar包到容器
ADD vueadmin-java-0.0.1-SNAPSHOT.jar vueadmin.jar
# 运行jar包
RUN bash -c 'touch /vueadmin.jar'
ENTRYPOINT ["java","-jar","/vueadmin.jar"]