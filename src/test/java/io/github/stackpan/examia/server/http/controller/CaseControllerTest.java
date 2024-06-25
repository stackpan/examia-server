package io.github.stackpan.examia.server.http.controller;

import com.jayway.jsonpath.JsonPath;
import io.github.stackpan.examia.server.util.Regexps;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:datasources/drop.sql", "classpath:datasources/schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
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
                            jsonPath("$._embedded.caseResourceList[*].createdAt").value(containsInAnyOrder("2024-05-16T00:00:01Z", "2024-05-16T00:00:02Z", "2024-05-16T00:00:03Z")),
                            jsonPath("$._embedded.caseResourceList[*].updatedAt").value(containsInAnyOrder("2024-05-16T00:00:01Z", "2024-05-16T00:00:02Z", "2024-05-16T00:00:03Z")),
                            jsonPath("$._embedded.caseResourceList[*]._links.self.href").value(containsInAnyOrder(containsString("/cases/2eef6095-06af-4c07-b989-795d64c86625"), containsString("/cases/b9a5ebc1-b77f-495d-9f28-5a181bf543bf"), containsString("/cases/f22a0d17-f8ba-44fb-b003-444124f6d1d3"))),
                            jsonPath("$._links.self.href").value(containsString("/cases")),
                            jsonPath("$.page.size").value(10),
                            jsonPath("$.page.totalElements").value(3),
                            jsonPath("$.page.totalPages").value(1),
                            jsonPath("$.page.number").value(0));
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
                            jsonPath("$.createdAt").value("2024-05-16T00:00:01Z"),
                            jsonPath("$.updatedAt").value("2024-05-16T00:00:01Z"),
                            jsonPath("$._links.self.href").value(containsString("/cases/2eef6095-06af-4c07-b989-795d64c86625")),
                            jsonPath("$._links.cases.href").value(containsString("/cases")));
        }

        @Test
        void notFound() throws Exception {
            String[] caseIds = {"17e3f075-4d00-4d70-ba24-68123777f0", "1", "CASE-001", "not-exists"};

            for (var caseId : caseIds) {
                mockMvc.perform(get("/cases/%s".formatted(caseId)))
                        .andExpect(status().isNotFound())
                        .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(jsonPath("$.errors").value("Cannot find Case with identity: %s".formatted(caseId)));
            }
        }
    }

    @Nested
    class CreateCase {

        @Test
        void created() throws Exception {
            var requestBody = """
                    {
                        "title": "New Case",
                        "description": "A long text of new case description",
                        "durationInSeconds": 2100
                    }
                    """;

            mockMvc.perform(
                            post("/cases")
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
                            jsonPath("$.createdAt", matchesPattern(Regexps.TIMESTAMP)),
                            jsonPath("$.updatedAt", matchesPattern(Regexps.TIMESTAMP)),
                            jsonPath("$._links.self.href", matchesPattern("^.*/cases/" + Regexps.UUID)),
                            jsonPath("$._links.cases.href").value(containsString("/cases")));
        }

        @Test
        void badRequest() throws Exception {
            var requestBody = """
                    {
                        "title": true,
                        "durationInSeconds": -900
                    }
                    """;

            mockMvc.perform(
                            post("/cases")
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

    }

    @Nested
    class DeleteCase {

    }

}
