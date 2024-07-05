package io.github.stackpan.examia.server.http.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {
        "classpath:datasources/drop.sql",
        "classpath:datasources/schema.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {
        "classpath:datasources/testdata/user.sql",
        "classpath:datasources/testdata/case.sql",
        "classpath:datasources/testdata/issue.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class IssueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    public class ListIssues {

        @Test
        void shouldReturnPaginatedIssues() throws Exception {
            mockMvc.perform(get("/cases/%s/issues".formatted("2eef6095-06af-4c07-b989-795d64c86625"))
                            .with(jwt().jwt(jwt -> jwt
                                    .claim("sub", "157e4056-3a6e-4410-bc15-f14ea86887b6")
                                    .claim("scope", "USER"))))
                    .andExpect(status().isOk())
                    .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/hal+json"))
                    .andExpectAll(
                            jsonPath("$._embedded.issues[*].id").value(contains(
                                    "e7576fb6-c4f3-4b73-b9d9-0e1e67ac91d5",
                                    "8a12c2ed-747e-489c-ad01-32ca95932bc7",
                                    "f1fbc220-585b-422a-91bd-141378f07e38"
                            )),
                            jsonPath("$._embedded.issues[*].sequence").value(contains(1, 2, 3)),
                            jsonPath("$._embedded.issues[*].type").value(contains(
                                    "SINGLE_CHOICE",
                                    "MULTIPLE_CHOICE",
                                    "SINGLE_CHOICE"
                            )),
                            jsonPath("$._embedded.issues[*].body").value(contains(
                                    "porttitor lorem id ligula suspendisse ornare",
                                    "massa id nisl",
                                    "molestie lorem quisque ut erat"
                            )),
                            jsonPath("$._embedded.issues[*].createdAt").value(contains(
                                    "2024-07-05T00:00:01Z",
                                    "2024-07-05T00:00:02Z",
                                    "2024-07-05T00:00:03Z"
                            )),
                            jsonPath("$._embedded.issues[*].updatedAt").value(contains(
                                    "2024-07-05T00:00:01Z",
                                    "2024-07-05T00:00:02Z",
                                    "2024-07-05T00:00:03Z"
                            )),
                            jsonPath("$._embedded.issues[*]._links.self.href").value(contains(
                                    containsString("/cases/2eef6095-06af-4c07-b989-795d64c86625/issues/e7576fb6-c4f3-4b73-b9d9-0e1e67ac91d5"),
                                    containsString("/cases/2eef6095-06af-4c07-b989-795d64c86625/issues/8a12c2ed-747e-489c-ad01-32ca95932bc7"),
                                    containsString("/cases/2eef6095-06af-4c07-b989-795d64c86625/issues/f1fbc220-585b-422a-91bd-141378f07e38"))),
                            jsonPath("$._embedded.issues[*]._links.issues.href").value(contains(
                                    containsString("/cases/2eef6095-06af-4c07-b989-795d64c86625/issues"),
                                    containsString("/cases/2eef6095-06af-4c07-b989-795d64c86625/issues"),
                                    containsString("/cases/2eef6095-06af-4c07-b989-795d64c86625/issues"))),
                            jsonPath("$._links.self.href").value(containsString("/cases/2eef6095-06af-4c07-b989-795d64c86625/issues?")),
                            jsonPath("$.page.size").value(10),
                            jsonPath("$.page.totalElements").value(3),
                            jsonPath("$.page.totalPages").value(1),
                            jsonPath("$.page.number").value(0)
                    );
        }
    }

    @Nested
    public class GetIssue {

    }

    @Nested
    public class CreateIssue {

    }

    @Nested
    public class UpdateIssue {

    }

    @Nested
    public class DeleteIssue {

    }

}
