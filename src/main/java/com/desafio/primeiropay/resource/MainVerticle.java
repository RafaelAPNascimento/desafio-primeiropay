package com.desafio.primeiropay.resource;

import com.desafio.primeiropay.domain.PaymentData;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.ext.web.handler.BodyHandler;

public class MainVerticle extends AbstractVerticle
{
    private WebClient client;

    @Override
    public void start(Future<Void> startFuture) throws Exception
    {
        client = WebClient.create(vertx);

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.post("/paymentsystem/preauth").handler(this::preAuthorization);
        router.post("/paymentsystem/capture/:id").handler(this::capture);
        router.post("/paymentsystem/refund/:id").handler(this::refund);

        vertx.createHttpServer()
            .requestHandler(router::accept)
            .listen(config().getInteger("http.port", 8091),
                httpServerAsyncResult -> {

                    if (httpServerAsyncResult.succeeded())
                    {
                        System.out.println("Http Server UP and Running!");
                        startFuture.complete();
                    }
                    else
                    {
                        startFuture.fail(httpServerAsyncResult.cause());
                        System.out.println("Http Server Failed to Start!");
                    }
                });
    }

    private void preAuthorization(RoutingContext context)
    {
        PaymentData paymentData = context.getBodyAsJson().mapTo(PaymentData.class);

        System.out.println("CPF: "+paymentData.getCpf());

        MultiMap form = MultiMap.caseInsensitiveMultiMap();
        form.set("entityId", paymentData.getEntityId());
        form.set("amount", paymentData.getAmount());
        form.set("currency", paymentData.getCurrency());
        form.set("paymentBrand", paymentData.getPaymentBrand());
        form.set("paymentType", paymentData.getPaymentType());
        form.set("card.number", paymentData.getCardNumber());
        form.set("card.holder", paymentData.getCardHolder());
        form.set("card.expiryMonth", paymentData.getCardExpiryMonth());
        form.set("card.expiryYear", paymentData.getCardExpiryYear());
        form.set("card.cvv", paymentData.getCardCvv());

        client.postAbs("https://test.oppwa.com/v1/payments")
            .as(BodyCodec.jsonObject())
            .bearerTokenAuthentication(getAuthToken(context))
            .sendForm(form, asyncResult -> {

                if (asyncResult.succeeded())
                {
                    System.out.println("PA Form enviado...");
                    HttpResponse<JsonObject> response = asyncResult.result();
                    JsonObject bodyResponse = response.body();
                    context.response().end(bodyResponse.toString());
                }
                else
                {
                    System.out.println("PA Form não enviado");
                    context.response().end(asyncResult.toString());
                }
            });
    }

    private void capture(RoutingContext context)
    {
      PaymentData paymentData = context.getBodyAsJson().mapTo(PaymentData.class);

      System.out.println("CPF: "+paymentData.getCpf());

      MultiMap form = MultiMap.caseInsensitiveMultiMap();
      form.set("entityId", paymentData.getEntityId());
      form.set("amount", paymentData.getAmount());
      form.set("currency", paymentData.getCurrency());
      form.set("paymentType", paymentData.getPaymentType());

      client.postAbs("https://test.oppwa.com/v1/payments/"+context.pathParam("id"))
          .as(BodyCodec.jsonObject())
          .bearerTokenAuthentication(getAuthToken(context))
          .sendForm(form, asyncResult -> {

              if (asyncResult.succeeded())
              {
                  System.out.println("CP Form enviado...");
                  HttpResponse<JsonObject> response = asyncResult.result();
                  JsonObject bodyResponse = response.body();
                  context.response().end(bodyResponse.toString());
              }
              else
              {
                  System.out.println("CP Form não enviado");
                  context.response().end(asyncResult.toString());
              }
          });

    }

    private void refund(RoutingContext context)
    {
        PaymentData paymentData = context.getBodyAsJson().mapTo(PaymentData.class);

        System.out.println("CPF: "+paymentData.getCpf());

        MultiMap form = MultiMap.caseInsensitiveMultiMap();
        form.set("entityId", paymentData.getEntityId());
        form.set("amount", paymentData.getAmount());
        form.set("currency", paymentData.getCurrency());
        form.set("paymentType", paymentData.getPaymentType());

        client.postAbs("https://test.oppwa.com/v1/payments/"+context.pathParam("id"))
            .as(BodyCodec.jsonObject())
            .bearerTokenAuthentication(getAuthToken(context))
            .sendForm(form, asyncResult -> {

                if (asyncResult.succeeded())
                {
                    System.out.println("RF Form enviado...");
                    HttpResponse<JsonObject> response = asyncResult.result();
                    JsonObject bodyResponse = response.body();
                    context.response().end(bodyResponse.toString());
                }
                else
                {
                    System.out.println("RF Form não enviado");
                    context.response().end(asyncResult.toString());
                }
            });
    }

    private String getAuthToken(RoutingContext context)
    {
        String authToken = context.request().getHeader("Authorization");
        authToken = authToken.substring(7, authToken.length()).trim();
        return authToken;
    }
}
