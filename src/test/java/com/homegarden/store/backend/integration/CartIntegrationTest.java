package com.homegarden.store.backend.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homegarden.store.backend.repository.UserRepository;
import com.homegarden.store.backend.service.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Transactional
public class CartIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

//    @Autowired
//    private UserRepository userRepository;

    String tokenUser;

    String tokenAdmin;

    @BeforeEach
    void setup() throws Exception {
        String loginUser = """
                {
                    "userEmail": "user-1@example.com",
                    "password": "password1"
                }
                """;
        MvcResult mvcResultUser = mockMvc.perform(post("/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginUser))
                .andExpect(status().isOk())
                .andReturn();
        String responseUser = mvcResultUser.getResponse().getContentAsString();
        JsonNode jsonNodeUser = objectMapper.readTree(responseUser);
        tokenUser = jsonNodeUser.get("token").asText();

        String loginAdmin = """
                {
                    "userEmail": "admin@example.com",
                    "password": "password6"
                }
                """;
        MvcResult mvcResultAdmin = mockMvc.perform(post("/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginAdmin))
                .andExpect(status().isOk())
                .andReturn();
        String responseAdmin = mvcResultAdmin.getResponse().getContentAsString();
        JsonNode jsonNodeAdmin = objectMapper.readTree(responseAdmin);
        tokenAdmin = jsonNodeAdmin.get("token").asText();
    }

    @Test
    void getAllCartItemsTest() throws Exception {

        mockMvc.perform(get("/v1/carts")
                        .header("Authorization", "Bearer " + tokenUser))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}