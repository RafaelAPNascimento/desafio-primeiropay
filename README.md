**Desafio Primeiro Pay**

DockerHub da imagem

        https://hub.docker.com/r/rafaelnasc1mento/desafio-primeiropay

Para empacotar a aplicação execute (do diretorio raiz):

        ./mvnw clean package

Para rodar localmente a aplicação execute (do diretorio raiz):

    ./mvnw clean compile exec:java

Uma vez rodando, esses são os endpoints (_`_~~o server usa a porta 8091~~_`_). **Todos requerem o bearer token no header authorization**:

**PreAuthorization (PA)**

    POST http://localhost:8091/paymentsystem/preauth

As informações requeridas pelo PA devem ser inseridas no body na forma de JSON, como no exemplo abaixo:

{
 "entityId":"**********",
 "cpf":"33333333344",
 "amount":"92.00",
 "currency":"EUR",
 "paymentBrand":"VISA",
 "paymentType":"CD",
 "cardNumber":"4200000000000000",
 "cardHolder":"Jane Jones",
 "cardExpiryMonth":"05",
 "cardExpiryYear":"2020",
 "cardCvv":"123"
}

**Capture**

    POST http://localhost:8091/paymentsystem/capture/{id}

Exemplo do JSON requerido no body:

{
 "entityId":"*********",
 "amount":"10.00",
 "currency":"EUR",
 "paymentType":"CP",
 "cpf":"33333333344"
}

**Refund**

    POST http://localhost:8091/paymentsystem/refund/{id}

Exemplo JSON requerido no body (o payment type **deve ser RF**):

{
 "entityId":"***********",
 "amount":"12.00",
 "currency":"EUR",
 "paymentType":"RF",
 "cpf":"33333333344"
}

