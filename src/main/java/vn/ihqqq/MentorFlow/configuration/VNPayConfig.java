package vn.ihqqq.MentorFlow.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class VNPayConfig {

    @Value("${payment.vnPay.tmnCode}")
    private String tmnCode;

    @Value("${payment.vnPay.secretKey}")
    private String hashSecret;

    @Value("${payment.vnPay.url}")
    private String vnpayUrl;

    @Value("${payment.vnPay.returnUrl}")
    private String returnUrl;

    @Value("${payment.vnPay.version}")
    private String version;

    @Value("${payment.vnPay.command}")
    private String command;

    @Value("${payment.vnPay.orderType}")
    private String orderType;
}