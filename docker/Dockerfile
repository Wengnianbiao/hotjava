# 选择基础镜像，这里以openjdk 8为例，可根据实际项目使用的Java版本调整
FROM openjdk:8u342-jdk-slim-buster

# 设置工作目录，后续复制和运行命令都在这个目录下操作
WORKDIR /app

# 将项目target目录下的docker.jar复制到镜像内的/app目录
COPY target/docker.jar /app/

# 指定容器启动时执行的命令，运行该jar包
CMD ["java", "-jar", "docker.jar"]