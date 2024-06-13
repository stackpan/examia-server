package io.github.stackpan.examia.server.http;

import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:datasources/reset.sql", "classpath:datasources/schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:datasources/testdata/case.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class CaseHttpTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Nested
    public class ListCaseTest {

        @Test
        public void success() {
            var response = restTemplate.getForEntity("/cases", String.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());

            var responseBody = JsonPath.parse(response.getBody());

            Integer caseResourceListLength = responseBody.read("$._embedded.caseResourceList.length()");
            assertEquals(3, caseResourceListLength);

            JSONArray ids = responseBody.read("$._embedded.caseResourceList[*].id");
            assertTrue(ids.containsAll(List.of("2eef6095-06af-4c07-b989-795d64c86625", "2eef6095-06af-4c07-b989-795d64c86625", "2eef6095-06af-4c07-b989-795d64c86625")));

            JSONArray titles = responseBody.read("$._embedded.caseResourceList[*].title");
            assertTrue(titles.containsAll(List.of("Case Title 1", "Case Title 2", "Case Title 3")));

            JSONArray descriptions = responseBody.read("$._embedded.caseResourceList[*].description");
            assertTrue(descriptions.containsAll(List.of("Case 1 Description.", "Case 2 Description.", "Case 3 Description.")));

            JSONArray durationInSecondsArray = responseBody.read("$._embedded.caseResourceList[*].durationInSeconds");
            assertTrue(durationInSecondsArray.containsAll(List.of(1800, 1900, 2000)));

            JSONArray ownerIds = responseBody.read("$._embedded.caseResourceList[*].owner.id");
            assertTrue(ownerIds.contains("157e4056-3a6e-4410-bc15-f14ea86887b6"));

            JSONArray ownerUsernames = responseBody.read("$._embedded.caseResourceList[*].owner.username");
            assertTrue(ownerUsernames.contains("user"));

            JSONArray ownerEmails = responseBody.read("$._embedded.caseResourceList[*].owner.email");
            assertTrue(ownerEmails.contains("user@example.com"));

            JSONArray ownerFirstNames = responseBody.read("$._embedded.caseResourceList[*].owner.firstName");
            assertTrue(ownerFirstNames.contains("First"));

            JSONArray ownerLastNames = responseBody.read("$._embedded.caseResourceList[*].owner.lastName");
            assertTrue(ownerLastNames.contains("Last"));

            JSONArray createdAts = responseBody.read("$._embedded.caseResourceList[*].createdAt");
            assertTrue(createdAts.containsAll(List.of("2024-05-16T00:00:01Z", "2024-05-16T00:00:02Z", "2024-05-16T00:00:03Z")));

            JSONArray updatedAts = responseBody.read("$._embedded.caseResourceList[*].updatedAt");
            assertTrue(updatedAts.containsAll(List.of("2024-05-16T00:00:01Z", "2024-05-16T00:00:02Z", "2024-05-16T00:00:03Z")));

            JSONArray innerLinkArray = responseBody.read("$._embedded.caseResourceList[*]._links");
            assertEquals(3, innerLinkArray.size());

            assertNotNull(responseBody.read("$._links"));
            assertNotNull(responseBody.read("$.page"));
        }
    }

    @Nested
    public class GetCaseTest {

        @Test
        public void success() {
            var targetId = "2eef6095-06af-4c07-b989-795d64c86625";
            var response = restTemplate.getForEntity("/cases/%s".formatted(targetId), String.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());

            var responseBody = JsonPath.parse(response.getBody());

            String id = responseBody.read("$.id");
            assertEquals(targetId, id);

            String title = responseBody.read("$.title");
            assertEquals("Case Title 1", title);

            String description = responseBody.read("$.description");
            assertEquals("Case 1 Description.", description);

            Integer durationInSeconds = responseBody.read("$.durationInSeconds");
            assertEquals(1800, durationInSeconds);

            String ownerId = responseBody.read("$.owner.id");
            assertEquals("157e4056-3a6e-4410-bc15-f14ea86887b6", ownerId);

            String ownerUsername = responseBody.read("$.owner.username");
            assertEquals("user", ownerUsername);

            String ownerEmail = responseBody.read("$.owner.email");
            assertEquals("user@example.com", ownerEmail);

            String ownerFirstName = responseBody.read("$.owner.firstName");
            assertEquals("First", ownerFirstName);

            String ownerLastName = responseBody.read("$.owner.lastName");
            assertEquals("Last", ownerLastName);

            String createdAt = responseBody.read("$.createdAt");
            assertEquals("2024-05-16T00:00:01Z", createdAt);

            String updatedAt = responseBody.read("$.updatedAt");
            assertEquals("2024-05-16T00:00:01Z", updatedAt);

            assertNotNull(responseBody.read("$._links"));
        }

        @Test
        public void notFound() {
            var response = restTemplate.getForEntity("/cases/not-exists", String.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }
}
