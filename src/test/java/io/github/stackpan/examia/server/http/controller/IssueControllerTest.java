package io.github.stackpan.examia.server.http.controller;

import io.github.stackpan.examia.server.data.enums.IssueType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.*;

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

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
                            jsonPath("$._embedded.issues[*].id").value(containsInAnyOrder(
                                    "e7576fb6-c4f3-4b73-b9d9-0e1e67ac91d5",
                                    "8a12c2ed-747e-489c-ad01-32ca95932bc7",
                                    "f1fbc220-585b-422a-91bd-141378f07e38"
                            )),
                            jsonPath("$._embedded.issues[*].type").value(containsInAnyOrder(
                                    "SINGLE_CHOICE",
                                    "MULTIPLE_CHOICE",
                                    "SINGLE_CHOICE"
                            )),
                            jsonPath("$._embedded.issues[*].body").value(containsInAnyOrder(
                                    "porttitor lorem id ligula suspendisse ornare",
                                    "massa id nisl",
                                    "molestie lorem quisque ut erat"
                            )),
                            jsonPath("$._embedded.issues[*].createdAt").value(containsInAnyOrder(
                                    "2024-07-05T00:00:01Z",
                                    "2024-07-05T00:00:02Z",
                                    "2024-07-05T00:00:03Z"
                            )),
                            jsonPath("$._embedded.issues[*].updatedAt").value(containsInAnyOrder(
                                    "2024-07-05T00:00:01Z",
                                    "2024-07-05T00:00:02Z",
                                    "2024-07-05T00:00:03Z"
                            )),
                            jsonPath("$._embedded.issues[*]._links.self.href").value(containsInAnyOrder(
                                    containsString("/cases/2eef6095-06af-4c07-b989-795d64c86625/issues/e7576fb6-c4f3-4b73-b9d9-0e1e67ac91d5"),
                                    containsString("/cases/2eef6095-06af-4c07-b989-795d64c86625/issues/8a12c2ed-747e-489c-ad01-32ca95932bc7"),
                                    containsString("/cases/2eef6095-06af-4c07-b989-795d64c86625/issues/f1fbc220-585b-422a-91bd-141378f07e38"))),
                            jsonPath("$._embedded.issues[*]._links.issues.href").value(containsInAnyOrder(
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

        @Test
        void shouldReturnApproriatePage() throws Exception {
            generateIssueDummies(17);

            mockMvc.perform(get("/cases/%s/issues?page=1".formatted("2eef6095-06af-4c07-b989-795d64c86625"))
                            .with(jwt().jwt(jwt -> jwt
                                    .claim("sub", "157e4056-3a6e-4410-bc15-f14ea86887b6")
                                    .claim("scope", "USER"))))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$._embedded.issues.length()").value(10),
                            jsonPath("$.page.size").value(10),
                            jsonPath("$.page.totalElements").value(20),
                            jsonPath("$.page.totalPages").value(2),
                            jsonPath("$.page.number").value(1));
        }

        @Test
        void withSizeShouldReturnAppropriatePageSize() throws Exception {
            generateIssueDummies(12);

            mockMvc.perform(get("/cases/%s/issues?size=5".formatted("2eef6095-06af-4c07-b989-795d64c86625"))
                    .with(jwt().jwt(jwt -> jwt
                            .claim("sub", "157e4056-3a6e-4410-bc15-f14ea86887b6")
                            .claim("scope", "USER"))))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$._embedded.issues.length()").value(5),
                            jsonPath("$.page.size").value(5),
                            jsonPath("$.page.totalElements").value(15),
                            jsonPath("$.page.totalPages").value(3),
                            jsonPath("$.page.number").value(0));
        }

        @Test
        void toUnknownCaseIdShouldReturnNotFound() throws Exception {
            String[] caseIds = {"17e3f075-4d00-4d70-ba24-68123777f0", "1", "CASE-001", "not-exists"};

            for (var caseId : caseIds) {
                mockMvc.perform(get("/cases/%s/issues".formatted(caseId))
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

            mockMvc.perform(get("/cases/%s/issues".formatted(caseId))
                            .with(jwt().jwt(jwt -> jwt
                                    .claim("sub", "157e4056-3a6e-4410-bc15-f14ea86887b6")
                                    .claim("scope", "USER"))))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errors").value("Cannot find Case with identity: %s".formatted(caseId)));
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

    private void generateIssueDummies(int count) {
        var sql = "INSERT INTO issues (id, case_id, type, body, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
        var issueList = new ArrayList<Map<String, Object>>(count);

        for (int i = 1; i <= count; i++) {
            issueList.add(Map.of(
                    "id", UUID.randomUUID(),
                    "case_id", UUID.fromString("2eef6095-06af-4c07-b989-795d64c86625"),
                    "type", IssueType.SINGLE_CHOICE,
                    "body", "Generated dummy issue content #%d".formatted(i),
                    "created_at", new Timestamp(new Date().getTime()),
                    "updated_at", new Timestamp(new Date().getTime())
            ));
        }

        jdbcTemplate.batchUpdate(
                sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        var issueMap = issueList.get(i);
                        ps.setObject(1, issueMap.get("id"));
                        ps.setObject(2, issueMap.get("case_id"));
                        ps.setObject(3, issueMap.get("type"), Types.OTHER);
                        ps.setString(4, (String) issueMap.get("body"));
                        ps.setTimestamp(5, (Timestamp) issueMap.get("created_at"));
                        ps.setTimestamp(6, (Timestamp) issueMap.get("updated_at"));
                    }

                    @Override
                    public int getBatchSize() {
                        return count;
                    }
                }
        );
    }
}
