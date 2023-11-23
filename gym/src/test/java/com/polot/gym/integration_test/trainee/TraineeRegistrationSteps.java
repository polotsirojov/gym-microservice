package com.polot.gym.integration_test.trainee;

import com.polot.gym.payload.request.TraineeRegisterRequest;
import com.polot.gym.payload.response.UsernamePasswordResponse;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TraineeRegistrationSteps {
    @Autowired
    private TestRestTemplate restTemplate;
    private TraineeRegisterRequest registerRequest;
    private ResponseEntity<UsernamePasswordResponse> responseRegTrainee;

    @Given("a trainee registration request with the following details:")
    public void givenTraineeRegistrationRequest(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> data = rows.get(0);

        registerRequest = new TraineeRegisterRequest();
        registerRequest.setFirstName(data.get("firstName"));
        registerRequest.setLastName(data.get("lastName"));

        if (data.containsKey("dob")) {
            registerRequest.setDob(LocalDate.parse(data.get("dob")));
        }

        if (data.containsKey("address")) {
            registerRequest.setAddress(data.get("address"));
        }
    }

    @When("the client sends a POST request to {string}")
    public void whenClientSendsPostRequest(String url) {
        responseRegTrainee = restTemplate.postForEntity(url, registerRequest, UsernamePasswordResponse.class);
        assertThat(responseRegTrainee.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Then("the response status code should be {int}")
    public void thenResponseStatusCodeShouldBe(int expectedStatusCode) {
        assertThat(responseRegTrainee.getStatusCode().value()).isEqualTo(expectedStatusCode);
    }

    @Then("the response body should contain the trainee's username and password")
    public void thenResponseBodyShouldContainTraineeDetails() {
        assertThat(responseRegTrainee.getBody().getUsername()).isNotNull();
        assertThat(responseRegTrainee.getBody().getPassword()).isNotNull();
    }

}
