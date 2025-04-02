package edu.sia.credigo.config;

import java.security.SecureRandom;
import java.util.Base64;

public class JwtSecretGenerator {
    public static void main(String[] args) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[64];
        secureRandom.nextBytes(bytes);
        String secret = Base64.getEncoder().encodeToString(bytes);
        System.out.println("Generated JWT Secret: " + secret);
    }
} 