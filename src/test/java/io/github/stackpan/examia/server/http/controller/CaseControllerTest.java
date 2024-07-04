package io.github.stackpan.examia.server.http.controller;

import com.jayway.jsonpath.JsonPath;
import io.github.stackpan.examia.server.util.Regexps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;


import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:datasources/drop.sql", "classpath:datasources/schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {"classpath:datasources/testdata/user.sql", "classpath:datasources/testdata/case.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class CaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Nested
    class ListCases {

        @Test
        void shouldReturnPaginatedCases() throws Exception {
            mockMvc.perform(get("/cases")
                            .with(jwt().jwt(jwt -> jwt
                                    .claim("sub", "157e4056-3a6e-4410-bc15-f14ea86887b6")
                                    .claim("scope", "USER"))))
                    .andExpect(status().isOk())
                    .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/hal+json"))
                    .andExpectAll(
                            jsonPath("$._embedded.cases.length()").value(3),
                            jsonPath("$._embedded.cases[*].id").value(containsInAnyOrder("2eef6095-06af-4c07-b989-795d64c86625", "b9a5ebc1-b77f-495d-9f28-5a181bf543bf", "f22a0d17-f8ba-44fb-b003-444124f6d1d3")),
                            jsonPath("$._embedded.cases[*].title").value(containsInAnyOrder("Case Title 1", "Case Title 2", "Case Title 3")),
                            jsonPath("$._embedded.cases[*].description").value(containsInAnyOrder("Case 1 Description.", "Case 2 Description.", "Case 3 Description.")),
                            jsonPath("$._embedded.cases[*].durationInSeconds").value(containsInAnyOrder(1800, 1900, 2000)),
                            jsonPath("$._embedded.cases[*].user.id").value(containsInAnyOrder("157e4056-3a6e-4410-bc15-f14ea86887b6", "157e4056-3a6e-4410-bc15-f14ea86887b6", "157e4056-3a6e-4410-bc15-f14ea86887b6")),
                            jsonPath("$._embedded.cases[*].user.username").value(containsInAnyOrder("user", "user", "user")),
                            jsonPath("$._embedded.cases[*].user.email").value(containsInAnyOrder("user@example.com", "user@example.com", "user@example.com")),
                            jsonPath("$._embedded.cases[*].user.firstName").value(containsInAnyOrder("First", "First", "First")),
                            jsonPath("$._embedded.cases[*].user.lastName").value(containsInAnyOrder("Last", "Last", "Last")),
                            jsonPath("$._embedded.cases[*].user.role").value(containsInAnyOrder("USER", "USER", "USER")),
                            jsonPath("$._embedded.cases[*].createdAt").value(containsInAnyOrder("2024-05-16T00:00:01Z", "2024-05-16T00:00:02Z", "2024-05-16T00:00:03Z")),
                            jsonPath("$._embedded.cases[*].updatedAt").value(containsInAnyOrder("2024-05-16T00:00:01Z", "2024-05-16T00:00:02Z", "2024-05-16T00:00:03Z")),
                            jsonPath("$._embedded.cases[*]._links.self.href").value(containsInAnyOrder(containsString("/cases/2eef6095-06af-4c07-b989-795d64c86625"), containsString("/cases/b9a5ebc1-b77f-495d-9f28-5a181bf543bf"), containsString("/cases/f22a0d17-f8ba-44fb-b003-444124f6d1d3"))),
                            jsonPath("$._links.self.href").value(containsString("/cases")),
                            jsonPath("$.page.size").value(10),
                            jsonPath("$.page.totalElements").value(3),
                            jsonPath("$.page.totalPages").value(1),
                            jsonPath("$.page.number").value(0));
        }

        @Test
        void shouldReturnAppropriatePage() throws Exception {
            generateCaseDummies(17);

            mockMvc.perform(get("/cases?page=1")
                            .with(jwt().jwt(jwt -> jwt
                                    .claim("sub", "157e4056-3a6e-4410-bc15-f14ea86887b6")
                                    .claim("scope", "USER"))))
                    .andExpect(status().isOk())
                    .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/hal+json"))
                    .andExpectAll(
                            jsonPath("$._embedded.cases.length()").value(10),
                            jsonPath("$.page.size").value(10),
                            jsonPath("$.page.totalElements").value(20),
                            jsonPath("$.page.totalPages").value(2),
                            jsonPath("$.page.number").value(1));
        }

        @Test
        void withSizeShouldReturnAppropriatePageSize() throws Exception {
            generateCaseDummies(12);

            mockMvc.perform(get("/cases?size=5")
                            .with(jwt().jwt(jwt -> jwt
                                    .claim("sub", "157e4056-3a6e-4410-bc15-f14ea86887b6")
                                    .claim("scope", "USER"))))
                    .andExpect(status().isOk())
                    .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/hal+json"))
                    .andExpectAll(
                            jsonPath("$._embedded.cases.length()").value(5),
                            jsonPath("$.page.size").value(5),
                            jsonPath("$.page.totalElements").value(15),
                            jsonPath("$.page.totalPages").value(3),
                            jsonPath("$.page.number").value(0));
        }
    }

    @Nested
    class GetCase {

        @Test
        void shouldReturnCase() throws Exception {
            var caseId = "2eef6095-06af-4c07-b989-795d64c86625";

            mockMvc.perform(get("/cases/%s".formatted(caseId))
                            .with(jwt().jwt(jwt -> jwt
                                    .claim("sub", "157e4056-3a6e-4410-bc15-f14ea86887b6")
                                    .claim("scope", "USER"))))
                    .andExpect(status().isOk())
                    .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/hal+json"))
                    .andExpectAll(
                            jsonPath("$.id").value(caseId),
                            jsonPath("$.title").value("Case Title 1"),
                            jsonPath("$.description").value("Case 1 Description."),
                            jsonPath("$.durationInSeconds").value(1800),
                            jsonPath("$.user.id").value("157e4056-3a6e-4410-bc15-f14ea86887b6"),
                            jsonPath("$.user.username").value("user"),
                            jsonPath("$.user.email").value("user@example.com"),
                            jsonPath("$.user.firstName").value("First"),
                            jsonPath("$.user.lastName").value("Last"),
                            jsonPath("$.user.role").value("USER"),
                            jsonPath("$.createdAt").value("2024-05-16T00:00:01Z"),
                            jsonPath("$.updatedAt").value("2024-05-16T00:00:01Z"),
                            jsonPath("$._links.self.href").value(containsString("/cases/2eef6095-06af-4c07-b989-795d64c86625")),
                            jsonPath("$._links.cases.href").value(containsString("/cases")));
        }

        @Test
        void toUnknownIdShouldReturnNotFound() throws Exception {
            String[] caseIds = {"17e3f075-4d00-4d70-ba24-68123777f0", "1", "CASE-001", "not-exists"};

            for (var caseId : caseIds) {
                mockMvc.perform(get("/cases/%s".formatted(caseId))
                                .with(jwt().jwt(jwt -> jwt
                                        .claim("sub", "157e4056-3a6e-4410-bc15-f14ea86887b6")
                                        .claim("scope", "USER"))))
                        .andExpect(status().isNotFound())
                        .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(jsonPath("$.errors").value("Cannot find Case with identity: %s".formatted(caseId)));
            }
        }

        @Test
        void toUnathorizedCaseShouldReturnNotFound() throws Exception {
            var caseId = "527fa3f0-449e-46f5-ac2a-ffd39e7e539f";

            mockMvc.perform(get("/cases/%s".formatted(caseId))
                            .with(jwt().jwt(jwt -> jwt
                                    .claim("sub", "157e4056-3a6e-4410-bc15-f14ea86887b6")
                                    .claim("scope", "USER"))))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errors").value("Cannot find Case with identity: %s".formatted(caseId)));
        }
    }

    @Nested
    class CreateCase {

        @Test
        void shouldReturnCreatedAndExistsOnDatabase() throws Exception {
            var requestBody = """
                    {
                        "title": "New Case",
                        "description": "A long text of new case description",
                        "durationInSeconds": 2100
                    }
                    """;

            mockMvc.perform(post("/cases")
                            .with(jwt().jwt(jwt -> jwt
                                    .claim("sub", "157e4056-3a6e-4410-bc15-f14ea86887b6")
                                    .claim("scope", "USER")))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept("application/hal+json")
                            .content(requestBody))
                    .andExpect(status().isCreated())
                    .andExpectAll(
                            header().string(HttpHeaders.CONTENT_TYPE, "application/hal+json"),
                            header().string(HttpHeaders.LOCATION, matchesPattern("^.*/cases/" + Regexps.UUID)))
                    .andExpectAll(
                            jsonPath("$.id", matchesPattern(Regexps.UUID)),
                            jsonPath("$.title").value(JsonPath.<String>read(requestBody, "$.title")),
                            jsonPath("$.description").value(JsonPath.<String>read(requestBody, "$.description")),
                            jsonPath("$.durationInSeconds").value(JsonPath.<String>read(requestBody, "$.durationInSeconds")),
                            jsonPath("$.user.id").value("157e4056-3a6e-4410-bc15-f14ea86887b6"),
                            jsonPath("$.user.username").value("user"),
                            jsonPath("$.user.email").value("user@example.com"),
                            jsonPath("$.user.firstName").value("First"),
                            jsonPath("$.user.lastName").value("Last"),
                            jsonPath("$.user.role").value("USER"),
                            jsonPath("$.createdAt", matchesPattern(Regexps.TIMESTAMP)),
                            jsonPath("$.updatedAt", matchesPattern(Regexps.TIMESTAMP)),
                            jsonPath("$._links.self.href", matchesPattern("^.*/cases/" + Regexps.UUID)),
                            jsonPath("$._links.cases.href").value(containsString("/cases")))
                    .andDo(result -> {
                        var responseContent = result.getResponse().getContentAsString();

                        var id = JsonPath.<String>read(responseContent, "$.id");
                        var count = jdbcTemplate.queryForObject("SELECT count(*) FROM cases WHERE id = ?", Integer.class, UUID.fromString(id));

                        assertEquals(1, count);
                    });
        }

        @Test
        void invalidPayloadShouldReturnBadRequest() throws Exception {
            var requestBody = """
                    {
                        "title": true,
                        "durationInSeconds": -900
                    }
                    """;

            mockMvc.perform(post("/cases")
                            .with(jwt().jwt(jwt -> jwt
                                    .claim("sub", "157e4056-3a6e-4410-bc15-f14ea86887b6")
                                    .claim("scope", "USER")))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept("application/hal+json")
                            .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                    .andExpectAll(
                            jsonPath("$.errors.title").exists(),
                            jsonPath("$.errors.durationInSeconds").exists());
        }
    }

    @Nested
    class UpdateCase {

        private final String targetId = "2eef6095-06af-4c07-b989-795d64c86625";

        private Map<String, Object> before;

        @BeforeEach
        void setUp() {
            before = jdbcTemplate.queryForMap("SELECT * FROM cases WHERE id = ?", UUID.fromString(targetId));
        }

        @Test
        void shouldReturnNoContentAndChangedOnDatabase() throws Exception {
            var requestBody = """
                    {
                        "title": "Updated case Title 1",
                        "description": "Updated case 1 Description.",
                        "durationInSeconds": 1700
                    }
                    """;

            mockMvc.perform(put("/cases/%s".formatted(targetId))
                            .with(jwt().jwt(jwt -> jwt
                                    .claim("sub", "157e4056-3a6e-4410-bc15-f14ea86887b6")
                                    .claim("scope", "USER")))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(requestBody))
                    .andExpect(status().isNoContent());

            var after = jdbcTemplate.queryForMap("SELECT * FROM cases WHERE id = ?", UUID.fromString(targetId));

            assertNotEquals(before.get("title"), after.get("title"));
            assertEquals(JsonPath.<String>read(requestBody, "$.title"), after.get("title"));
            assertNotEquals(before.get("description"), after.get("description"));
            assertEquals(JsonPath.<String>read(requestBody, "$.description"), after.get("description"));
            assertNotEquals(before.get("duration_in_seconds"), after.get("duration_in_seconds"));
            assertEquals(JsonPath.<String>read(requestBody, "$.durationInSeconds"), after.get("duration_in_seconds"));
            assertNotEquals(before.get("updated_at"), after.get("updated_at"));
        }

        @Test
        void invalidPayloadShouldReturnBadRequest() throws Exception {
            var requestBody = """
                    {
                        "title": true,
                        "durationInSeconds": -900
                    }
                    """;

            mockMvc.perform(put("/cases/%s".formatted(targetId))
                            .with(jwt().jwt(jwt -> jwt
                                    .claim("sub", "157e4056-3a6e-4410-bc15-f14ea86887b6")
                                    .claim("scope", "USER")))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                    .andExpectAll(
                            jsonPath("$.errors.title").exists(),
                            jsonPath("$.errors.durationInSeconds").exists());
        }

        @Test
        void toUnknownIdShouldReturnNotFound() throws Exception {
            String[] caseIds = {"17e3f075-4d00-4d70-ba24-68123777f0", "1", "CASE-001", "not-exists"};

            var requestBody = """
                    {
                        "title": "Updated case Title 1",
                        "description": "Updated case 1 Description.",
                        "durationInSeconds": 1700
                    }
                    """;

            for (var caseId : caseIds) {
                mockMvc.perform(put("/cases/%s".formatted(caseId))
                                .with(jwt().jwt(jwt -> jwt
                                        .claim("sub", "157e4056-3a6e-4410-bc15-f14ea86887b6")
                                        .claim("scope", "USER")))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(requestBody))
                        .andExpect(status().isNotFound())
                        .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(jsonPath("$.errors").value("Cannot find Case with identity: %s".formatted(caseId)));
            }
        }

        @Test
        void toUnauthorizedCaseShouldReturnNotFound() throws Exception {
            var caseId = "527fa3f0-449e-46f5-ac2a-ffd39e7e539f";

            var requestBody = """
                    {
                        "title": "Updated case Title 4",
                        "description": "Updated case 4 Description.",
                        "durationInSeconds": 2150
                    }
                    """;

            mockMvc.perform(put("/cases/%s".formatted(caseId))
                            .with(jwt().jwt(jwt -> jwt
                                    .claim("sub", "157e4056-3a6e-4410-bc15-f14ea86887b6")
                                    .claim("scope", "USER")))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(requestBody))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errors").value("Cannot find Case with identity: %s".formatted(caseId)));
        }
    }

    @Nested
    class DeleteCase {

        private final String targetId = "2eef6095-06af-4c07-b989-795d64c86625";

        @Test
        void shouldReturnNoContentAndDisappearedOnDatabase() throws Exception {
            mockMvc.perform(delete("/cases/%s".formatted(targetId))
                            .with(jwt().jwt(jwt -> jwt
                                    .claim("sub", "157e4056-3a6e-4410-bc15-f14ea86887b6")
                                    .claim("scope", "USER"))))
                    .andExpect(status().isNoContent());

            var count = jdbcTemplate.queryForObject("SELECT count(*) FROM cases WHERE id = ?", Integer.class, UUID.fromString(targetId));

            assertEquals(0, count);
        }

        @Test
        void toUnknownIdShouldReturnNotFound() throws Exception {
            String[] caseIds = {"17e3f075-4d00-4d70-ba24-68123777f0", "1", "CASE-001", "not-exists"};

            for (var caseId : caseIds) {
                mockMvc.perform(delete("/cases/%s".formatted(caseId))
                                .with(jwt().jwt(jwt -> jwt
                                        .claim("sub", "157e4056-3a6e-4410-bc15-f14ea86887b6")
                                        .claim("scope", "USER"))))
                        .andExpect(status().isNotFound());
            }
        }

        @Test
        void toUnauthorizedCaseShouldReturnNotFound() throws Exception {
            var caseId = "527fa3f0-449e-46f5-ac2a-ffd39e7e539f";

            mockMvc.perform(delete("/cases/%s".formatted(caseId))
                            .with(jwt().jwt(jwt -> jwt
                                    .claim("sub", "157e4056-3a6e-4410-bc15-f14ea86887b6")
                                    .claim("scope", "USER"))))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errors").value("Cannot find Case with identity: %s".formatted(caseId)));
        }
    }

    private void generateCaseDummies(int count) {
        List<Object> argList = new ArrayList<>();
        var sqlBuilder = new StringBuilder("INSERT INTO cases (id, title, description, duration_in_seconds, user_id, created_at, updated_at) VALUES ");

        for (int i = 0; i < count; i++) {
            argList.add(UUID.randomUUID());
            argList.add("Case Title 1%s".formatted(i));
            argList.add("Case 1%d Description.".formatted(i));
            argList.add(2000 + (i * 10));
            argList.add(UUID.fromString("157e4056-3a6e-4410-bc15-f14ea86887b6"));
            argList.add(new Timestamp(new Date().getTime()));
            argList.add(new Timestamp(new Date().getTime()));

            sqlBuilder.append("(?, ?, ?, ?, ?, ?, ?)");
            sqlBuilder.append((i < count - 1) ? ", " : ";"); // Use semicolon at the end of the query
        }

        List<Object[]> batchArgs = new ArrayList<>();
        batchArgs.add(argList.toArray());

        jdbcTemplate.batchUpdate(sqlBuilder.toString(), batchArgs);
    }

}
