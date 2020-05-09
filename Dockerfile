FROM vertx/vertx3

LABEL author=rafael.sjp17@gmail.com
LABEL site="https://finalexception.blogspot.com"

ENV VERTICLE_NAME com.desafio.primeiropay.resource.MainVerticle
ENV VERTICLE_FILE target/desafio-primeiropay.jar

ENV VERTICLE_HOME /usr/verticles

EXPOSE 8091

COPY $VERTICLE_FILE $VERTICLE_HOME/

WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec vertx run $VERTICLE_NAME -cp $VERTICLE_HOME/*"]

# docker build -t rafaelnasc1mento/desafio-primeiropay:latest .
# docker run -it -p <localhost-port>:8091 --name desafio-primeirypay rafaelnasc1mento/desafio-primeiropay:latest
