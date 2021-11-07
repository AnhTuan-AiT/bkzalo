package com.hust.bkzalo.utils;

import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenUtils {
    public static final long JWT_TOKEN_EXPIRATION_DURATION = 10 * 60 * 1000;

    public static final long JWT_REFRESH_TOKEN_EXPIRATION_DURATION = 30 * 60 * 1000;

    public static final String SECRET = "balo_secret";

    public static Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());

    public static void writeToken2Response(HttpServletResponse response, String access_token, String refresh_token) throws IOException {
        Map<String, String> tokens = new HashMap<>();

        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}
