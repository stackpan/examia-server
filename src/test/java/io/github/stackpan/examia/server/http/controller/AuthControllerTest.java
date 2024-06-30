package io.github.stackpan.examia.server.http.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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
    public class Me {

        @Test
        void shouldReturnCurrentUser() throws Exception {
            mockMvc.perform(
                    get("/auth/me").with(httpBasic("user", "examiauser-Secret0")))
                    .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                    .andExpectAll(
                            jsonPath("id").value("157e4056-3a6e-4410-bc15-f14ea86887b6"),
                            jsonPath("username").value("user"),
                            jsonPath("email").value("user@example.com"),
                            jsonPath("firstName").value("First"),
                            jsonPath("lastName").value("Last"),
                            jsonPath("role").value("USER"));
        }
    }

}
