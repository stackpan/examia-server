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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:datasources/reset.sql", "classpath:datasources/schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:datasources/testdata/case.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class CaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    class ListCases {

        @Test
        void success() throws Exception {
            mockMvc.perform(get("/cases"))
                    .andExpect(status().isOk())
                    .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/hal+json"))
                    .andExpectAll(
                            jsonPath("$._embedded.caseResourceList.length()").value(3),
                            jsonPath("$._embedded.caseResourceList[*].id").value(containsInAnyOrder("2eef6095-06af-4c07-b989-795d64c86625", "b9a5ebc1-b77f-495d-9f28-5a181bf543bf", "f22a0d17-f8ba-44fb-b003-444124f6d1d3")),
                            jsonPath("$._embedded.caseResourceList[*].title").value(containsInAnyOrder("Case Title 1", "Case Title 2", "Case Title 3")),
                            jsonPath("$._embedded.caseResourceList[*].description").value(containsInAnyOrder("Case 1 Description.", "Case 2 Description.", "Case 3 Description.")),
                            jsonPath("$._embedded.caseResourceList[*].durationInSeconds").value(containsInAnyOrder(1800, 1900, 2000)),
                            jsonPath("$._embedded.caseResourceList[*].owner.id").value(containsInAnyOrder("157e4056-3a6e-4410-bc15-f14ea86887b6", "157e4056-3a6e-4410-bc15-f14ea86887b6", "157e4056-3a6e-4410-bc15-f14ea86887b6")),
                            jsonPath("$._embedded.caseResourceList[*].owner.username").value(containsInAnyOrder("user", "user", "user")),
                            jsonPath("$._embedded.caseResourceList[*].owner.email").value(containsInAnyOrder("user@example.com", "user@example.com", "user@example.com")),
                            jsonPath("$._embedded.caseResourceList[*].owner.firstName").value(containsInAnyOrder("First", "First", "First")),
                            jsonPath("$._embedded.caseResourceList[*].owner.lastName").value(containsInAnyOrder("Last", "Last", "Last")),
                            jsonPath("$._embedded.caseResourceList[*].createdAt").value(containsInAnyOrder("2024-05-16T00:00:01Z", "2024-05-16T00:00:02Z", "2024-05-16T00:00:03Z")),
                            jsonPath("$._embedded.caseResourceList[*].updatedAt").value(containsInAnyOrder("2024-05-16T00:00:01Z", "2024-05-16T00:00:02Z", "2024-05-16T00:00:03Z")),
                            jsonPath("$._embedded.caseResourceList[*]._links.self.href").value(containsInAnyOrder(containsString("/cases/2eef6095-06af-4c07-b989-795d64c86625"), containsString("/cases/b9a5ebc1-b77f-495d-9f28-5a181bf543bf"), containsString("/cases/f22a0d17-f8ba-44fb-b003-444124f6d1d3"))),
                            jsonPath("$._links.self.href").value(containsString("/cases")),
                            jsonPath("$.page.size").value(10),
                            jsonPath("$.page.totalElements").value(3),
                            jsonPath("$.page.totalPages").value(1),
                            jsonPath("$.page.number").value(0)
                    );
        }
    }

    @Nested
    class GetCase {

        @Test
        void success() throws Exception {
            var caseId = "2eef6095-06af-4c07-b989-795d64c86625";

            mockMvc.perform(get("/cases/%s".formatted(caseId)))
                    .andExpect(status().isOk())
                    .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/hal+json"))
                    .andExpectAll(
                            jsonPath("$.id").value(caseId),
                            jsonPath("$.title").value("Case Title 1"),
                            jsonPath("$.description").value("Case 1 Description."),
                            jsonPath("$.durationInSeconds").value(1800),
                            jsonPath("$.owner.id").value("157e4056-3a6e-4410-bc15-f14ea86887b6"),
                            jsonPath("$.owner.username").value("user"),
                            jsonPath("$.owner.email").value("user@example.com"),
                            jsonPath("$.owner.firstName").value("First"),
                            jsonPath("$.owner.lastName").value("Last"),
                            jsonPath("$.createdAt").value("2024-05-16T00:00:01Z"),
                            jsonPath("$.updatedAt").value("2024-05-16T00:00:01Z"),
                            jsonPath("$._links.self.href").value(containsString("/cases/2eef6095-06af-4c07-b989-795d64c86625")),
                            jsonPath("$._links.cases.href").value(containsString("/cases"))
                    );
        }

        @Test
        void notFound() {
            var caseIds = List.of("17e3f075-4d00-4d70-ba24-68123777f0", "1", "CASE-001", "not-exists");

            caseIds.forEach(caseId -> {
                try {
                    mockMvc.perform(get("/cases/%s".formatted(caseId)))
                            .andExpect(status().isNotFound())
                            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                            .andExpect(jsonPath("$.errors").value("Cannot find case with identity: %s".formatted(caseId)));
                } catch (Exception e) {
                    fail(e);
                }
            });
        }
    }

    @Nested
    class CreateCase {

    }

    @Nested
    class UpdateCase {

    }

    @Nested
    class DeleteCase {

    }

}
