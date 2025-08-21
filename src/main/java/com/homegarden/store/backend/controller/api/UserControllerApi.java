package com.homegarden.store.backend.controller.api;

import com.homegarden.store.backend.dto.CreateUserRequestDto;
import com.homegarden.store.backend.dto.UpdateUserRequestDto;
import com.homegarden.store.backend.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Users", description = "Controller for working with users")
public interface UserControllerApi {

    @Operation(
            summary = "Get all users",
            description = "Retrieves a list of all registered users in the system"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(
                            name = "Success",
                            value = """
                                      [
                                        {
                                          "userId": 99,
                                          "username": "Dunya Smirnova",
                                          "email": "dunya@qi.com",
                                          "phoneNumber": "+491607654321",
                                          "password": "Password2",
                                          "role": "CLIENT",
                                          "favorites": null,
                                          "cart": null
                                        }
                                      ]
                                    """
                    ))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "Invalid request parameters"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "Access denied"))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "Users not found"))),
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "Conflict occurred while retrieving users")))
    })
    ResponseEntity<List<UserResponseDto>> getAll();

    @Operation(
            summary = "Create a new user",
            description = "Creates a new user and returns the created user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json")
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(
                            value = """
                                      {
                                        "userId": 100,
                                        "username": "Anna Petrova",
                                        "email": "anna@qi.com",
                                        "phoneNumber": "+491609999999",
                                        "password": "AnnaPass1",
                                        "role": "CLIENT",
                                        "favorites": null,
                                        "cart": null
                                      }
                                    """
                    ))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "Invalid email format or missing username"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "Access denied"))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "User not found"))),
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "User with this email already exists")))
    })
    ResponseEntity<UserResponseDto> create(@RequestBody @Valid CreateUserRequestDto userRequestDTO);

    @Operation(
            summary = "Update user",
            description = "Updates an existing user by userId",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json")
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(
                            value = """
                                      {
                                        "userId": 99,
                                        "username": "Dunya Updated",
                                        "email": "dunya@qi.com",
                                        "phoneNumber": "+491607654321",
                                        "password": "PasswordNew",
                                        "role": "CLIENT",
                                        "favorites": null,
                                        "cart": null
                                      }
                                    """
                    ))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "Invalid user update data"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "Access denied"))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "User not found for update"))),
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "Email already in use")))
    })
    ResponseEntity<UserResponseDto> update(@RequestBody @Valid UpdateUserRequestDto updateDto);

    @Operation(
            summary = "Get user by ID",
            description = "Returns a user by their unique identifier"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(
                            value = """
                                      {
                                        "userId": 99,
                                        "username": "Dunya Smirnova",
                                        "email": "dunya@qi.com",
                                        "phoneNumber": "+491607654321",
                                        "password": "Password2",
                                        "role": "CLIENT",
                                        "favorites": null,
                                        "cart": null
                                      }
                                    """
                    ))),
            @ApiResponse(responseCode = "400", description = "Invalid ID format",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "ID must be a number > 0"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "You do not have permission"))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "No user found with id 99"))),
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "User data is inconsistent")))
    })
    ResponseEntity<UserResponseDto> getById(@PathVariable @Min(1) Long userId);

    @Operation(
            summary = "Delete user",
            description = "Deletes a user by their ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid ID format",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "User ID must be positive"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "You are not allowed to delete this user"))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "User with id 99 not found"))),
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "Cannot delete user with active cart")))
    })
    ResponseEntity<Void> delete(@PathVariable @Min(1) Long userId);
}
