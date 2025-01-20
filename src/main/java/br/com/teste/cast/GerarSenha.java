package br.com.teste.cast;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GerarSenha {
    public static void main(String[] args) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String rawPassword = "123";

        String encodedPassword = encoder.encode(rawPassword);

        System.out.println(encodedPassword);
    }
}
