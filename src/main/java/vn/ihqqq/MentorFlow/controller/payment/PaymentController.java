package vn.ihqqq.MentorFlow.controller.payment;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vn.ihqqq.MentorFlow.dto.request.payment.PaymentRequest;
import vn.ihqqq.MentorFlow.dto.response.ApiResponse;
import vn.ihqqq.MentorFlow.dto.response.payment.PaymentResponse;
import vn.ihqqq.MentorFlow.dto.response.payment.VNPayResponse;
import vn.ihqqq.MentorFlow.enums.PaymentStatus;
import vn.ihqqq.MentorFlow.service.payment.PaymentService;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PaymentController {

    PaymentService paymentService;

    @PostMapping("/create")
    public ApiResponse<VNPayResponse> createPayment(
            @Valid @RequestBody PaymentRequest request,
            HttpServletRequest httpRequest) throws UnsupportedEncodingException {

        return ApiResponse.<VNPayResponse>builder()
                .result(paymentService.createPayment(request, httpRequest))
                .build();
    }

    @GetMapping("/vnpay-return")
    public ApiResponse<PaymentResponse> vnpayReturn(HttpServletRequest request) {
        // ✅ Lấy raw query string từ URL (chưa decode)
        String queryString = request.getQueryString();

        log.info("Raw query string: {}", queryString);

        // ✅ Parse params thủ công
        Map<String, String> params = new HashMap<>();

        if (queryString != null && !queryString.isEmpty()) {
            String[] pairs = queryString.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                if (idx > 0) {
                    String key = pair.substring(0, idx);
                    String value = pair.substring(idx + 1);
                    params.put(key, value);
                }
            }
        }

        return ApiResponse.<PaymentResponse>builder()
                .result(paymentService.handleVNPayReturn(params))
                .build();
    }

    @GetMapping("/my-payments")
    public ApiResponse<List<PaymentResponse>> getMyPayments() {
        return ApiResponse.<List<PaymentResponse>>builder()
                .result(paymentService.getMyPayments())
                .build();
    }

    @GetMapping("/my-payments/status/{status}")
    public ApiResponse<List<PaymentResponse>> getMyPaymentsByStatus(
            @PathVariable PaymentStatus status) {

        return ApiResponse.<List<PaymentResponse>>builder()
                .result(paymentService.getMyPaymentsByStatus(status))
                .build();
    }

    @GetMapping("/{paymentId}")
    public ApiResponse<PaymentResponse> getPaymentById(@PathVariable String paymentId) {
        return ApiResponse.<PaymentResponse>builder()
                .result(paymentService.getPaymentById(paymentId))
                .build();
    }
}