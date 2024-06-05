package io.github.stackpan.examia.server.http;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:datasources/reset.sql", "classpath:datasources/schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:datasources/testdata/case.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class CaseHttpTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getAllShouldSuccess() {
        var response = restTemplate.getForEntity("/cases", String.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);

        var responseBody = JsonPath.parse(response.getBody());

        Integer caseResourceListLength = responseBody.read("$._embedded.caseResourceList.length()");
        assertEquals(5, caseResourceListLength);
    }
}
