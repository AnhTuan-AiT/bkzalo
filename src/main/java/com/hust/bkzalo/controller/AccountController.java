package com.hust.bkzalo.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.bkzalo.entity.Account;
import com.hust.bkzalo.entity.Role;
import com.hust.bkzalo.model.SignUpIM;
import com.hust.bkzalo.service.AccountService;
import com.hust.bkzalo.utils.BaseHTTP;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.hust.bkzalo.utils.JwtTokenUtils.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@AllArgsConstructor(onConstructor_ = @Autowired)
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/users")
    public ResponseEntity<List<Account>> getUsers() {
        return ResponseEntity.ok().body(accountService.getUsers());
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);

                String username = decodedJWT.getSubject();
                Account account = accountService.getUser(username);

                String accessToken = JWT.create()
                        .withSubject(account.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + JWT_TOKEN_EXPIRATION_DURATION)).withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", account.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);

                writeToken2Response(response, accessToken, refreshToken);
            } catch (Exception e) {
                writUnauthorizedResponse(response, null, e.getMessage()); // TODO: consider value of 'code'
            }
        } else {
            writUnauthorizedResponse(response, String.valueOf(BaseHTTP.CODE_1004), "Refresh token is missing");
        }
    }

    private void writUnauthorizedResponse(HttpServletResponse response, String code, String message) throws IOException {
        Map<String, String> responseBody = new HashMap<String, String>() {{
            put("code", code);
            put("message", message);
        }};

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(UNAUTHORIZED.value());
        new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpIM signUpIM) {
        return ResponseEntity.ok().body(accountService.signUp(signUpIM));
    }

}
