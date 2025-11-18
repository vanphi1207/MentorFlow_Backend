package vn.ihqqq.MentorFlow.service.payment;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.ihqqq.MentorFlow.configuration.VNPayConfig;
import vn.ihqqq.MentorFlow.dto.request.payment.PaymentRequest;
import vn.ihqqq.MentorFlow.dto.response.payment.PaymentResponse;
import vn.ihqqq.MentorFlow.dto.response.payment.VNPayResponse;
import vn.ihqqq.MentorFlow.entity.course.Course;
import vn.ihqqq.MentorFlow.entity.payment.Payment;
import vn.ihqqq.MentorFlow.entity.user.User;
import vn.ihqqq.MentorFlow.entity.user.UserCourse;
import vn.ihqqq.MentorFlow.enums.PaymentMethod;
import vn.ihqqq.MentorFlow.enums.PaymentStatus;
import vn.ihqqq.MentorFlow.exception.AppException;
import vn.ihqqq.MentorFlow.exception.ErrorCode;
import vn.ihqqq.MentorFlow.mapper.PaymentMapper;
import vn.ihqqq.MentorFlow.repository.CourseRepository;
import vn.ihqqq.MentorFlow.repository.PaymentRepository;
import vn.ihqqq.MentorFlow.repository.UserRepository;
import vn.ihqqq.MentorFlow.service.UserService;
import vn.ihqqq.MentorFlow.utils.VNPayUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentService {

    PaymentRepository paymentRepository;
    CourseRepository courseRepository;
    UserRepository userRepository;
    PaymentMapper paymentMapper;
    VNPayConfig vnPayConfig;
    UserService userService;

    @Transactional
    public VNPayResponse createPayment(PaymentRequest request, HttpServletRequest httpRequest)
            throws UnsupportedEncodingException {

        User user = userService.getCurrentUser();

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        // Kiểm tra đã mua khóa học chưa
        if (paymentRepository.existsByUser_UserIdAndCourse_CourseIdAndStatus(
                user.getUserId(), course.getCourseId(), PaymentStatus.SUCCESS)) {
            throw new AppException(ErrorCode.COURSE_EXISTED);
        }

        // Tạo mã giao dịch
        String txnRef = VNPayUtil.getRandomNumber(8);

        // Tạo payment record
        Payment payment = Payment.builder()
                .user(user)
                .course(course)
                .amount(request.getAmount())
                .paymentMethod(PaymentMethod.VNPAY)
                .status(PaymentStatus.PENDING)
                .transactionNo(txnRef)
                .orderInfo(request.getOrderInfo() != null ?
                        request.getOrderInfo() : "Thanh toan khoa hoc: " + course.getTitleCourse())
                .build();

        paymentRepository.save(payment);

        // Tạo URL thanh toán VNPay
        String paymentUrl = buildVNPayUrl(payment, httpRequest, request.getBankCode());

        return VNPayResponse.builder()
                .paymentUrl(paymentUrl)
                .message("Tạo URL thanh toán thành công")
                .success(true)
                .build();
    }

    private String buildVNPayUrl(Payment payment, HttpServletRequest request, String bankCode)
            throws UnsupportedEncodingException {

        Map<String, String> vnpParams = new HashMap<>();

        vnpParams.put("vnp_Version", vnPayConfig.getVersion());
        vnpParams.put("vnp_Command", vnPayConfig.getCommand());
        vnpParams.put("vnp_TmnCode", vnPayConfig.getTmnCode());
        vnpParams.put("vnp_Amount", String.valueOf(payment.getAmount().longValue() * 100)); // VNPay yêu cầu * 100
        vnpParams.put("vnp_CurrCode", "VND");

        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParams.put("vnp_BankCode", bankCode);
        }

        vnpParams.put("vnp_TxnRef", payment.getTransactionNo());
        vnpParams.put("vnp_OrderInfo", payment.getOrderInfo());
        vnpParams.put("vnp_OrderType", vnPayConfig.getOrderType());
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", vnPayConfig.getReturnUrl());
        vnpParams.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpCreateDate = formatter.format(cld.getTime());
        vnpParams.put("vnp_CreateDate", vnpCreateDate);

        cld.add(Calendar.MINUTE, 15); // Timeout 15 phút
        String vnpExpireDate = formatter.format(cld.getTime());
        vnpParams.put("vnp_ExpireDate", vnpExpireDate);

        // Sắp xếp params và tạo query string
        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnpParams.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String queryUrl = query.toString();
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getHashSecret(), hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;

        String paymentUrl = vnPayConfig.getVnpayUrl() + "?" + queryUrl;

        log.info("Payment URL created for transaction: {}", payment.getTransactionNo());

        return paymentUrl;
    }

//    @Transactional
//    public PaymentResponse handleVNPayReturn(Map<String, String> params) {
//        String vnpSecureHash = params.get("vnp_SecureHash");
//        params.remove("vnp_SecureHashType");
//        params.remove("vnp_SecureHash");
//
//        // Verify signature
//        String signValue = VNPayUtil.hashAllFields(params, vnPayConfig.getHashSecret());
//
//        if (!signValue.equals(vnpSecureHash)) {
//            log.error("Invalid signature");
//            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
//        }
//
//        String txnRef = params.get("vnp_TxnRef");
//        String responseCode = params.get("vnp_ResponseCode");
//        String transactionNo = params.get("vnp_TransactionNo");
//        String bankCode = params.get("vnp_BankCode");
//        String cardType = params.get("vnp_CardType");
//
//        Payment payment = paymentRepository.findByTransactionNo(txnRef)
//                .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));
//
//        if ("00".equals(responseCode)) {
//            payment.setStatus(PaymentStatus.SUCCESS);
//            payment.setVnpayTransactionNo(transactionNo);
//            payment.setBankCode(bankCode);
//            payment.setCardType(cardType);
//            payment.setVnpayResponseCode(responseCode);
//
//            // Tạo UserCourse khi thanh toán thành công
//            createUserCourse(payment);
//
//            log.info("Payment successful: {}", txnRef);
//        } else {
//            payment.setStatus(PaymentStatus.FAILED);
//            payment.setVnpayResponseCode(responseCode);
//            log.warn("Payment failed: {} - Response code: {}", txnRef, responseCode);
//        }
//
//        Payment savedPayment = paymentRepository.save(payment);
//        return paymentMapper.toPaymentResponse(savedPayment);
//    }

    @Transactional
    public PaymentResponse handleVNPayReturn(Map<String, String> params) {
        log.info("=== VNPay Return Params (Raw) ===");
        params.forEach((key, value) -> log.info("{}: {}", key, value));

        // ✅ Lấy secure hash
        String vnpSecureHash = params.get("vnp_SecureHash");

        // ✅ Tính hash với params RAW (chưa decode)
        String calculatedHash = VNPayUtil.hashAllFields(params, vnPayConfig.getHashSecret());

        log.info("Received hash:   {}", vnpSecureHash);
        log.info("Calculated hash: {}", calculatedHash);

        // ✅ So sánh
        if (!calculatedHash.equalsIgnoreCase(vnpSecureHash)) {
            log.error("❌ Signature mismatch!");
            throw new AppException(ErrorCode.INVALID_PAYMENT_SIGNATURE);
        }

        log.info("✅ Signature valid!");

        // ✅ Decode params khi sử dụng
        String txnRef = params.get("vnp_TxnRef");
        String responseCode = params.get("vnp_ResponseCode");
        String transactionNo = params.get("vnp_TransactionNo");
        String bankCode = params.get("vnp_BankCode");
        String cardType = params.get("vnp_CardType");

        // ✅ Decode orderInfo nếu cần hiển thị
        String orderInfo = params.get("vnp_OrderInfo");
        try {
            orderInfo = URLDecoder.decode(orderInfo, StandardCharsets.UTF_8);
            log.info("Decoded order info: {}", orderInfo);
        } catch (Exception e) {
            log.warn("Cannot decode orderInfo: {}", orderInfo);
        }

        Payment payment = paymentRepository.findByTransactionNo(txnRef)
                .orElseThrow(() -> {
                    log.error("❌ Payment not found: {}", txnRef);
                    return new AppException(ErrorCode.PAYMENT_NOT_FOUND);
                });

        if ("00".equals(responseCode)) {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setVnpayTransactionNo(transactionNo);
            payment.setBankCode(bankCode);
            payment.setCardType(cardType);
            payment.setVnpayResponseCode(responseCode);

            createUserCourse(payment);
            log.info("✅ Payment successful: {}", txnRef);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setVnpayResponseCode(responseCode);
            log.warn("⚠️ Payment failed: {} - code: {}", txnRef, responseCode);
        }

        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toPaymentResponse(savedPayment);
    }

    private void createUserCourse(Payment payment) {
        UserCourse userCourse = UserCourse.builder()
                .user(payment.getUser())
                .course(payment.getCourse())
                .purchaseDate(LocalDate.now())
                .paymentStatus("PAID")
                .build();

        payment.getUser().getUserCourses().add(userCourse);

        // Tăng enrolledCount
        Course course = payment.getCourse();
        course.setEnrolledCount(course.getEnrolledCount() + 1);
        courseRepository.save(course);
    }

    public List<PaymentResponse> getMyPayments() {
        User user = userService.getCurrentUser();
        return paymentRepository.findByUser_UserId(user.getUserId())
                .stream()
                .map(paymentMapper::toPaymentResponse)
                .toList();
    }

    public List<PaymentResponse> getMyPaymentsByStatus(PaymentStatus status) {
        User user = userService.getCurrentUser();
        return paymentRepository.findByUser_UserIdAndStatus(user.getUserId(), status)
                .stream()
                .map(paymentMapper::toPaymentResponse)
                .toList();
    }

    public PaymentResponse getPaymentById(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));

        return paymentMapper.toPaymentResponse(payment);
    }
}