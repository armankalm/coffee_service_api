package com.example.coffee_service_api.auth;

import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class TelegramAuthValidator {

    private final String botToken;

    public TelegramAuthValidator(String botToken) {
        this.botToken = botToken;
    }

    public boolean validate(Map<String, String> data) {
        String hash = data.get("hash");
        if (hash == null) return false;

        // копируем и удаляем hash
        SortedMap<String, String> sorted = new TreeMap<>(data);
        sorted.remove("hash");

        // строим data_check_string: key=value\nkey2=value2...
        StringBuilder dataCheckString = new StringBuilder();
        sorted.forEach((k, v) -> {
            if (v != null && !v.isEmpty()) {
                dataCheckString.append(k).append("=").append(v).append("\n");
            } else {
                dataCheckString.append(k).append("=").append("\n");
            }
        });
        if (dataCheckString.length() > 0) {
            dataCheckString.setLength(dataCheckString.length() - 1); // убрать последний \n
        }

        try {
            // secret key = SHA256(botToken)
            byte[] secretKey = DigestUtils.sha256(botToken);

            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(secretKey, "HmacSHA256");
            mac.init(keySpec);

            byte[] computed = mac.doFinal(dataCheckString.toString().getBytes(StandardCharsets.UTF_8));
            String computedHex = bytesToHex(computed);

            return computedHex.equalsIgnoreCase(hash);
        } catch (Exception e) {
            return false;
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
