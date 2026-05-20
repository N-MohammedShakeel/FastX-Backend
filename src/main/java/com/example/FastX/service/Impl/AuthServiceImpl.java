package com.example.FastX.service.Impl;

import com.example.FastX.constants.AuthProvider;
import com.example.FastX.constants.Role;
import com.example.FastX.dto.ForgotPasswordDTO;
import com.example.FastX.dto.LoginDTO;
import com.example.FastX.dto.UserRegisterDTO;
import com.example.FastX.entity.User;
import com.example.FastX.exception.BadRequestException;
import com.example.FastX.exception.ResourceNotFoundException;
import com.example.FastX.security.model.UserPrincipal;
import com.example.FastX.repository.UserRepository;
import com.example.FastX.security.jwt.JwtUtil;
import com.example.FastX.service.AuthService;
import com.example.FastX.service.EmailService;
import com.example.FastX.util.Mapper;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final Mapper mapper;
    private final EmailService emailService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Override
    public String registerPassenger(UserRegisterDTO dto) {
        return registerWithRole(dto, Role.PASSENGER.name());
    }

    @Override
    public String registerOperator(UserRegisterDTO dto) {
        return registerWithRole(dto, Role.OPERATOR.name());
    }

    private String registerWithRole(UserRegisterDTO dto, String role) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            return "Email already registered";
        }

        User user = mapper.toUser(dto);
        user.setRole(Role.valueOf(role));
        user.setProvider(AuthProvider.LOCAL);

        userRepository.save(user);
        return role + " registered successfully";
    }

    @Override
    public String login(LoginDTO dto) {

        User user = userRepository.findByEmail(dto.getEmail());

        if (user != null && !user.isActive()) {
            throw new BadRequestException("Account has been deactivated");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return jwtUtil.generateToken(userPrincipal.getUsername(), userPrincipal.getRole());
    }

    @Override
    public String forgotPassword(ForgotPasswordDTO dto)
            throws MessagingException, ResourceNotFoundException {

        User user = userRepository.findByEmail(dto.getEmail());

        if (user == null) {
            throw new ResourceNotFoundException(
                    "User not found with this email"
            );
        }

        String tempPassword = generateRandomPassword();

        user.setPassword(
                encoder.encode(tempPassword)
        );

        userRepository.save(user);

        emailService.sendForgotPasswordMail(
                user,
                tempPassword
        );

        return "Temporary password sent to email";
    }

    private String generateRandomPassword() {

        String chars =
                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$";

        StringBuilder password =
                new StringBuilder();

        Random random = new Random();

        for (int i = 0; i < 10; i++) {

            password.append(
                    chars.charAt(
                            random.nextInt(chars.length())
                    )
            );
        }

        return password.toString();
    }
}