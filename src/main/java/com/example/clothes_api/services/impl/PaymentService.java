package com.example.clothes_api.services.impl;

import com.example.clothes_api.dto.payment.PaymentRequest;
import com.example.clothes_api.dto.payment.PaymentStatusRequest;
import com.example.clothes_api.dto.payment.ZaloPaymentDto;
import com.example.clothes_api.entity.Payment;
import com.example.clothes_api.repository.PaymentRepository;
import com.example.clothes_api.util.HMACUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${zalo.app-id}")
    private int APP_ID;

    @Value("${zalo.key-1}")
    private String KEY1;

    @Value("${zalo.key-2}")
    private String KEY2;

    @Value("${zalo.create-order-endpoint}")
    private String CREATE_ORDER_ENDPOINT;

    private final WebClient webClientZalopay;
    private final PaymentRepository paymentRepository;

    private String getCurrentTimeString(String format) {
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setCalendar(calendar);
        return formatter.format(calendar.getTimeInMillis());
    }

    @Transactional
    public Payment createPayment(PaymentRequest request) {
        String apptransid= getCurrentTimeString("yyMMdd")+ "_"+ new Date().toString();
        Payment payment=new Payment();
        payment.setTransactionId(apptransid);
        payment.setStatus("PENDING");
        payment.setCreatedBy(request.getOrderBy());
        payment.setPayPrice(request.getAmount());
        payment.setPaymentType("COD");
        payment.setOrderId(request.getOrderId());
        return paymentRepository.save(payment);

    }

    @Transactional
    public Mono<Object> createPaymentZALOPAY(PaymentRequest request) {
        Payment payment=createPayment(request);

        String apptransid= getCurrentTimeString("yyMMdd")+ "_"+ new Date().toString();

        ZaloPaymentDto order=new ZaloPaymentDto();
        order.setAppId(APP_ID);
        order.setAppUser("KaneStore");
        order.setAppTransId(apptransid);
        order.setAppTime(System.currentTimeMillis());
        order.setAmount(request.getAmount());
        order.setDescription("Thanh toán đơn hàng");
        order.setEmbedData("{}");
        order.setItem("[]");

        String data=order.getAppId()+ "|"+ order.getAppTransId()+ "|"+ order.getAppUser()+ "|"+ order.getAmount()+ "|"+ order.getAppTime() +"|"+ order.getEmbedData()+ "|"+ order.getItem() ;
        String mac="";
        try {
            mac= HMACUtil.HMacEncode("HmacSHA256", data, KEY1);
        }catch (Exception e){
            throw new RuntimeException("Error while encoding mac");
        }

        order.setMac(mac);

        String json="";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            json = objectMapper.writeValueAsString(order);
            System.out.println(json);
        } catch (Exception e) {
            throw new RuntimeException("Error while converting to json");
        }
        return webClientZalopay.post()
                .uri(CREATE_ORDER_ENDPOINT)
                .header("Content-Type", "application/json")
                .bodyValue(json)
                .retrieve()
                .bodyToMono(Object.class)
                .flatMap(response -> {
                    payment.setPaymentType("ZALOPAY");
                    paymentRepository.save(payment);
                    return Mono.just(response);
                });

    }

    public Mono<Integer> getStatusPaymentZALO(String apptransid) {

        PaymentStatusRequest request=new PaymentStatusRequest();
        request.setAppId(APP_ID);
        request.setTransactionId(apptransid);

        String data=request.getAppId()+ "|"+ request.getTransactionId()+ "|" + KEY1;
        String mac="";
        try {
            mac= HMACUtil.HMacEncode("HmacSHA256", data, KEY1);
        }catch (Exception e){
            throw new RuntimeException("Error while encoding mac");
        }
        if(mac.isEmpty())
            return Mono.just(-1);

        request.setMac(mac);

        String json="";
        try {
            ObjectMapper mapper=new ObjectMapper();
            json=mapper.writeValueAsString(request);
        }catch (Exception e){
            throw new RuntimeException("Error while converting to json");
        }
        if(json.isEmpty())
            return Mono.just(-1);

        return webClientZalopay.post()
                .uri("/v2/query")
                .header("Content-Type", "application/json")
                .bodyValue(json)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> {
                    try {
                        ObjectMapper mapper=new ObjectMapper();
                        JsonNode node=mapper.readTree(response);
                        Integer returnCode=node.path("return_code").asInt();
                        if(returnCode==1){
                            Payment payment=paymentRepository.findByTransactionId(apptransid).get();
                            payment.setStatus("SUCCESS");
                            paymentRepository.save(payment);
                            return Mono.just(returnCode);
                        }
                        else if(returnCode==2){
                            Payment payment=paymentRepository.findByTransactionId(apptransid).get();
                            payment.setStatus("FAILED");
                            paymentRepository.save(payment);
                            return Mono.just(returnCode);
                        }
                        else {
                            return Mono.just(-1);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return Mono.just(-1);
                });


    }

}
