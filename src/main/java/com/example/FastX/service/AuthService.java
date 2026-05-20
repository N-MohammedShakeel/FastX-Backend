package com.example.FastX.service;

import com.example.FastX.dto.ForgotPasswordDTO;
import com.example.FastX.dto.LoginDTO;
import com.example.FastX.dto.UserRegisterDTO;
import com.example.FastX.exception.ResourceNotFoundException;
import jakarta.mail.MessagingException;

public interface AuthService {

    String registerPassenger(UserRegisterDTO dto);
    String registerOperator(UserRegisterDTO dto);
    String login(LoginDTO dto);
    String forgotPassword(ForgotPasswordDTO dto) throws ResourceNotFoundException, MessagingException;
}