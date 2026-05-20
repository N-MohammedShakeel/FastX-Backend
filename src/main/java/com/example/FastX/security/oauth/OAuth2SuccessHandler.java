package com.example.FastX.security.oauth;

import com.example.FastX.constants.AuthProvider;
import com.example.FastX.constants.Role;
import com.example.FastX.entity.User;
import com.example.FastX.repository.UserRepository;
import com.example.FastX.security.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Component
@AllArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder(12);

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        User user = userRepository.findByEmailAndActiveTrue(email);

        Role finalRole;

        if (user != null) {
            finalRole = user.getRole();
        } else {

            finalRole = Role.valueOf(getRoleFromCookie(request));

            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setProvider(AuthProvider.GOOGLE);
            newUser.setRole(finalRole);
            newUser.setPassword(encoder.encode("sample"));
            newUser.setPhone("0000000000");
            newUser.setGender("Not Specified");
            newUser.setAddress("Please update your address");

            userRepository.save(newUser);
        }

        String token = jwtUtil.generateToken(email, finalRole);

        clearRoleCookie(response);

        String redirectUrl = String.format(
                "http://localhost:5173/oauth-success?token=%s&role=%s",
                URLEncoder.encode(token, StandardCharsets.UTF_8),
                URLEncoder.encode(String.valueOf(finalRole), StandardCharsets.UTF_8)
        );

        response.sendRedirect(redirectUrl);
    }

    private String getRoleFromCookie(
            HttpServletRequest request
    ) {

        if (request.getCookies() == null)
            return "PASSENGER";

        return Arrays.stream(request.getCookies())
                .filter(cookie -> "pending_role".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse("PASSENGER");
    }

    private void clearRoleCookie(
            HttpServletResponse response
    ) {

        Cookie cookie = new Cookie("pending_role", null);

        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }
}