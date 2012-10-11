package de.congrace.blog4j.tools;

import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

public class PasswordUtil {
    private static PasswordEncoder encoder = new ShaPasswordEncoder();

    public static void main(String[] args) {
        String pass = args[0];
        Object salt = args[1];
        System.out.println("SHA-1 with Salt(" + salt.toString() + "): " + encoder.encodePassword(pass, salt));
    }
}
