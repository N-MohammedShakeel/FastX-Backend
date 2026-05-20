package com.example.FastX.controller;

import com.example.FastX.dto.*;
import com.example.FastX.exception.ResourceNotFoundException;
import com.example.FastX.service.Impl.AuthServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(
        name = "REST APIs for Account Creation in FastX",
        description = "REST APIs in FastX to REGISTER AND LOGIN user"
)
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Validated
public class AuthController {

    private AuthServiceImpl authService;

    @Operation(
            summary = "Create PASSENGER REST API",
            description = "REST API to create new user inside FastX"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    }
    )
    @PostMapping("/register/passenger")
    public ResponseEntity<ApiResponseDTO> registerPassenger(
            @Valid @RequestBody UserRegisterDTO dto
    ) {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.CREATED.value(),
                        "Passenger registered successfully",
                        authService.registerPassenger(dto)),
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Create OPERATOR REST API",
            description = "REST API to create new user inside FastX"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    }
    )
    @PostMapping("/register/operator")
    public ResponseEntity<ApiResponseDTO> registerOperator(
            @Valid @RequestBody UserRegisterDTO dto
    ) {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.CREATED.value(),
                        "Operator registered successfully",
                        authService.registerOperator(dto)),
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Authenticate USER REST API",
            description = "REST API to authenticate user inside FastX"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    }
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO> login(
            @Valid @RequestBody LoginDTO dto
    ) {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Login successful",
                        authService.login(dto)),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Create a mew account using google REST API",
            description = "REST API to create new user inside FastX"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    }
    )
    @GetMapping("/oauth2/start")
    public void startOAuth(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam @NotBlank(message = "Role is required") String role
    ) throws IOException {

        Cookie roleCookie = new Cookie("pending_role", role);
        roleCookie.setHttpOnly(true);
        roleCookie.setSecure(true);
        roleCookie.setPath("/");
        roleCookie.setMaxAge(300);
        response.addCookie(roleCookie);

        response.sendRedirect("/oauth2/authorization/google");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponseDTO> forgotPassword(
            @Valid
            @RequestBody
            ForgotPasswordDTO dto
    ) throws MessagingException, ResourceNotFoundException {

        return ResponseEntity.ok(
                new ApiResponseDTO(
                        HttpStatus.OK.value(),
                        authService.forgotPassword(dto),
                        null
                )
        );
    }
}