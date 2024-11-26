package com.lvh.RentalBE.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.lvh.RentalBE.model.Vippackage;
import com.lvh.RentalBE.services.UserService;
import com.lvh.RentalBE.services.VipPackageService;
import jakarta.transaction.Transactional;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/bill")
public class BillController {

    @Value("${zalopay.app_id}")
    private String zalopayAppId;

    @Value("${zalopay.key1}")
    private String zalopayKey1;

    @Value("${zalopay.key2}")
    private String zalopayKey2;

    @Value("${zalopay.endpoint_create}")
    private String zalopayEndpointCreate;

    @Value("https://${ngrok.url}")
    private String urlNgrok;

    private final VipPackageService vipPackageService;
    private final VipPackageService userPackageService;

    public BillController(VipPackageService vipPackageService, VipPackageService userPackageService) {
        this.vipPackageService = vipPackageService;
        this.userPackageService = userPackageService;
    }

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<?> createOrder(@RequestBody Map<String, String> request) {
        try {
            Optional<Vippackage> vippackage = vipPackageService.findById(Long.parseLong(request.get("vipPackageId")));
            if (vippackage.isEmpty()) {
                return new ResponseEntity<>("Bill not found", HttpStatus.NOT_FOUND);
            }

            String userId = request.get("userId");
            String transID = request.get("vipPackageId") + userId;
            Integer amount = (int) (vippackage.get().getPrice());
            long appTime = System.currentTimeMillis(); // Lấy thời gian hiện tại

            // Định dạng app_trans_id
            String appTransId = new SimpleDateFormat("yyMMdd").format(new Date()) + "_" + transID + "_" + appTime;

            // Tạo order JSON object
            Map<String, Object> order = new HashMap<>();
            order.put("app_id", zalopayAppId);
            order.put("app_trans_id", appTransId);
            order.put("app_user", "user123");
            order.put("app_time", appTime);

            // Tạo embed_data JSON từ Map
            ObjectMapper objectMapper = new ObjectMapper();
            String embedData = objectMapper.writeValueAsString(Map.of("userId", userId, "vipPackageId", request.get("vipPackageId")));
            order.put("embed_data", embedData); // Gửi thông tin người dùng và gói

            order.put("item", "[{}]");
            order.put("amount", amount);
            order.put("description", "Nâng cấp tài khoản " + vippackage.get().getType());
            order.put("bank_code", "zalopayapp");
            String callback = urlNgrok + "/api/bill/callback";
            order.put("callback_url", callback);

            // Tạo dữ liệu HMAC SHA256
            String data = String.join("|", zalopayAppId, appTransId, "user123", String.valueOf(amount), String.valueOf(appTime), embedData, "[{}]");
            String mac = hmacSHA256(zalopayKey1, data);
            order.put("mac", mac);

            // Gửi request đến Zalopay
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(convertToMultiValueMap(order), headers);
            ResponseEntity<String> response = restTemplate.postForEntity(zalopayEndpointCreate, requestEntity, String.class);

            // Xử lý phản hồi từ Zalopay
            Map<String, Object> result = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {
            });

            if ("1".equals(result.get("return_code").toString())) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            }

            if ("2".equals(result.get("return_code").toString())) {
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(Map.of("ms", "fail!!!"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(Map.of("error", "Server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/callback")
    public ResponseEntity<?> handleZaloPayCallback(@RequestBody Map<String, Object> requestData) {
        Map<String, Object> result = new HashMap<>();

        try {
            // Lấy các trường từ callback data
            String mac = (String) requestData.get("mac");
            String data = (String) requestData.get("data");

            // Tính toán lại HMAC để kiểm tra tính hợp lệ
            String computedMac = hmacSHA256(zalopayKey2, data);

            // Kiểm tra callback hợp lệ (so sánh HMAC từ Zalopay với HMAC đã tính toán)
            if (!computedMac.equals(mac)) {
                // Callback không hợp lệ
                result.put("return_code", -1);
                result.put("return_message", "mac not equal");
            } else {
                ObjectMapper mapper = new ObjectMapper();

                try {

                    String dataString = requestData.get("data").toString();
                    Map<String, Object> dataMap = mapper.readValue(dataString, Map.class);

                    String embedDataJson = (String) dataMap.get("embed_data");

                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, String> embedData = objectMapper.readValue(embedDataJson, new TypeReference<Map<String, String>>() {});

                    // Lấy userId và vipPackageId từ embed_data
                    String userId = embedData.get("userId");
                    String vipPackageId = embedData.get("vipPackageId");

                    // In ra kết quả

                    // Gọi hàm registerVipPackage để lưu thông tin người dùng đăng ký gói VIP
                    this.userPackageService.registerVipPackage(Long.parseLong(userId), Long.parseLong(vipPackageId));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Trích xuất userId và vipPackageId từ embed_data

                result.put("return_code", 1);
                result.put("return_message", "success");

            }
        } catch (Exception e) {
            e.printStackTrace();
            // Trường hợp xảy ra lỗi
            result.put("return_code", 0); // Zalopay sẽ callback lại tối đa 3 lần
            result.put("error", e.getMessage());
        }
        // Trả về kết quả cho ZaloPay server
        return ResponseEntity.ok(result);
    }



    // Hàm tính HMAC SHA256
    private String hmacSHA256(String key, String data) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        sha256Hmac.init(secretKey);
        byte[] macData = sha256Hmac.doFinal(data.getBytes());
        return Hex.encodeHexString(macData);
    }

    // Chuyển đổi Map<String, Object> sang MultiValueMap<String, String> để gửi request form-urlencoded
    private MultiValueMap<String, String> convertToMultiValueMap(Map<String, Object> map) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        map.forEach((key, value) -> multiValueMap.add(key, value.toString()));
        return multiValueMap;
    }
}
