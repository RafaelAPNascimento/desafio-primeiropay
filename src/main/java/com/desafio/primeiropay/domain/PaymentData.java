package com.desafio.primeiropay.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentData
{
    private String entityId;
    private String cpf;
    private String amount;
    private String currency;
    private String paymentBrand;
    private String paymentType;
    private String cardNumber;
    private String cardHolder;
    private String cardExpiryMonth;
    private String cardExpiryYear;
    private String cardCvv;
}
