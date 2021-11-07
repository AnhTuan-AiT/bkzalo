package com.hust.bkzalo.filter;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.bkzalo.entity.Account;
import com.hust.bkzalo.repo.AccountRepo;
import com.hust.bkzalo.utils.BaseHTTP;
import com.hust.bkzalo.utils.Validator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.hust.bkzalo.utils.JwtTokenUtils.*;

@AllArgsConstructor(onConstructor_ = @Autowired)
public class JwtUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final AccountRepo accountRepo;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String phoneNumber = request.getParameter("username");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(phoneNumber, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();

        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_TOKEN_EXPIRATION_DURATION)).withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_EXPIRATION_DURATION))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        Account account = accountRepo.findByPhoneNumber(user.getUsername());
        Map<String, Object> responseBody = new HashMap<String, Object>() {{
            put("code", String.valueOf(BaseHTTP.CODE_1000));
            put("message", BaseHTTP.MESSAGE_1000);
            put("data", new HashMap<String, Object>() {{
                put("id", account.getId());
                put("username", account.getUsername());
                put("token", new HashMap<String, String>() {{
                    put("access_token", access_token);
                    put("refresh_token", refresh_token);
                }});
                put("avatar", account.getAvatarURL());
            }});
        }};

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
        SecurityContextHolder.getContext().setAuthentication(authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // test case 6 -> auto.
        request.setCharacterEncoding("utf-8");
        String phoneNumber = request.getParameter("username");
        String password = request.getParameter("password");
//        Gson gson = new Gson();
//        SignInRequest signInRequest = gson.fromJson(request.getReader(), SignInRequest.class); //Body JSON

        Map<String, String> responseBody = new HashMap<>();
        if (phoneNumber == null || password == null) {
            invalidParameter(responseBody, null);
            writeResponse(response, responseBody);// done test case 5.
        }

        if (!Validator.isValid(phoneNumber)) {
            invalidParameter(responseBody, "Số điện thoại không đúng định dạng");
            writeResponse(response, responseBody);// done test case 3.
        }

        if (password.equals(phoneNumber) || !Validator.isValidPassword(password)) {
            invalidParameter(responseBody, "Mật khẩu không đúng định dạng");
            writeResponse(response, responseBody);// done test case 4.
        }

        responseBody.put("code", String.valueOf(BaseHTTP.CODE_9995));
        responseBody.put("message", BaseHTTP.MESSAGE_9995);
        writeResponse(response, responseBody); // done test case 2;
    }

    private void invalidParameter(Map<String, String> response, String data) {
        response.put("code", String.valueOf(BaseHTTP.CODE_1004));
        response.put("message", BaseHTTP.MESSAGE_1004);
        response.put("data", data);
    }

    private void writeResponse(HttpServletResponse response, Map<String, String> body) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), body);
    }
}
