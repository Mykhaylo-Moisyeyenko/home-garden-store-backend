package com.homegarden.store.backend.controller.api;

import com.homegarden.store.backend.dto.security.LoginRequestDto;
import com.homegarden.store.backend.dto.security.LoginResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Authentication", description = "Login endpoint for users")
public interface AuthentificationControllerApi {

    @Operation(
            summary = "User login",
            description = "Authenticates a user and returns a JWT token",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequestDto.class),
                            examples = @ExampleObject(
                                    name = "Login request",
                                    value = "{\"username\":\"user@example.com\",\"password\":\"strongpassword\"}"
                            )
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDto.class),
                            examples = @ExampleObject(
                                    name = "Successful login",
                                    value = "{\"token\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.abc.def\"}"
                            ))),
            @ApiResponse(responseCode = "400", description = "Missing or invalid fields",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Validation error",
                                    value = "{\"error\":\"Username and password must be provided\"}"
                            ))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid credentials",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Invalid credentials",
                                    value = "{\"error\":\"Invalid username or password\"}"
                            ))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "User not found",
                                    value = "{\"error\":\"User with given email not found\"}"
                            ))),
            @ApiResponse(responseCode = "409", description = "Account temporarily locked",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Locked account",
                                    value = "{\"error\":\"Too many failed login attempts. Try again later.\"}"
                            )))
    })
    ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto);
}
