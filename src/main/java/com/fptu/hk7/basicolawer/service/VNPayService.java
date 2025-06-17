package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.config.VNPayConfig;
import com.fptu.hk7.basicolawer.dto.Request.FormPaymentDTO;
import com.fptu.hk7.basicolawer.dto.Request.RequestDTO;
import com.fptu.hk7.basicolawer.enums.PaymentMethodEnum;
import com.fptu.hk7.basicolawer.enums.PaymentStatusEnum;
import com.fptu.hk7.basicolawer.enums.RequestStatusEnum;
import com.fptu.hk7.basicolawer.pojo.*;
import com.fptu.hk7.basicolawer.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VNPayService {

    private final VNPayConfig config;

    public VNPayService(VNPayConfig config) {
        this.config = config;
    }
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private StaffProfileRepository staffProfileRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private FormTemplateRepository formTemplateRepository;
    @Autowired
    private UserFormRepository userFormRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private BookingRepository bookingRepository;
    public String createPaymentUrl(RequestDTO dto , HttpServletRequest request) {
        try {
            System.out.println(dto);
            String orderId = String.valueOf(System.currentTimeMillis());
            String txnRef = orderId;

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", "2.1.0");
            vnp_Params.put("vnp_Command", "pay");
            vnp_Params.put("vnp_TmnCode", config.getTmnCode());
            vnp_Params.put("vnp_Amount", String.valueOf(dto.getAmount() * 100)); // VNPay yêu cầu số tiền tính bằng đồng
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", txnRef);
            vnp_Params.put("vnp_OrderInfo", dto.getNote().isEmpty() ? "Thanh toan dich vu" : dto.getNote());// hoặc dto.getNote()
            vnp_Params.put("vnp_OrderType", "other");
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", config.getReturnUrl());
            String ipAddr = request.getHeader("X-Forwarded-For");
            if (ipAddr == null || ipAddr.isEmpty() || ipAddr.equals("0:0:0:0:0:0:0:1")) {
                ipAddr = "127.0.0.1"; // hoặc IP thật nếu dùng ngrok
            }
            vnp_Params.put("vnp_IpAddr", ipAddr);
            vnp_Params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
            vnp_Params.put("vnp_BankCode", "NCB");

            // Sort + encode
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);

            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            for (String fieldName : fieldNames) {
                String fieldValue = vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    // Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    // Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (!fieldName.equals(fieldNames.get(fieldNames.size() - 1))) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            String secureHash = config.hmacSHA512(config.getHashSecret(), hashData.toString());
            System.out.println("HashData: " + hashData);
            System.out.println("SecureHash: " + secureHash);
            String paymentUrl = config.getUrl() + "?" + query + "&vnp_SecureHash=" + secureHash;
            System.out.println("VNPay Payment URL: " + paymentUrl);
            saveRequestAfterPayment(dto,txnRef);
            return paymentUrl;

        } catch (Exception e) {
            throw new RuntimeException("Error creating VNPay URL", e);
        }
    }

    public String createPaymentFormUrl(FormPaymentDTO dto , HttpServletRequest request) {
        try {
            System.out.println(dto);
            String formId = String.valueOf(System.currentTimeMillis());
            String txnRef = formId;

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", "2.1.0");
            vnp_Params.put("vnp_Command", "pay");
            vnp_Params.put("vnp_TmnCode", config.getTmnCode());
            vnp_Params.put("vnp_Amount", String.valueOf(dto.getAmount() * 100)); // VNPay yêu cầu số tiền tính bằng đồng
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", txnRef);
            vnp_Params.put("vnp_OrderInfo", dto.getNote() != null ? dto.getNote() : "Thanh toan hoa don");
            vnp_Params.put("vnp_OrderType", "other");
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", config.getReturnFormUrl());
            String ipAddr = request.getHeader("X-Forwarded-For");
            if (ipAddr == null || ipAddr.isEmpty() || ipAddr.equals("0:0:0:0:0:0:0:1")) {
                ipAddr = "127.0.0.1"; // hoặc IP thật nếu dùng ngrok
            }
            vnp_Params.put("vnp_IpAddr", ipAddr);
            vnp_Params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
            vnp_Params.put("vnp_BankCode", "NCB");

            // Sort + encode
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);

            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            for (int i = 0; i < fieldNames.size(); i++) {
                String fieldName = fieldNames.get(i);
                String fieldValue = vnp_Params.get(fieldName);
                if (fieldValue != null && !fieldValue.isEmpty()) {
                    hashData.append(fieldName).append("=").append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8))
                            .append("=")
                            .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                    if (i < fieldNames.size() - 1) {
                        hashData.append("&");
                        query.append("&");
                    }
                }
            }

            String secureHash = config.hmacSHA512(config.getHashSecret(), hashData.toString());
            System.out.println("HashData: " + hashData);
            System.out.println("SecureHash: " + secureHash);
            String paymentUrl = config.getUrl() + "?" + query + "&vnp_SecureHash=" + secureHash;
            System.out.println("VNPay Payment URL: " + paymentUrl);
            saveFormTempalte(dto,txnRef);
            return paymentUrl;

        } catch (Exception e) {
            throw new RuntimeException("Error creating VNPay URL", e);
        }
    }

    public void saveFormTempalte(FormPaymentDTO formPaymentDTO , String txnRef) {
        // Tạo entity UserForm mới
        UserForm userForm = new UserForm();

        // Gán các trường từ DTO

        userForm.setTxnRef(txnRef);
        userForm.setAmount(formPaymentDTO.getAmount());
        userForm.setPaymentMethod(formPaymentDTO.getPaymentMethod());
        User user = userService.getCurrentUser();
        userForm.setUser(userService.getCurrentUser());

        FormTemplate formTemplate = formTemplateRepository.findById(formPaymentDTO.getFormId()).get();
        userForm.setTemplate(formTemplate);

        userFormRepository.save(userForm);
        // Lấy User từ DB nếu có

    }

    public void saveRequestAfterPayment(RequestDTO dto , String txnRef) {
        // Tạo entity Request mới
        Request request = new Request();

        // Set trạng thái mặc định là PENDING
        request.setStatus(RequestStatusEnum.PENDING);

        // Gán các trường từ DTO
        request.setDateBooking(dto.getDateBooking());
        request.setDateRequest(dto.getDateRequest());

        request.setNote(dto.getNote());
        request.setSlot(dto.getSlot());
        request.setUser(userService.getCurrentUser());
        request.setTxnRef(txnRef);
        List<String> serviceNames = dto.getServices(); // List of service names
        if (serviceNames != null) {
            List<Services> services = new ArrayList<>();
            for (String name : serviceNames) {
                Optional<Services> serviceOpt = serviceRepository.findByName(name);
                serviceOpt.ifPresent(services::add);
            }
            request.setServices(services);
        }
        UUID randomLawyerUUID = null;
        if (dto.getPreferedLawerUUID() != null ) {
            request.setPreferedLawerUUID(dto.getPreferedLawerUUID());
            randomLawyerUUID = dto.getPreferedLawerUUID() instanceof UUID
                    ? (UUID) dto.getPreferedLawerUUID()
                    : UUID.fromString(dto.getPreferedLawerUUID().toString());
        } else {
            randomLawyerUUID = pickRandomAvailableStaff(
                    dto.getDateBooking(),
                    dto.getSlot(),
                    dto.getServices() // truyền chuyên môn xuống
            );
            request.setPreferedLawerUUID(randomLawyerUUID);
        }

        // Lấy User từ DB nếu có
        if (dto.getUserId() != null) {
            UUID userUuid = dto.getUserId() instanceof UUID
                    ? (UUID) dto.getUserId()
                    : UUID.fromString(dto.getUserId().toString());
            Optional<User> userOpt = userRepository.findById(userUuid);
            if (userOpt.isPresent()) {
                request.setUser(userOpt.get());

            } else {
                throw new RuntimeException("User not found with id: " + dto.getUserId());
            }
        }

        // TODO: Gán các trường liên quan khác nếu cần (Booking, Payment, Specialty...)

        // Lưu Request xuống D

        Payment payment = new Payment();
        payment.setStatus(PaymentStatusEnum.PAID);
        payment.setAmount(dto.getAmount());
        payment.setMethod(PaymentMethodEnum.VNPAY);
        payment.setRequest(request);

        request.setPayment(payment);

        Optional<StaffProfile> staffProfile = staffProfileRepository.findById(randomLawyerUUID);
        if (!staffProfile.isPresent()) {
            throw new RuntimeException("Staff not found with id: " + randomLawyerUUID);
        }

        Booking booking = new Booking();
        booking.setRequest(request);
        booking.setUser(staffProfile.get().getUser());
        booking.setAssignedAt(LocalDateTime.now());

        request.setBooking(booking);


        // Lưu các entity xuống DB
        requestRepository.save(request);
        paymentRepository.save(payment);
        bookingRepository.save(booking);
    }

    public RequestDTO handleVNPayReturn(String txnRef, String vnpResponseCode) {
        if (!"00".equals(vnpResponseCode)) {
            return null; // Thanh toán thất bại
        }

        Optional<Request> requestOpt = requestRepository.findByTxnRef(txnRef);
        System.out.println(requestOpt);
        Optional<Payment> paymentOpt = paymentRepository.findByRequest(requestOpt.get());
        paymentOpt.get().setStatus(PaymentStatusEnum.PAID);
        paymentRepository.save(paymentOpt.get());
        if (requestOpt.isPresent()) {
            return convertToDTO(requestOpt.get(),paymentOpt.get());
        }

        return null; // Không tìm thấy Request
    }

    public UserForm handleVNPayFormReturn(String txnRef, String vnpResponseCode) {
        if (!"00".equals(vnpResponseCode)) {
            return null; // Thanh toán thất bại
        }
        Optional<UserForm> userForm = userFormRepository.findByTxnRef(txnRef);
        userForm.get().setPaid(true);
        userFormRepository.save(userForm.get());
        if (userForm.isPresent()) {
            return userForm.get();
        }

        return null; // Không tìm thấy Request
    }

    public RequestDTO getRequestDTOByTxnRef(String txnRef) {
        Optional<Request> requestOpt = requestRepository.findByTxnRef(txnRef);
        if (requestOpt.isPresent()) {
            Request request = requestOpt.get();
            Optional<Payment> paymentOpt = paymentRepository.findByRequest(request);
            Payment payment = paymentOpt.get();
            return convertToDTO(request,payment);
        }
        return null; // hoặc throw exception nếu muốn
    }

    private RequestDTO convertToDTO(Request request , Payment payment) {
        RequestDTO dto = new RequestDTO();
        dto.setDateBooking(request.getDateBooking());
        dto.setDateRequest(request.getDateRequest());
        dto.setNote(request.getNote());
        dto.setSlot(request.getSlot());
        dto.setPreferedLawerUUID(request.getPreferedLawerUUID());
        Optional<StaffProfile> staffProfile = staffProfileRepository.findById(request.getPreferedLawerUUID());
        dto.setStaffName(staffProfile.get().getUser().getFullname());
        dto.setUserId(request.getUser() != null ? request.getUser().getId() : null);
        List<String> serviceNames = null;
        if (request.getServices() != null) {
            serviceNames = request.getServices().stream()
                    .map(Services::getName)
                    .collect(Collectors.toList());
        }
        dto.setServices(serviceNames);
        dto.setAmount(payment.getAmount());
        // Gán thêm các trường nếu có
        return dto;
    }

    UUID pickRandomAvailableStaff(LocalDate dateBooking, int slot, List<String> services) {
        List<StaffProfile> availableStaff = staffProfileRepository.findAvailableStaff(dateBooking, slot, services);
        if (availableStaff.isEmpty()) {
            throw new RuntimeException("Không có luật sư nào phù hợp.");
        }
        Collections.shuffle(availableStaff);
        return availableStaff.get(0).getId(); // UUID hoặc Long tuỳ định nghĩa
    }


}
