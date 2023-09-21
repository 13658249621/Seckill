package cn.hfbin.seckill.util;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionUtils {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int IV_LENGTH = 16;
    private static final int KEY_LENGTH = 128;
    private static final int ITERATION_COUNT = 10000;
    private static final byte[] salt = new byte[] { 0x74, (byte) 0x98, 0x6f, 0x6b, 0x6c, 0x6a, 0x6b, 0x6f, 0x6b, 0x6c, 0x6a, 0x6b, 0x6f, 0x6b, 0x6c, 0x6a };
    private static final String password = "password";
    public static String encrypt(String plaintext) throws Exception {
        byte[] salt = generateSalt();
        SecretKey secretKey = generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        AlgorithmParameters params = cipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();

        byte[] encryptedText = cipher.doFinal(plaintext.getBytes("UTF-8"));
        byte[] combined = new byte[salt.length + iv.length + encryptedText.length];
        System.arraycopy(salt, 0, combined, 0, salt.length);
        System.arraycopy(iv, 0, combined, salt.length, iv.length);
        System.arraycopy(encryptedText, 0, combined, salt.length + iv.length, encryptedText.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    public static String decrypt(String encryptedText) throws Exception {
        byte[] combined = Base64.getDecoder().decode(encryptedText);
        byte[] salt = new byte[KEY_LENGTH / 8];
        byte[] iv = new byte[IV_LENGTH];
        byte[] encrypted = new byte[combined.length - salt.length - iv.length];

        System.arraycopy(combined, 0, salt, 0, salt.length);
        System.arraycopy(combined, salt.length, iv, 0, iv.length);
        System.arraycopy(combined, salt.length + iv.length, encrypted, 0, encrypted.length);

        SecretKey secretKey = generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

        byte[] decryptedText = cipher.doFinal(encrypted);
        return new String(decryptedText, "UTF-8");
    }

    private static byte[] generateSalt() {
        System.out.println("salt: " + Base64.getEncoder().encodeToString(salt));
        return salt;
    }

    private static SecretKey generateKey() throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }

    public static void main(String[] args) throws Exception {
//        String plaintext = "Hello, World!";
//
//        try {
//            String encryptedText = encrypt(plaintext);
//            System.out.println("Encrypted text: " + encryptedText);
//
//            String decryptedText = decrypt(encryptedText);
//            System.out.println("Decrypted text: " + decryptedText);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        System.out.println(decrypt("dJhva2xqa29rbGprb2tsagGxW3+q+S9Yyl7kJg7LwBUcxODzFkyNJatPIqSLvXqQ"));
    }
}
