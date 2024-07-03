package io.github.stackpan.examia.server.http.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {
        "classpath:datasources/drop.sql",
        "classpath:datasources/schema.sql",
        "classpath:datasources/testdata/user.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    public class Login {

        @Test
        void shouldReturnJwt() throws Exception {
            var requestBody = """
                    {
                        "username": "user",
                        "password": "examiauser-Secret0"
                    }
                    """;

            mockMvc.perform(
                            post("/auth/login")
                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                    .accept(MediaType.APPLICATION_JSON_VALUE)
                                    .content(requestBody))
                    .andExpect(status().isOk())
                    .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                    .andExpectAll(
                            jsonPath("$.token").isString(),
                            jsonPath("$.scopes[0]").value("USER"));
        }

        @Test
        void wrongCredentialsShouldReturnUnathorized() throws Exception {
            var requestBody = """
                    {
                        "username": "wrong",
                        "password": "wrongpassword"
                    }
                    """;

            mockMvc.perform(
                            post("/auth/login")
                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                    .accept(MediaType.APPLICATION_JSON_VALUE)
                                    .content(requestBody))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    public class Me {

        @Test
        void shouldReturnCurrentUser() throws Exception {
            var token = setupLogin();

            mockMvc.perform(
                            get("/auth/me")
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isOk())
                    .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                    .andExpectAll(
                            jsonPath("id").value("157e4056-3a6e-4410-bc15-f14ea86887b6"),
                            jsonPath("username").value("user"),
                            jsonPath("email").value("user@example.com"),
                            jsonPath("firstName").value("First"),
                            jsonPath("lastName").value("Last"),
                            jsonPath("role").value("USER"));
        }

        @Test
        void invalidTokenShouldReturnUnauthorized() throws Exception {
            var token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjRlY2U1ZmI0MjI4M2JhNjgwYWNlOTk1ZjA5ODc5NjA5IiwidHlwIjoiSldUIn0.eyJpc3MiOiJodHRwczovL2lkcC5sb2NhbCIsImF1ZCI6Im15X2NsaWVudF9hcHAiLCJzdWIiOiI1YmU4NjM1OTA3M2M0MzRiYWQyZGEzOTMyMjIyZGFiZSIsImV4cCI6MTcyMDAxNDg1NywiaWF0IjoxNzIwMDE0NTU3fQ.DRQBi4FJRjpg5iEh_qyz6sd8lwz9mupT2pii1RypkUBj0odquGRuYMjqLOAz1EC0KRCdt7tHC9ExgHj05Aa6TRHzGocOmd67iDEGf2o61APmXjn85W7egZjgzVHKZDLrpaGDDpFTAZZv4DapncKsOciLt6KZoHn9BDOqN5BNwF6SA_DYOT83QOsYwn-H4FVaBc3oiLonCMv5-uypkusmut9Z5cd3aItWNC8F7t2rl20QHY006DdPBNIv8zBxOf0knZ-CZPqNQh7OmCQdWRgFbSC-3dww2On_w0W7AqOArCe7PoRk3dz4Y7SRlj1gM59ifSgTXHxpXU1Ic5LhNZp9bA";

            mockMvc.perform(
                            get("/auth/me")
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token)))
                    .andExpect(status().isUnauthorized());
        }

        private String setupLogin() throws Exception {
            var requestBody = """
                    {
                        "username": "user",
                        "password": "examiauser-Secret0"
                    }
                    """;

            var result = mockMvc.perform(
                            post("/auth/login")
                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                    .accept(MediaType.APPLICATION_JSON_VALUE)
                                    .content(requestBody))
                    .andReturn();

            return JsonPath.read(result.getResponse().getContentAsString(), "$.token");
        }
    }

}
