package com.eldar.challenge.ejercicio2.utils;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class Encryption {
    private static final String ALGORITHM = "AES";

    // Encriptar
    public static String encryptCVV(String cvv, String secretKey) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encrypted = cipher.doFinal(cvv.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // Desencriptar
    public static String decryptCVV(String encryptedCVV, String secretKey) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedCVV));
        return new String(decrypted);
    }
}
