package site.metacoding.white.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.management.RuntimeErrorException;

import org.springframework.stereotype.Component;

@Component
public class SHA256 {

    // public String encrypt(String text) throws NoSuchAlgorithmException { 제어권을 나한테
    public String encrypt(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes());

            return bytesToHex(md.digest());
        } catch (Exception e) {
            throw new RuntimeException("해싱 실패");
        }

    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

}