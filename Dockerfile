FROM openjdk:21-jdk-slim

# Instale o Maven
RUN apt-get update && apt-get install -y maven
RUN mvn -v

# Defina o diretório de trabalho dentro do container
WORKDIR /app

# Copie o código fonte para o container
COPY . /app

# Compile o projeto com Maven
RUN mvn clean package -DskipTests -X
RUN ls -l target/

# Copie o arquivo jar gerado para o container
# Note que você pode usar a instrução RUN para executar um comando que move o jar diretamente
RUN cp target/Blackbelt-0.0.1-SNAPSHOT.jar Blackbelt.jar

# Exponha a porta que a aplicação vai usar
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "Blackbelt.jar"]
